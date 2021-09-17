package wat.bartoszmichalak.mazegenandsolve.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;

import java.util.Optional;

@Repository
public interface MazeRepository extends JpaRepository<Maze, Long> {

    @Override
    Optional<Maze> findById(Long id);
}
