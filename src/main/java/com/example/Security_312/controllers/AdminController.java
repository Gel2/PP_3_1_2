package com.example.Security_312.controllers;

import com.example.Security_312.models.Person;
import com.example.Security_312.models.Role;
import com.example.Security_312.repositories.RoleRepository;
import com.example.Security_312.service.PersonService;
import com.example.Security_312.util.PersonValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RoleRepository roleRepository;
    private final PersonService personService;
    private final PersonValidator personValidator;

    public AdminController(RoleRepository roleRepository, PersonService personService, PersonValidator personValidator) {

        this.roleRepository = roleRepository;
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping("")
    public String indexView(Model model) {
        model.addAttribute("person", personService.findAll());
        return "/admin";
    }


//    //Получение пользователя по id
//    @GetMapping("users/show")
//    public String getUser(@RequestParam("id") long id, Model model) {
//        Optional<Person> userOpt = personService.findById(id);
//        Person user = userOpt.orElseThrow(() -> new NoSuchElementException("User not found")); // Выбросить исключение, если значение отсутствует
//        model.addAttribute("person", user);
//        return "/show";
//    }


    //Создание нового пользователя
    @GetMapping("users/new")
    public String newUser(Model model) {
        model.addAttribute("person", new Person());
        return "/new";
    }

    @PostMapping("users")
    public String addUser(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, Model model) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/new"; // Вернуть страницу регистрации с ошибками
        }

        try {
            personService.save(person);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // Передать сообщение об ошибке на страницу
            return "/new"; // Вернуть страницу регистрации с сообщением об ошибке
        }

        return "redirect:/admin";
    }

    @GetMapping("users/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        Optional<Person> userOpt = Optional.ofNullable(personService.findById(id));
        Person user = userOpt.orElseThrow(() -> new NoSuchElementException("User not found")); // Выбросить исключение, если значение отсутствует

        List<Role> listRole = roleRepository.findAll();
        model.addAttribute("person", user);
        model.addAttribute("allRoles", listRole);
        return "/edit";
    }


    @PostMapping("users/update")
    public String updateUser(@ModelAttribute("person") Person updatedPerson, @RequestParam("id") Long id, Model model) {
        Person existingPerson = personService.findById(id);

        if (existingPerson != null) {
            // Обновляем только измененные поля
            existingPerson.setUsername(updatedPerson.getUsername());
            existingPerson.setAge(updatedPerson.getAge());
            existingPerson.setRoleList(updatedPerson.getRoleList()); // Передаем роли из формы
            existingPerson.setPassword(updatedPerson.getPassword()); // Передаем пароль из формы

            personService.update(id, existingPerson);
        } else {
            model.addAttribute("error", "User not found."); // Добавляем пользовательское сообщение об ошибке
        }

        return "redirect:/admin";
    }


    @PostMapping("users/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        personService.remove(id);
        return "redirect:/admin";
    }

}
