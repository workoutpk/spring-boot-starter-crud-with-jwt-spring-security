package pk.first.application.pkspringapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pk.first.application.pkspringapplication.model.Space;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {

    @Query("from Space S WHERE S.universeName=?1")
    List<Space> findByUniverseName(String string);
}
