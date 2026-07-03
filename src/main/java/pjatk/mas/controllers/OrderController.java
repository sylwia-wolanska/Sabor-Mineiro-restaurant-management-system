package pjatk.mas.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pjatk.mas.config.SessionHelper;
import pjatk.mas.dto.response.MenuItemDTO;
import pjatk.mas.dto.response.OrderDetailDTO;
import pjatk.mas.models.*;
import pjatk.mas.services.MenuService;
import pjatk.mas.services.OrderService;
import pjatk.mas.session.OrderWizardSession;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {

    private static final String WIZARD_KEY = "orderWizard";

    private final OrderService orderService;
    private final MenuService menuService;

    public OrderController(OrderService orderService, MenuService menuService) {
        this.orderService = orderService;
        this.menuService = menuService;
    }

    @GetMapping
    public String listOrders(HttpSession session, Model model) {
        Person person = SessionHelper.getLoggedInPerson(session).orElse(null);
        if (person == null) return "redirect:/login";
        model.addAttribute("orders", orderService.getOrdersForPerson(person));
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, HttpSession session, Model model) {
        Person person = SessionHelper.getLoggedInPerson(session).orElse(null);
        if (person == null) return "redirect:/login";
        try {
            OrderDetailDTO order = orderService.getOrderDetailById(id, person);
            model.addAttribute("order", order);
            return "orders/detail";
        } catch (IllegalStateException e) {
            return "redirect:/orders";
        }
    }

    @GetMapping("/new")
    public String step1ShowMenu(HttpSession session, Model model) {
        if (SessionHelper.getLoggedInClient(session).isEmpty()) {
            return "redirect:/login?redirect=/orders/new";
        }
        OrderWizardSession wizard = new OrderWizardSession();
        wizard.setClientId(SessionHelper.getLoggedInClient(session).get().getId());
        session.setAttribute(WIZARD_KEY, wizard);
        model.addAttribute("menuItems", menuService.getAllMenuItems());
        model.addAttribute("wizard", wizard);
        return "orders/step1-menu";
    }

    @PostMapping("/new/summary")
    public String step2ShowSummary(@RequestParam Map<String, String> params, HttpSession session, Model model) {
        OrderWizardSession wizard = getWizard(session);
        wizard.clearItems();

        params.forEach((key, value) -> {
            if (key.startsWith("qty_")) {
                try {
                    Long menuItemId = Long.parseLong(key.substring(4));
                    int qty = Integer.parseInt(value);
                    if (qty > 0) wizard.getSelectedItems().put(menuItemId, qty);
                } catch (NumberFormatException ignored) {}
            }
        });

        if (!wizard.hasItems()) {
            model.addAttribute("error", "Please select at least one item.");
            model.addAttribute("menuItems", menuService.getAllMenuItems());
            return "orders/step1-menu";
        }

        session.setAttribute(WIZARD_KEY, wizard);

        List<MenuItemDTO> allItems = menuService.getAllMenuItems();
        Map<MenuItemDTO, Integer> summaryItems = buildSummaryMap(wizard, allItems);

        model.addAttribute("summaryItems", summaryItems);
        model.addAttribute("totalPrice", calculateTotal(summaryItems));

        int estimatedPrepTime = summaryItems.keySet().stream().mapToInt(MenuItemDTO::preparationTime).max().orElse(0);
        model.addAttribute("estimatedPrepTime", estimatedPrepTime);

        return "orders/step2-summary";
    }

    @PostMapping("/new/pickup-options")
    public String step3PickupOptions(@RequestParam String action, HttpSession session, Model model) {
        if ("modify".equals(action)) {
            OrderWizardSession wizard = getWizard(session);
            model.addAttribute("menuItems", menuService.getAllMenuItems());
            model.addAttribute("previousSelections", wizard.getSelectedItems());
            return "orders/step1-menu";
        }
        return "orders/step3-delivery";
    }

    @PostMapping("/new/confirm")
    public String step4Confirm(@RequestParam String pickupOption, @RequestParam(required = false) String street,
                               @RequestParam(required = false) String buildingNumber, @RequestParam(required = false) Integer apartmentNumber,
                               @RequestParam(required = false) String hourOfPickup, HttpSession session, Model model) {
        OrderWizardSession wizard = getWizard(session);

        if ("pickup".equals(pickupOption)) {
            wizard.setPickupAtRestaurant(true);
            if (hourOfPickup != null && !hourOfPickup.isBlank()) {
                wizard.setHourOfPickup(LocalTime.parse(hourOfPickup));
            }
        } else {
            wizard.setPickupAtRestaurant(false);
            wizard.setStreet(street);
            wizard.setBuildingNumber(buildingNumber);
            wizard.setApartmentNumber(apartmentNumber);
        }

        session.setAttribute(WIZARD_KEY, wizard);

        List<MenuItemDTO> allItems = menuService.getAllMenuItems();
        Map<MenuItemDTO, Integer> summaryItems = buildSummaryMap(wizard, allItems);
        model.addAttribute("wizard", wizard);
        model.addAttribute("summaryItems", summaryItems);
        model.addAttribute("totalPrice", calculateTotal(summaryItems));
        return "orders/step4-confirm";
    }

    @PostMapping("/new/create")
    public String step5CreateOrder(@RequestParam String action, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        OrderWizardSession wizard = getWizard(session);

        if ("edit".equals(action)) {
            model.addAttribute("menuItems", menuService.getAllMenuItems());
            model.addAttribute("previousSelections", wizard.getSelectedItems());
            return "orders/step1-menu";
        }

        var clientOpt = SessionHelper.getLoggedInClient(session);
        if (clientOpt.isEmpty()) return "redirect:/login";

        try {
            Order order = clientOpt.get().makeOrder(wizard, orderService);
            session.removeAttribute(WIZARD_KEY);
            redirectAttributes.addFlashAttribute("orderId", order.getId());
            redirectAttributes.addFlashAttribute("totalPrice", order.getTotalPrice());
            redirectAttributes.addFlashAttribute("estimatedTime", order.countEstimatedPrepTime());
            return "redirect:/orders/new/done";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating order: " + e.getMessage());
            List<MenuItemDTO> allItems = menuService.getAllMenuItems();
            Map<MenuItemDTO, Integer> summaryItems = buildSummaryMap(wizard, allItems);
            model.addAttribute("wizard", wizard);
            model.addAttribute("summaryItems", summaryItems);
            model.addAttribute("totalPrice", calculateTotal(summaryItems));
            return "orders/step4-confirm";
        }
    }

    @GetMapping("/new/done")
    public String done() {
        return "orders/step5-done";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam OrderStatus status, HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Person person = SessionHelper.getLoggedInPerson(session).orElse(null);
        if (person == null || !person.getRole().equals("WAITER")) {
            redirectAttributes.addFlashAttribute("error", "Only waiters can update order status");
            return "redirect:/orders/" + id;
        }

        Order order = orderService.getOrderById(id);
        if (order.getWaiter() == null || !order.getWaiter().getId().equals(person.getId())) {
            redirectAttributes.addFlashAttribute("error", "You can only update orders assigned to you");
            return "redirect:/orders/" + id;
        }

        try {
            orderService.updateOrderStatus(id, status, person);
            redirectAttributes.addFlashAttribute("success", "Status updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    private OrderWizardSession getWizard(HttpSession session) {
        OrderWizardSession wizard = (OrderWizardSession) session.getAttribute(WIZARD_KEY);
        if (wizard == null) throw new IllegalStateException("Session expired.");
        return wizard;
    }

    private Map<MenuItemDTO, Integer> buildSummaryMap(OrderWizardSession wizard, List<MenuItemDTO> allItems) {
        Map<MenuItemDTO, Integer> result = new LinkedHashMap<>();
        wizard.getSelectedItems().forEach((id, qty) ->
                allItems.stream().filter(item -> item.id().equals(id)).findFirst().ifPresent(item -> result.put(item, qty)));
        return result;
    }

    private BigDecimal calculateTotal(Map<MenuItemDTO, Integer> items) {
        return items.entrySet().stream()
                .map(e -> e.getKey().price()
                        .multiply(BigDecimal.valueOf(e.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleSessionExpiry(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Your session expired. Please start again");
        return "redirect:/orders/new";
    }
}