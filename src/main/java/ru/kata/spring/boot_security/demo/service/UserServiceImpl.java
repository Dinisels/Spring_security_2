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
import ru.kata.spring.boot_security.demo.Repository.RoleRepository;

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
    public void saveUser(User user, List<Long> roleIds) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        setUserRoles(user, roleIds);

        userDao.saveUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        getUserById(id); // TODO present посмотреть мб через него без гет юзер бай айди // тут если не будет то выикнуть иск4лючение TODO
        userDao.deleteUser(id);
    }

    private void setUserRoles(User user, List<Long> roleIds) {  // TODO Тут сразу получить все роли одним запросом TODO
        Set<Role> roles = new HashSet<>();

        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long id : roleIds) {
                Role role = roleService.getRoleById(id);
                if (role != null) {
                    roles.add(role);
                }
            }
        }

        user.setRoles(roles);
    }

    // TODO тут username - это email !!!

    @Transactional
    @Override
    public void updateUser(long id, User user, List<Long> selectedRoles) {
        User existingUser = getUserById(id);

        existingUser.setName(user.getName());
        existingUser.setAge(user.getAge());

        if (!existingUser.getUsername().equals(user.getUsername())) { // TODO 98 - 103 убрать и выкинуть исключение TODO
            Optional<User> userWithNewUsername =
                    userRepository.findByUsername(user.getUsername());

            if (userWithNewUsername.isPresent()
                    && userWithNewUsername.get().getId() != id) {
                throw new RuntimeException(
                        "User with username " + user.getUsername() + "already exists"
                );
            }

            existingUser.setUsername(user.getUsername());
        }  // TODO FindByUsername optional и если такое уже есть то просто исключение выкинуть и все.. TODO

        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        setUserRoles(existingUser, selectedRoles); // TODO GetRolesByIdS и получать сразу все роли через дао

        userDao.updateUser(existingUser);
    }


}
