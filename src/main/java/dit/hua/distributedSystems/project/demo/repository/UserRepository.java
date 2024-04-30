package dit.hua.distributedSystems.project.demo.repository;

import dit.hua.distributedSystems.project.demo.entity.MUser;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RepositoryRestResource(path= "user")
@Hidden
public interface UserRepository  extends JpaRepository<MUser, Integer> {

    Optional<MUser> findByUsername(String username);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
