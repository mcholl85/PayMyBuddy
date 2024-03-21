package com.payme.buddy.controller;

import com.payme.buddy.dto.*;
import com.payme.buddy.service.ConnexionService;
import com.payme.buddy.service.PayMeService;
import com.payme.buddy.service.TransactionService;
import com.payme.buddy.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Log4j2
@Controller
public class PayMeController {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ConnexionService connexionService;

    @Autowired
    private PayMeService payMeService;

    @GetMapping("/home")
    public String getUser(Model model, Principal principal) {
        UserDto userDto = userService.getUserDtoByEmail(principal.getName());
        model.addAttribute("user", userDto);

        return "home";
    }

    @GetMapping("/login")
    public String redirectIfAuthenticated(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);

        return "login";
    }

    @GetMapping("/transfer")
    public String getTransfer(Model model, Principal principal, @RequestParam(defaultValue = "1") int page, TransactionFormDto transactionForm) {
        if (page < 1) {
            return "redirect:/transfer?page=1";
        }

        String email = principal.getName();
        model.addAttribute("transactionFormDto", transactionForm);

        Page<TransactionDto> transactionDtoList = transactionService.getTransactionsBySender(email, page, 5);
        model.addAttribute("transactions", transactionDtoList);

        List<ConnexionDto> connexionDtoList = connexionService.findConnectedUsersFrom(email);
        model.addAttribute("connexions", connexionDtoList);

        return "transfer";
    }

    @PostMapping("/transfer")
    public String sendMoney(@ModelAttribute @Valid TransactionFormDto transactionForm, BindingResult bindingResult, Principal principal, Model model) {
        String email = principal.getName();
        model.addAttribute("transactionFormDto", transactionForm);

        if (!bindingResult.hasErrors() && userService.exceedBalance(email, transactionForm.getAmount())) {
            bindingResult.rejectValue("amount", "user.error", "You don't have enough money in your account");
        }
        if (bindingResult.hasErrors()) {
            return getTransfer(model, principal, 1, transactionForm);
        }

        try {
            payMeService.sendMoney(email, transactionForm);
        } catch (Exception e) {
            log.error("Transaction failed. User: {}. Form: {}", principal.getName(), transactionForm.toString());
        }

        return "redirect:/transfer";
    }

    @GetMapping("/contact")
    public String getContactForm(ContactFormDto contactFormDto, Model model, Principal principal) {
        String email = principal.getName();
        model.addAttribute("contactFormDto", contactFormDto);

        List<ConnexionDto> connexionDtoList = connexionService.findConnectedUsersFrom(email);
        model.addAttribute("connexions", connexionDtoList);

        return "contact";
    }

    @PostMapping("/contact")
    public String postContactForm(@ModelAttribute @Valid ContactFormDto contactFormDto, BindingResult bindingResult, Principal principal, Model model) {
        String email = principal.getName();
        UserDto userDto2 = userService.getUserDtoByEmail(contactFormDto.getEmail());

        if (email.equals(contactFormDto.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "you select your account email !");
        }
        if (!StringUtils.isBlank(contactFormDto.getEmail()) && userDto2 == null) {
            bindingResult.rejectValue("email", "error.user", "user does not exist");
        }
        if (userDto2 != null && connexionService.existConnexion(email, userDto2.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "connexion already exists");
        }
        if (bindingResult.hasErrors()) {
            log.info("Validation Form errors: {}", bindingResult.getFieldErrors());

            return getContactForm(contactFormDto, model, principal);
        }

        payMeService.addConnexion(email, userDto2.getEmail());

        return "redirect:/transfer";
    }

    @GetMapping("/contact/delete/{email}")
    public String deleteContact(@PathVariable String email, Principal principal) {
        try {
            this.connexionService.removeConnexion(principal.getName(), email);
        } catch (Exception e) {
            log.error("Error deleting connexion : {} {}", email, principal.getName());
        }

        return "redirect:/contact";
    }

    @GetMapping("/profile")
    public String getProfile(@RequestParam(defaultValue = "1") int page, ProfileFormDto profileFormDto, Model model, Principal principal) {
        if (page < 1) {
            log.error("Query Params page error: {}", page);
            return "redirect:/profile?page=1";
        }

        UserDto userDto = userService.getUserDtoByEmail(principal.getName());
        Page<TransactionDto> transactionDtoList = transactionService.getTransactionsByRecipient(principal.getName(), 1, 5);

        model.addAttribute("user", userDto);
        model.addAttribute("profileFormDto", profileFormDto);
        model.addAttribute("transactions", transactionDtoList);

        return "profile";
    }

    @PostMapping("/profile")
    public String postProfileForm(@ModelAttribute @Valid ProfileFormDto profileFormDto, BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            return getProfile(1, profileFormDto, model, principal);
        }

        userService.addToBalance(principal.getName(), profileFormDto.getAmount());

        return "redirect:/profile";
    }
}
