package com.romsa.library.repository;

import com.romsa.library.entity.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    @Query("select b from BookLoan b where b.user.id = ?1 and b.book.id = ?2")
    Optional<BookLoan> findByUserIdAndBookId(Long userId, Long bookId);

    boolean existsByBookId(Long id);

    boolean existsByUserId(Long id);
}
