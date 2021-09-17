package wat.bartoszmichalak.mazegenandsolve.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;
import wat.bartoszmichalak.mazegenandsolve.entities.MazeCell;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface MazeCellRepository extends JpaRepository<MazeCell, Long> {

    Optional<ArrayList<MazeCell>> findMazeCellsByMaze(Maze maze);
}
