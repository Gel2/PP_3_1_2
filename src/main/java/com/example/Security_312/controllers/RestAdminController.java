package com.example.Security_312.controllers;

import com.example.Security_312.models.Person;
import com.example.Security_312.service.PersonService;
import com.example.Security_312.util.NoSuchPersonExc;
import com.example.Security_312.util.PersonIncorrectData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestAdminController {
    private final PersonService personService;

    @Autowired
    public RestAdminController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/people")
    public List<Person> getAllPerson() {
        List<Person> personList = personService.findAll();
        return personList;
    }

    @GetMapping("/people/{id}")
    public ResponseEntity<Person> getOnePerson(@PathVariable int id) {
        Person person = personService.findById(id);

        if (person != null) {
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/people")
    public Person createPerson(@RequestBody Person person) {
        personService.save(person);
        return person;
    }


    @ExceptionHandler
    public ResponseEntity<PersonIncorrectData> handleException(NoSuchPersonExc exc) {
        PersonIncorrectData data = new PersonIncorrectData();
        data.setInfo(exc.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PersonIncorrectData> handleException(Exception exc) {
        PersonIncorrectData data = new PersonIncorrectData();
        data.setInfo(exc.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
