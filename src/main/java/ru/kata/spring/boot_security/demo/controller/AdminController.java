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

    //TODO главная страница получает только пользователей, а роли и пустой юзер передаются при обновлении\создании


    @GetMapping
    public String getUserAddForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        //TODO model.addAttribute("roles", roleService.getAllRoles());  <-- перенести в гет маппинг апдейт юзер после получения юзера по айди
        // TODO model.addAttribute("newUser", new User());
        return "admin/adminPanel";
    }

    @GetMapping("/updateUser") // TODO тут {id} через pathvarible     editUserId - вот тут просто UserID т.к.
    public String getUserUpdateForm(@RequestParam(value = "editUserId") Long editUserId, Model model) {
        model.addAttribute("existingUser", userService.getUserById(editUserId));
        // вот сюда перенести
        return "admin/adminPanel";
    }

    @PostMapping("/saveUser") // TODO тут пусто
    public String saveUser(@ModelAttribute("newUser") User user,
                           @RequestParam(value = "newRoles") List<Long> newRoles) { // TODO newRoles - RolesIds т.е. нормальные названия
        userService.saveUser(user, newRoles);
        return "redirect:/admin";
        // TODO сделать гет маппинт и туда
        //TODO сюда перенести 32ю строчку пустого юзера и добавить сюда роли



    }

    @PostMapping("/updateUser") // TODO тут установить PutMapping просто на юзерс/id тут еще адрес править
    public String updateUser(@RequestParam("userId") long id,
                             @ModelAttribute("existingUser") User user,
                             @RequestParam(value = "selectedRoles", required = false) List<Long> selectedRoles) {
        userService.updateUser(id, user, selectedRoles);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser") // TODO users\id через пасвариблся
    public String deleteUser(@RequestParam("userId") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}