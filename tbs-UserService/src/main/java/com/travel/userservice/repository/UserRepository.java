package com.travel.userservice.repository;

import com.travel.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    
    Optional<User> findByUserEmail(String userEmail);
    
    boolean existsByUserName(String userName);
    boolean existsByUserEmail(String userEmail);

} 