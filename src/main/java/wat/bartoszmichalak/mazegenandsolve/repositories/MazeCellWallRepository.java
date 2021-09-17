package wat.bartoszmichalak.mazegenandsolve.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.bartoszmichalak.mazegenandsolve.entities.MazeCellWall;

@Repository
public interface MazeCellWallRepository extends JpaRepository<MazeCellWall, Long>{

}
