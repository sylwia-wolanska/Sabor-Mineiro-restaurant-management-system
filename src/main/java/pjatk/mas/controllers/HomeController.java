package pjatk.mas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController extends BaseController{
    @GetMapping("/")
    public String home() {
        return "redirect:/orders";
    }
}