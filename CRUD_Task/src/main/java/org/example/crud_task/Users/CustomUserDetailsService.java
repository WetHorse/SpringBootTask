package org.example.crud_task.Users;

import lombok.RequiredArgsConstructor;
import org.example.crud_task.Users.Person;
import org.example.crud_task.Users.PersonRepository;
//import org.example.crud_task.Users.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private PersonRepository personRepository;

    @Autowired
    public CustomUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(username);
        if(person.isPresent()){
            var userObj = person.get();
            return User.builder().username(userObj.getUsername()).password(userObj.getPassword()).build();
        }
        else {
            throw new UnsupportedOperationException("Error");
        }
    }

}
