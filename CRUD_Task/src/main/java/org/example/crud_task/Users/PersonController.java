package org.example.crud_task.Users;

import org.example.crud_task.DTO.AuthResponseDTO;
import org.example.crud_task.DTO.LoginDTO;
import org.example.crud_task.DTO.RegisterDTO;
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

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")

public class PersonController {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PersonRepository personRepository;
    private final   JWTGenerator jwtGenerator;

    @Autowired
    public PersonController(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,  PersonRepository personRepository, JWTGenerator jwtGenerator) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.personRepository = personRepository;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDTO registerDTO) {
        if (personRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);
        }

        Person person = new Person();
        person.setUsername(registerDTO.getUsername());
        person.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(person.getUsername(), null);
        String jwt = jwtGenerator.generateToken(authentication.getName());
        System.out.println("jwt "+jwt);


        personRepository.save(person);

        return new ResponseEntity<>("Registration complete", HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication.getName());
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }


    @GetMapping("/users")
    public ResponseEntity<List<Person>> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<Person> users = personRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        Person person = personRepository.findById(id).orElse(null);
        if (person == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        personRepository.delete(person);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

}
