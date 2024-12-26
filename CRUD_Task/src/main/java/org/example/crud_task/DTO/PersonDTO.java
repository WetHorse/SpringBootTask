package org.example.crud_task.DTO;

import org.springframework.security.access.method.P;

public class PersonDTO {
    private String username;
    private String password;

    public PersonDTO(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
