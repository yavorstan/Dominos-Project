package com.example.demo.model.entity;

import com.example.demo.model.dto.PostUserRegistrationFormDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String password;

    public User(PostUserRegistrationFormDto userDto) {
        setEmail(userDto.getEmail());
        setFirstName(userDto.getFirstName());
        setLastName(userDto.getLastName());
        setPassword(userDto.getPassword());
    }

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

}
