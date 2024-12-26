package org.example.crud_task.Users;

import lombok.RequiredArgsConstructor;
import org.example.crud_task.DTO.AuthResponseDTO;
import org.example.crud_task.DTO.PersonDTO;
import org.example.crud_task.Security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/api/users")

public class PersonController {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final   JWTGenerator jwtGenerator;

    @Autowired
    public PersonController(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RoleRepository roleRepository, PersonRepository personRepository, JWTGenerator jwtGenerator) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.personRepository = personRepository;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody PersonDTO personDTO){
        if(personRepository.findByUsername(personDTO.getUsername()).isEmpty()){
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }
        Person person = new Person();
        person.setUsername(personDTO.getUsername());
        person.setPassword(passwordEncoder.encode(personDTO.getPassword()));

        Role roles = roleRepository.findByName("USER").get();
        person.setRoles(Collections.singletonList(roles));

        personRepository.save(person);
        return new ResponseEntity<>("Registration complete", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody PersonDTO personDTO){
        Authentication authentication = authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(personDTO.getUsername(), personDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }

//    @GetMapping("/users")
//    public List<Person> getAllUsers() {
//        return users;
//    }
//
//    @DeleteMapping("/{id}")
//    public String deleteUser(@PathVariable int id) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            personService.deleteUserById(id);
//            return "User deleted";
//        }
//        throw new RuntimeException("Access Denied");
//    }




}
