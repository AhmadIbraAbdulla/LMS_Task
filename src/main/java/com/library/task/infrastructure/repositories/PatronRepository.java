package com.library.task.infrastructure.repositories;

import com.library.task.models.Book;
import com.library.task.models.Patron;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {
    Page<Patron> findByIsDeletedFalse(Pageable pageable);
}
