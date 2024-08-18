package  com.example.user_management_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user_management_service.models.User;

@Repository
public interface  userRep extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
}
