package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.User;


import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(long id);

    void saveUser(User user, List<Long> roleIds);

    void deleteUser(long id);

    void updateUser(long id, User user, List<Long> selectedRoles);
}
