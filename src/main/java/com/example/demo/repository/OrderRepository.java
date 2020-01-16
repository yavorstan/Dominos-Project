package com.example.demo.repository;

import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    ArrayList<Order> findAllByUser(User user);

}
