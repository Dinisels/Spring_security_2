package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userPage(Model model) {
        // Получаем текущего аутентифицированного пользователя
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            // В вашем случае getUsername() возвращает email
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        System.out.println("Looking for user with email: " + email);

        // Ищем пользователя по email (так как email используется как логин)
        Optional<User> user = userService.findByEmail(email);

        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            System.out.println("Found user: " + user.get().getUsername() +
                    ", email: " + user.get().getEmail() +
                    ", age: " + user.get().getAge());
        } else {
            System.out.println("User not found for email: " + email);
            // Для отладки можно создать минимальный объект
            User minimalUser = new User();
            minimalUser.setUsername("Unknown");
            minimalUser.setEmail(email);
            model.addAttribute("user", minimalUser);
        }

        return "user"; // Это должен быть ваш HTML файл (user.html)
    }
}