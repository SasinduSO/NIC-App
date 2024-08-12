package  com.example.user_management_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_management_service.models.User;
public interface  userRep extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);
    
}
