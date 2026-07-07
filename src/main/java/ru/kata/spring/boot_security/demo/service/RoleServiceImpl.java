package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDaoImpl;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    private final RoleDaoImpl roleDaoImpl;



    // private final RoleRepository roleRepository;

//    public RoleService(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    public Set<Role> getRolesByIds(List<Long> ids) {
//
//        if (ids == null || ids.isEmpty()) {
//            return new HashSet<>();
//        }
//
//        return new HashSet<>(roleRepository.findAllById(ids));
//    }


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

}


