package wat.bartoszmichalak.mazegenandsolve.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wat.bartoszmichalak.mazegenandsolve.entities.SolvedMaze;

public interface SolvedMazeRepository extends JpaRepository<SolvedMaze, Long> {
}
