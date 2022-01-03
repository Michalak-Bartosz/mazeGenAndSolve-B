package wat.bartoszmichalak.mazegenandsolve.dto;

import lombok.Getter;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;
import wat.bartoszmichalak.mazegenandsolve.entities.Wall;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WallDto {

    private final Long id;
    private final int wallIndex;
    private final Long mazeId;
    private final Boolean isVisible;
    private final List<Long> neighbourCellsId;

    public WallDto(Wall wall) {
        this.id = wall.getWallId();
        this.wallIndex = wall.getWallIndex();
        this.mazeId = wall.getMaze().getMazeId();
        this.isVisible = wall.getIsVisible();
        this.neighbourCellsId = wall.getNeighbourCells()
                .stream()
                .map(Cell::getCellId)
                .collect(Collectors.toList());
    }
}
