package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.Repository.RoleRepository;
import ru.kata.spring.boot_security.demo.Repository.UserRepository;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<Role> getRolesByNames(List<String> names) {
        if (names == null || names.isEmpty()) {
            return new HashSet<>();
        }

        List<String> roleNames = names.stream()
                .map(name -> "ROLE_" + name)
                .toList();

        return new HashSet<>(roleRepository.findAllByNameIn(roleNames));
    }
}


