package com.example.webapp3.Repositories;

import com.example.webapp3.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByOrderByIdAsc();
    List<User> findByOrderByIdDesc();
}
