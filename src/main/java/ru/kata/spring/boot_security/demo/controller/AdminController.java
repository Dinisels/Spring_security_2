package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


    @GetMapping()
    public String getUserAddForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "/admin/adminPanel";
    }

    @PostMapping
    public String addUser(@ModelAttribute("newUser") User user,
                          @RequestParam(value = "newRoles", required = false) String[] newRoles,
                          RedirectAttributes redirectAttributes) {

        try {
            userService.saveUser(user, newRoles);
            redirectAttributes.addFlashAttribute("success", "Пользователь успешно добавлен");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin";
    }

    @GetMapping("/updateUser")
    public String getUserUpdateForm(@RequestParam("editUserId") Long editUserId,
                                    Model model) {

        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        model.addAttribute("existingUser", userService.getUserById(editUserId));

        return "admin/adminPanel";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam("userId") long id,
                             @ModelAttribute("existingUser") User user,
                             @RequestParam(value = "selectedRoles", required = false) String[] selectedRoles) {
        userService.updateUser(id, user, selectedRoles);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }



}