package wat.bartoszmichalak.mazegenandsolve.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;

import java.util.List;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {

    @Override
    List<Cell> findAllById(Iterable<Long> iterable);
}
