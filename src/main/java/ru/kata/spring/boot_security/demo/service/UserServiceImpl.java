package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.Repository.UserRepository;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {


    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void saveUser(User user, String[] newRoles) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with email " + user.getUsername() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        setUserRoles(user, newRoles);
        userDao.saveUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        getUserById(id);
        userDao.deleteUser(id);
    }

    private void setUserRoles(User user, String[] selectedRoles) {
        if (selectedRoles != null) {
            Set<Role> roleSet = new HashSet<>();
            for (String roleName : selectedRoles) {
                roleSet.add(roleService.getRoleByName(roleName));
            }
            user.setRoles(roleSet);
        }
    }

    // TODO тут username - это просто name !!!

    @Transactional
    @Override
    public void updateUser(long id, User user, String[] selectedRoles) {
        user.setId(id);
        User existingUser = getUserById(user.getId());
        existingUser.setName(user.getName());
        existingUser.setAge(user.getAge());

        if (!existingUser.getUsername().equals(user.getUsername())) {
            // Если email изменился, проверяем не занят ли новый email
            Optional<User> userWithNewEmail = userRepository.findByUsername(user.getUsername());
            if (userWithNewEmail.isPresent() && userWithNewEmail.get().getId() != id) {
                throw new RuntimeException("User with email " + user.getUsername() + " already exists");
            }
            existingUser.setUsername(user.getUsername());
        }


        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        setUserRoles(existingUser, selectedRoles);
        userDao.updateUser(existingUser);
    }



}
