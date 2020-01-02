package com.example.demo.repository;

import com.example.demo.model.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    Optional<Pizza> findByName(String name);

    boolean existsByName(String name);

}
