package pk.first.application.pkspringapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pk.first.application.pkspringapplication.model.ERole;
import pk.first.application.pkspringapplication.model.Role;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
