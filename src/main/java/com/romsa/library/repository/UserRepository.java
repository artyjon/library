package com.romsa.library.repository;

import com.romsa.library.entity.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    void deleteUserById(Long id);

    boolean existsByEmail(@Email(message = "Invalid email format") String email);

    @Query("""
        select count(u) > 0 from User u
        where u.email = :firstEmail and u.email <> :secondEmail
        """)
    boolean existsByEmailAndEmailIsNotTheSame(@Param("firstEmail") String firstEmail, @Param("secondEmail") String secondEmail);
}