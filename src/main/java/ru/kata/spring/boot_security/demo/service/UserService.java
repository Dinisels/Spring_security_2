package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.Repository.RoleRepository;
import ru.kata.spring.boot_security.demo.Repository.UserRepository;
import ru.kata.spring.boot_security.demo.dto.UserCreateDto;
import ru.kata.spring.boot_security.demo.dto.UserUpdateDto;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }


    public User createUser(UserCreateDto dto) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Пароли не совпадают");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email уже используется");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        user.setRoles(roleService.getRolesByNames(dto.getRoles()));

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Integer id, UserUpdateDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(dto.getUsername());
        user.setAge(dto.getAge());

        // проверка email
        if (!user.getEmail().equals(dto.getEmail()) &&
                userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email уже используется");
        }

        user.setEmail(dto.getEmail());

        // пароль
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {

            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new RuntimeException("Пароли не совпадают");
            }

            user.setPassword(dto.getPassword());
        }

        user.setRoles(roleService.getRolesByNames(dto.getRoles()));

        return userRepository.save(user);
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }


    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }


}
