package com.romsa.library.repository;

import com.romsa.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    void deleteUserById(Long id);

    boolean existsByEmail(String email);

    @Query("""
                select count(u) > 0 from User u
                where u.email = :email and u.id <> :userId
            """)
    boolean existsByEmailAndNotSameUser(@Param("email") String email, @Param("userId") Long userId);
}