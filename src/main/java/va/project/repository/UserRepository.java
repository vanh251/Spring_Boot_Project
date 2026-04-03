package va.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import va.project.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
// chức năng đăng nhập:
    Optional<User> findByUsername(String username);
//chức năng đăng ký:
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
