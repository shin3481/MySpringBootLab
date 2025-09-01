package com.rookies4.myspringbootlab.repository;

import com.rookies4.myspringbootlab.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository  extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByName(String name);
    @Query("SELECT p FROM Publisher p JOIN FETCH p.book WHERE p.id = :id")
    Optional<Publisher> findByIdWithBook(@Param("id") Long id);
    //Name 중복체크를 위한 메서드
    boolean existsByName(String name);
}
