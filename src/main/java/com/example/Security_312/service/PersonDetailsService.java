package com.example.Security_312.service;

import com.example.Security_312.models.Person;
import com.example.Security_312.repositories.PersonRepository;
import com.example.Security_312.security.PersonDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> personOptional = personRepository.findByUsername(username);
        if (personOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not faund");
        }
        return new PersonDetails(personOptional.get());
    }
}
