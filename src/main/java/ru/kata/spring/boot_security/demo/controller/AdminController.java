package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.dto.UserCreateDto;
import ru.kata.spring.boot_security.demo.dto.UserUpdateDto;
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
        //тут еще и роли все передалть из ролсервиса

        return "admin/newUser";
    }



    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Integer id, Model model) {

        User user = userService.getById(id);

        model.addAttribute("user", user);
        return "admin/edit-user";
    }

//    @PostMapping("/add")
//    public String addUser(@RequestBody UserCreateDto dto,
//                          RedirectAttributes redirectAttributes) {
//
//        try {
//            userService.createUser(dto);
//            redirectAttributes.addFlashAttribute("success",
//                    "Пользователь успешно добавлен");
//        } catch (RuntimeException e) {
//            redirectAttributes.addFlashAttribute("error", e.getMessage());
//            return "redirect:/admin/add";
//        }
//
//        return "redirect:/admin";
//    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute UserCreateDto dto,
                          RedirectAttributes redirectAttributes) {

        try {
            userService.createUser(dto);
            redirectAttributes.addFlashAttribute("success", "Пользователь успешно добавлен");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/add";
        }

        return "redirect:/admin";
    }



    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Integer id,
                           @ModelAttribute UserUpdateDto dto,
                           RedirectAttributes redirectAttributes) {

        try {

            dto.setId(id);
            userService.updateUser(dto);

            redirectAttributes.addFlashAttribute("success",
                    "Пользователь успешно обновлен");

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute("error",
                    e.getMessage());

            return "redirect:/admin/edit/" + id;
        }

        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Пользователь успешно удален");
        return "redirect:/admin";
    }
}