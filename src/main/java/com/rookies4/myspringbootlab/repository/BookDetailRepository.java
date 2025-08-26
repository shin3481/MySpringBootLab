package com.rookies4.myspringbootlab.repository;

import com.rookies4.myspringbootlab.entity.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDetailRepository extends JpaRepository<BookDetail, Long> {

    Optional<BookDetail> findByBookId(Long bookId);
    @Query("SELECT b FROM BookDetail b JOIN FETCH b.book WHERE b.id= :id")
    Optional<BookDetail> findByIdWithBook(@Param("id") Long bookDetailId);

    List<BookDetail> findByPublisher(String publisher);
}
