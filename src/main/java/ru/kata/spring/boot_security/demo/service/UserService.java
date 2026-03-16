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
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();

            for (String roleName : dto.getRoles()) {
                Role role = roleRepository.findByName(roleName);
                if (role != null) {
                    roles.add(role);
                }
            }

            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UserUpdateDto dto) {

        User existingUser = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(dto.getUsername());
        existingUser.setAge(dto.getAge());
        existingUser.setEmail(dto.getEmail());

        // обновление пароля
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {

            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new RuntimeException("Пароли не совпадают");
            }

            existingUser.setPassword(dto.getPassword());
        }

        // обновление ролей
        if (dto.getRoles() != null) {

            Set<Role> roles = new HashSet<>();

            for (String roleName : dto.getRoles()) {
                Role role = roleRepository.findByName(roleName);
                if (role != null) {
                    roles.add(role);
                }
            }

            existingUser.setRoles(roles);
        }

        return userRepository.save(existingUser);
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email уже используется");
        }
        return userRepository.save(user);
    }

    public User update(User user) {
       return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Role findRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
