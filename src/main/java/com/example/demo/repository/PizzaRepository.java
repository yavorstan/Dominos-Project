package com.example.demo.repository;

import com.example.demo.model.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    Pizza findByName(String name);

    boolean existsByName(String name);

}
