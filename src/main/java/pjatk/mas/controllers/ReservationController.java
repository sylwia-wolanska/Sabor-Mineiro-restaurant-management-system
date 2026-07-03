package pjatk.mas.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pjatk.mas.config.SessionHelper;
import pjatk.mas.dto.response.ReservationDetailDTO;
import pjatk.mas.dto.response.ReservationSummaryDTO;
import pjatk.mas.models.*;
import pjatk.mas.services.ReservationService;
import pjatk.mas.session.ReservationWizardSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservations")
public class ReservationController extends BaseController {

    private static final String WIZARD_KEY = "reservationWizard";

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String listReservations(HttpSession session, Model model) {
        Person person = SessionHelper.getLoggedInPerson(session).orElse(null);
        if (person == null) return "redirect:/login";
        List<ReservationSummaryDTO> reservations = reservationService.getReservationsForPerson(person);
        model.addAttribute("reservations", reservations);
        return "reservations/list";
    }

    @GetMapping("/{id}")
    public String reservationDetail(@PathVariable Long id, HttpSession session, Model model) {
        Person person = SessionHelper.getLoggedInPerson(session).orElse(null);
        if (person == null) return "redirect:/login";
        try {
            ReservationDetailDTO reservation = reservationService.getReservationDetailById(id, person);
            model.addAttribute("reservation", reservation);
            return "reservations/detail";
        } catch (IllegalStateException e) {
            return "redirect:/reservations";
        }
    }

    @GetMapping("/new")
    public String step1ShowForm(HttpSession session, Model model) {
        Optional<Client> clientOpt = SessionHelper.getLoggedInClient(session);
        if (clientOpt.isEmpty()) {
            return "redirect:/login?redirect=/reservations/new";
        }
        ReservationWizardSession wizard = new ReservationWizardSession();
        wizard.setClientId(clientOpt.get().getId());
        session.setAttribute(WIZARD_KEY, wizard);
        model.addAttribute("wizard", wizard);
        return "reservations/step1-form";
    }

    @PostMapping("/new/find-table")
    public String step2FindTable(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam int numberOfPeople, HttpSession session, Model model) {

        ReservationWizardSession wizard = getWizard(session);
        wizard.setDateTime(dateTime);
        wizard.setNumberOfPeople(numberOfPeople);

        DiningTable table = reservationService.checkFreeTables(dateTime, numberOfPeople);

        if (table == null) {
            model.addAttribute("wizard", wizard);
            model.addAttribute("numberOfPeople", numberOfPeople);
            model.addAttribute("dateTime", dateTime);
            return "reservations/step2-no-table";
        }

        wizard.setFoundTableId(table.getId());
        session.setAttribute(WIZARD_KEY, wizard);

        model.addAttribute("wizard", wizard);
        model.addAttribute("table", table);
        model.addAttribute("numberOfPeople", numberOfPeople);
        model.addAttribute("dateTime", dateTime);
        return "reservations/step2-summary";
    }

    @PostMapping("/new/confirm")
    public String step3Confirm(@RequestParam(required = false) String action, HttpSession session, Model model,
                               RedirectAttributes redirectAttributes) {
        ReservationWizardSession wizard = getWizard(session);

        if ("edit".equals(action)) {
            model.addAttribute("wizard", wizard);
            return "reservations/step1-form";
        }

        Optional<Client> clientOpt = SessionHelper.getLoggedInClient(session);
        if (clientOpt.isEmpty()) return "redirect:/login";

        try {
            Reservation reservation = clientOpt.get().makeReservation(wizard, reservationService);
            session.removeAttribute(WIZARD_KEY);
            redirectAttributes.addFlashAttribute("reservationId", reservation.getId());
            redirectAttributes.addFlashAttribute("dateTime", reservation.getDateTime());
            redirectAttributes.addFlashAttribute("numberOfPeople", reservation.getCountOfPeople());
            redirectAttributes.addFlashAttribute("tableMaxPeople", reservation.getDiningTable().getMaxPeople());
            redirectAttributes.addFlashAttribute("status", reservation.getReservationStatus());
            return "redirect:/reservations/new/done";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating reservation: " + e.getMessage());
            model.addAttribute("wizard", wizard);
            return "reservations/step1-form";
        }
    }

    @GetMapping("/new/done")
    public String done() {return "reservations/step3-done";}

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam ReservationStatus status, HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Person person = SessionHelper.getLoggedInPerson(session).orElse(null);
        if (person == null || !person.getRole().equals("WAITER")) {
            redirectAttributes.addFlashAttribute("error", "Only waiters can update reservation status");
            return "redirect:/reservations/" + id;
        }
        try {
            reservationService.updateReservationStatus(id, status, person);
            redirectAttributes.addFlashAttribute("success", "Status updated");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations/" + id;
    }

    @GetMapping("/{id}/reschedule")
    public String showRescheduleForm(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Optional<Client> clientOpt = SessionHelper.getLoggedInClient(session);
        if (clientOpt.isEmpty()) {
            return "redirect:/login?redirect=/reservations";
        }

        try {
            ReservationDetailDTO reservation = reservationService.getReservationDetailById(id, clientOpt.get());
            if (!reservation.canReschedule()) {
                redirectAttributes.addFlashAttribute("error", "This reservation cannot be rescheduled");
                return "redirect:/reservations";
            }
            model.addAttribute("reservation", reservation);
            return "reservations/reschedule";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservations";
        }
    }

    @PostMapping("/{id}/reschedule")
    public String processReschedule(@PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dateTime, HttpSession session, RedirectAttributes redirectAttributes) {

        Optional<Client> clientOpt = SessionHelper.getLoggedInClient(session);
        if (clientOpt.isEmpty()) {
            return "redirect:/login?redirect=/reservations";
        }

        try {
            reservationService.reschedule(id, dateTime, clientOpt.get());
            redirectAttributes.addFlashAttribute("success", "Reservation rescheduled successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservations/" + id + "/reschedule";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error rescheduling: " + e.getMessage());
            return "redirect:/reservations/" + id + "/reschedule";
        }
        return "redirect:/reservations";
    }

    private ReservationWizardSession getWizard(HttpSession session) {
        ReservationWizardSession wizard = (ReservationWizardSession) session.getAttribute(WIZARD_KEY);
        if (wizard == null) throw new IllegalStateException("Session expired");
        return wizard;
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleSessionExpiry(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Your session expired. Please start again");
        return "redirect:/reservations/new";
    }
}