package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin/user")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    //TODO главная страница получает только пользователей, а роли и пустой юзер передаются при обновлении\создании
    //TODO сделать гет маппинг для создании
    //TODO привести адреса согласно рест

//    GET    /admin/user
//    GET    /admin/user/new
//    POST   /admin/user
//    GET    /admin/user/{id}/edit
//    PUT    /admin/user/{id}
//    DELETE /admin/user/{id}

    @GetMapping
    public String getAdminPanel(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/adminPanel";
    }

    @GetMapping("/new")
    public String getUserAddForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());

        return "admin/adminPanel";
    }

    @PostMapping
    public String saveUser(
            @ModelAttribute("newUser") User user,
            @RequestParam("newRoles") List<Long> roleIds
    ) {
        userService.saveUser(user, roleIds);
        return "redirect:/admin/user";
    }


    //TODO сделать через PathVarible
    @GetMapping("/{userId}/edit")
    public String getUserUpdateForm(
            @PathVariable Long userId,
            Model model
    ) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("existingUser", userService.getUserById(userId));

        return "admin/adminPanel";
    }

    //TODO здесь пут маппинг
    @PutMapping("/{id}")
    public String updateUser(
            @PathVariable Long id,
            @ModelAttribute("existingUser") User user,
            @RequestParam(value = "selectedRoles", required = false) List<Long> roleIds) {

        userService.updateUser(id, user, roleIds);
        return "redirect:/admin/user";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/user";
    }


}


