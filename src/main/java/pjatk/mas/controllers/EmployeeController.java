package pjatk.mas.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pjatk.mas.config.SessionHelper;
import pjatk.mas.dto.request.CreateEmployeeRequestDTO;
import pjatk.mas.models.Person;
import pjatk.mas.services.EmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeController extends BaseController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private boolean isManager(HttpSession session) {
        return SessionHelper.getLoggedInPerson(session).map(p -> p.getRole().equals("MANAGER")).orElse(false);
    }

    @GetMapping
    public String listEmployees(HttpSession session, Model model) {
        if (!isManager(session)) return "redirect:/orders";
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees/list";
    }

    @GetMapping("/new")
    public String showCreateForm(HttpSession session, Model model) {
        if (!isManager(session)) return "redirect:/orders";
        model.addAttribute("form", new CreateEmployeeRequestDTO());
        return "employees/new";
    }

    @PostMapping("/new")
    public String createEmployee(@ModelAttribute CreateEmployeeRequestDTO form, HttpSession session, Model model,
                                 RedirectAttributes redirectAttributes) {
        if (!isManager(session)) return "redirect:/orders";
        try {
            employeeService.createEmployee(form);
            redirectAttributes.addFlashAttribute("success", "Employee created successfully.");
            return "redirect:/employees";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("form", form);
            return "employees/new";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isManager(session)) return "redirect:/orders";
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("success", "Employee removed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/employees";
    }
}