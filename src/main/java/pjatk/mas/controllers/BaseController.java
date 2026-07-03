package pjatk.mas.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import pjatk.mas.config.SessionHelper;
import pjatk.mas.models.Person;

public abstract class BaseController {

    @ModelAttribute("loggedInPerson")
    public Person addLoggedInPersonToModel(HttpSession session) {
        return SessionHelper.getLoggedInPerson(session).orElse(null);
    }
}