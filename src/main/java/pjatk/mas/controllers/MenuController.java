package pjatk.mas.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pjatk.mas.config.SessionHelper;
import pjatk.mas.dto.request.AddIngredientRequestDTO;
import pjatk.mas.dto.response.MenuItemDetailDTO;
import pjatk.mas.services.MenuService;

import java.math.BigDecimal;

@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public String listMenu(Model model) {
        model.addAttribute("menuItems", menuService.getAllMenuItems());
        return "menu/list";
    }

    @GetMapping("/{id}")
    public String menuItemDetail(@PathVariable Long id, HttpSession session, Model model) {
        MenuItemDetailDTO menuItem = menuService.getMenuItemDetail(id);
        model.addAttribute("menuItem", menuItem);

        if (SessionHelper.getLoggedInChef(session).isPresent()) {
            model.addAttribute("allIngredients", menuService.getAllIngredients());
        }

        return "menu/detail";
    }

    @PostMapping("/{id}/ingredients/add")
    public String addIngredient(@PathVariable Long id, @RequestParam Long ingredientId, @RequestParam BigDecimal quantity,
                                HttpSession session, RedirectAttributes redirectAttributes) {
        if (SessionHelper.getLoggedInChef(session).isEmpty()) {
            return "redirect:/login?redirect=/menu/" + id;
        }
        try {
            menuService.addIngredient(id, ingredientId, quantity);
            redirectAttributes.addFlashAttribute("success", "Ingredient added");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error while adding ingredient: " + e.getMessage());
        }
        return "redirect:/menu/" + id;
    }

    @PostMapping("/{id}/ingredients/update")
    public String updateIngredient(@PathVariable Long id, @RequestParam Long ingredientQuantityId, @RequestParam BigDecimal quantity,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        if (SessionHelper.getLoggedInChef(session).isEmpty()) {
            return "redirect:/login?redirect=/menu/" + id;
        }
        try {
            menuService.updateIngredientQuantity(ingredientQuantityId, quantity);
            redirectAttributes.addFlashAttribute("success", "Ingredient updated");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error while updating: " + e.getMessage());
        }
        return "redirect:/menu/" + id;
    }

    @PostMapping("/{id}/ingredients/remove")
    public String removeIngredient(@PathVariable Long id, @RequestParam Long ingredientQuantityId,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        if (SessionHelper.getLoggedInChef(session).isEmpty()) {
            return "redirect:/login?redirect=/menu/" + id;
        }
        try {
            menuService.removeIngredient(id, ingredientQuantityId);
            redirectAttributes.addFlashAttribute("success", "Ingredient removed");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error while removing: " + e.getMessage());
        }
        return "redirect:/menu/" + id;
    }
}