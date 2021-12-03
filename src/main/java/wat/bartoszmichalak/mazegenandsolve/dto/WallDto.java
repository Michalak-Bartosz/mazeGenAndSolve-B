package wat.bartoszmichalak.mazegenandsolve.dto;

import wat.bartoszmichalak.mazegenandsolve.entities.Cell;
import wat.bartoszmichalak.mazegenandsolve.entities.Wall;

import java.util.List;
import java.util.stream.Collectors;

public class WallDto {

    private final Long id;
    private final int wallIndex;
    private final Long mazeId;
    private final Boolean isVisible;
    private final List<Long> neighbourCellsId;

    public WallDto(Wall wall) {
        this.id = wall.getId();
        this.wallIndex = wall.getWallIndex();
        this.mazeId = wall.getMaze().getMazeId();
        this.isVisible = wall.getVisible();
        this.neighbourCellsId = wall.getNeighbourCells()
                .stream()
                .map(Cell::getId)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public int getWallIndex() {
        return wallIndex;
    }

    public Long getMazeId() {
        return mazeId;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public List<Long> getNeighbourCellsId() {
        return neighbourCellsId;
    }
}
