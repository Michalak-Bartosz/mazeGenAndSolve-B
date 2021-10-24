package wat.bartoszmichalak.mazegenandsolve.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.bartoszmichalak.mazegenandsolve.entities.Wall;

@Repository
public interface WallRepository extends JpaRepository<Wall, Long>{

}
