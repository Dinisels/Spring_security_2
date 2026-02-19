package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String admin(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/dash";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/newUser";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Integer id, Model model) {
        User user = userService.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    @PostMapping("/add")
    public String addUser(
            @RequestParam("username") String username,
            @RequestParam("age") Integer age,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam(value = "roles", required = false) List<String> roleNames,
            RedirectAttributes redirectAttributes) {

        // Проверка паролей
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Пароли не совпадают");
            return "redirect:/admin/add";
        }

        // Создаем нового пользователя
        User user = new User();
        user.setUsername(username);
        user.setAge(age);
        user.setEmail(email);
        user.setPassword(password);

        // Обрабатываем роли
        if (roleNames != null && !roleNames.isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : roleNames) {
                Role role = roleService.findByName(roleName);
                if (role != null) {
                    roles.add(role);
                }
            }
            user.setRoles(roles);
        }

        // Сохраняем пользователя
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "Пользователь успешно добавлен");
        return "redirect:/admin";
    }

    @PostMapping("/edit/{id}")
    public String editUser(
            @PathVariable Integer id,
            @RequestParam("username") String username,
            @RequestParam("age") Integer age,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            @RequestParam(value = "roles", required = false) List<String> roleNames,
            RedirectAttributes redirectAttributes) {

        User existingUser = userService.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid user Id:" + id));

        // Обновляем поля
        existingUser.setUsername(username);
        existingUser.setAge(age);
        existingUser.setEmail(email);

        // Обновляем пароль, если он указан
        if (password != null && !password.trim().isEmpty()) {
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Пароли не совпадают");
                return "redirect:/admin/edit/" + id;
            }
            existingUser.setPassword(password);
        }

        // Обновляем роли
        Set<Role> roles = new HashSet<>();
        if (roleNames != null && !roleNames.isEmpty()) {
            for (String roleName : roleNames) {
                Role role = roleService.findByName(roleName);
                if (role != null) {
                    roles.add(role);
                }
            }
        }
        existingUser.setRoles(roles);

        // Сохраняем изменения
        userService.save(existingUser);
        redirectAttributes.addFlashAttribute("success", "Пользователь успешно обновлен");
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Пользователь успешно удален");
        return "redirect:/admin";
    }
}