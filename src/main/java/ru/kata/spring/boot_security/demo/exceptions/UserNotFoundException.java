package ru.kata.spring.boot_security.demo.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super("User not found with id = " + id);
    }
}
