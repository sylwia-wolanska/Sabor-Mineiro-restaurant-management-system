package pjatk.mas.config;

import jakarta.servlet.http.HttpSession;
import pjatk.mas.models.Chef;
import pjatk.mas.models.Client;
import pjatk.mas.models.Person;

import java.util.Optional;

public class SessionHelper {

    public static final String SESSION_KEY = "loggedInPerson";

    public static void login(HttpSession session, Person person) {
        session.setAttribute(SESSION_KEY, person);
    }


    public static void logout(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
        session.invalidate();
    }

    public static Optional<Person> getLoggedInPerson(HttpSession session) {
        Person person = (Person) session.getAttribute(SESSION_KEY);
        return Optional.ofNullable(person);
    }

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_KEY) != null;
    }

    public static boolean isClient(HttpSession session) {
        return getLoggedInPerson(session).map(p -> p instanceof Client).orElse(false);
    }

    public static boolean isChef(HttpSession session) {
        return getLoggedInPerson(session).map(p -> p instanceof Chef).orElse(false);
    }

    public static Optional<Client> getLoggedInClient(HttpSession session) {
        return getLoggedInPerson(session).filter(p -> p instanceof Client).map(p -> (Client) p);
    }

    public static Optional<Chef> getLoggedInChef(HttpSession session) {
        return getLoggedInPerson(session).filter(p -> p instanceof Chef).map(p -> (Chef) p);
    }
}
