package pjatk.mas.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pjatk.mas.config.SessionHelper;
import pjatk.mas.dto.request.RegistrationRequestDTO;
import pjatk.mas.models.Person;
import pjatk.mas.services.AuthenticationService;

import java.util.Optional;

@Controller
public class AuthenticationController extends BaseController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String showLoginForm(HttpSession session, @RequestParam(required = false) String redirect, Model model) {
        if (SessionHelper.isLoggedIn(session)) return "redirect:/orders";
        model.addAttribute("redirect", redirect);
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) String redirect,
                               HttpSession session, Model model) {
        Optional<Person> person = authenticationService.login(username, password);
        if (person.isEmpty()) {
            model.addAttribute("error", "Invalid username or password");
            return "auth/login";
        }
        SessionHelper.login(session, person.get());
        if (redirect != null && !redirect.isBlank()) return "redirect:" + redirect;
        return "redirect:/orders";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        SessionHelper.logout(session);
        redirectAttributes.addFlashAttribute("success", "You have been logged out");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(HttpSession session, Model model) {
        if (SessionHelper.isLoggedIn(session)) return "redirect:/orders";
        model.addAttribute("form", new RegistrationRequestDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute RegistrationRequestDTO form, HttpSession session, Model model) {
        try {
            var client = authenticationService.register(form);
            SessionHelper.login(session, client);
            return "redirect:/orders";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("form", form);
            return "auth/register";
        }
    }
}