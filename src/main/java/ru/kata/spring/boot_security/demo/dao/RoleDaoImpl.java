package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Role> getAllRoles() {
        return em.createQuery("select r from Role r", Role.class).getResultList();
    }

    @Override
    public Role getRoleById(Long id) {
        return em.find(Role.class, id);
    }

    @Override
    public void saveRole(Role role) {
        em.persist(role);
    }
}
