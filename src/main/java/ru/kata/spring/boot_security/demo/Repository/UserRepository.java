package ru.kata.spring.boot_security.demo.Repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @EntityGraph(value = "User.withRoles", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByUsername(String name);

    @EntityGraph(value = "User.withRoles", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String name);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, long id);

}
