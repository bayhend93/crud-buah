package com.example.spring.boot.crud.spring_boot.repositories;

import com.example.spring.boot.crud.spring_boot.entities.Buah;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuahRepository extends CrudRepository<Buah,Long> {
    List<Buah> findByName(String name);
}
