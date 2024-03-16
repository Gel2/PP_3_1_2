package com.example.Security_312.controllers;

import com.example.Security_312.models.Person;
import com.example.Security_312.repositories.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class UserController {
    private final PersonRepository personRepository;

    public UserController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/user")
    public String getUserPage(Model model, Principal principal) {
        String username = principal.getName();
        Person person = personRepository.findByUsername(username).get();
        model.addAttribute("person", person);
        return "/user";
    }
}
