package com.project.auction.controller;

import com.project.auction.dto.ListingForm;
import com.project.auction.model.Listing;
import com.project.auction.model.User;
import com.project.auction.service.CategoryService;
import com.project.auction.service.ListingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes({"listingForm", "categories"})
public class CreateListingController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ListingService listingService;

    @ModelAttribute
    public ListingForm listingForm() {
        return new ListingForm();
    }

    @GetMapping("/create-listing")
    public String createListingPage(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "create";
    }

    @PostMapping("/create-listing")
    public String processListing(@Valid @ModelAttribute ListingForm form,
                                 BindingResult binding,
                                 Authentication authentication,
                                 SessionStatus session,
                                 RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            return "create";
        }
        Listing l = form.toListing((User) authentication.getPrincipal());
        boolean result = listingService.save(l);
        if (result) {
            session.setComplete();
            redirect.addFlashAttribute(
                    "successMessage",
                    "Created listing successfully");
            return "redirect:/";
        }
        else {
            binding.reject(
                    "error.listing",
                    "Cant not save listing to db");
            return "create";
        }
    }

}
