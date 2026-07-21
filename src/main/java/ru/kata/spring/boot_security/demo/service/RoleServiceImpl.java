package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.Repository.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.RoleDaoImpl;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService{
    private final RoleDaoImpl roleDaoImpl;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleDaoImpl roleDaoImpl) {
        this.roleDaoImpl = roleDaoImpl;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDaoImpl.getAllRoles();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleDaoImpl.getRoleById(id);
    }

    @Transactional
    @Override
    public void saveRole(Role role) {
        roleDaoImpl.saveRole(role);
    }

    @Override
    public Set<Role> getRolesByIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new HashSet<>();
        }

        return new HashSet<>(roleRepository.findAllById(roleIds));
    }

}


