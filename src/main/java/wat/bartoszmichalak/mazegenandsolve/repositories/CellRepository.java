package wat.bartoszmichalak.mazegenandsolve.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {

    Optional<ArrayList<Cell>> findMazeCellsByMaze(Maze maze);
}
