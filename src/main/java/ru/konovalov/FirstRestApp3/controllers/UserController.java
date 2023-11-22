package ru.konovalov.FirstRestApp3.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.konovalov.FirstRestApp3.models.User;

import java.time.LocalDate;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/")
    public ResponseEntity<User> sayLoginPassword() {
        System.out.println("Request!!!!!");
        return new ResponseEntity<>(new User("Ivan", "2511121"), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<User> sayLoginPasswordDate(@RequestBody User user) {
        /*if(user.getLogin().isEmpty()){
            System.out.println("Bad request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }*/
        System.out.println("Request in post");
        user.setLocalDate(LocalDate.now());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    /*@PostMapping("/")
    public User sayLoginPasswordDate(@RequestBody User user) {
        System.out.println("Request in post");
        user.setLocalDate(LocalDate.now());
        return user;
    }*/
}
