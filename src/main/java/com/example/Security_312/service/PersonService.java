package com.example.Security_312.service;

import com.example.Security_312.models.Person;
import com.example.Security_312.repositories.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonService {
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    public PersonService(PasswordEncoder passwordEncoder, PersonRepository personRepository) {
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
    }


    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findById(long id) {
        return personRepository.findById(id).get();
    }

    public void save(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);
    }

    public void update(Long id, Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.saveAndFlush(person);
    }

    public void remove(Long id) {
        personRepository.deleteById(id);
    }
}
