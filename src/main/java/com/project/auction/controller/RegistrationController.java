package com.project.auction.controller;

import com.project.auction.dto.RegistrationForm;
import com.project.auction.exception.PasswordMismatchException;
import com.project.auction.model.User;
import com.project.auction.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
@Slf4j
@SessionAttributes("regForm")
public class RegistrationController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;

    public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute("regForm")
    public RegistrationForm registrationForm() {
        return new RegistrationForm();
    }

    @GetMapping
    public String registrationPage() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(
            @Valid @ModelAttribute("regForm") RegistrationForm registrationForm,
            BindingResult binding,
            SessionStatus session,
            RedirectAttributes redirectAttributes
            ) {
        if (binding.hasErrors()) {
            return "registration";
        }
        try {
            User newUser = registrationForm.toUser(passwordEncoder);
            userRepo.save(newUser);
            log.info(String.format("User %s with email %s registered",
                    newUser.getUsername(),
                    newUser.getEmail()));
            session.setComplete();
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Created new account!");
            return "redirect:/login";
        }
        catch (PasswordMismatchException pme) {
            log.error("Password mismatch");
            binding.rejectValue(
                    "confirmation",
                    "error.registration",
                    "Passwords do not match.");
            return "registration";
        }
        catch (DataIntegrityViolationException | ConstraintViolationException de) {
            log.error("data integrity violation");
            binding.reject(
                    "error.registration",
                    "Data integrity violation");
            return "registration";
        }

    }
}
