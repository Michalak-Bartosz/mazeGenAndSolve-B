package wat.bartoszmichalak.mazegenandsolve.dto;

import lombok.Getter;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.Direction;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;
import wat.bartoszmichalak.mazegenandsolve.entities.Wall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CellDto {

    private final Long cellId;
    private final Long mazeId;
    private final int cellIndex;
    private final int positionX;
    private final int positionY;
    private final CellState cellState;
    private final Map<Direction, WallDto> walls;
    private final List<Long> neighbourCellsId;

    public CellDto(Cell cell) {
        this.cellId = cell.getCellId();
        this.mazeId = cell.getMaze().getMazeId();
        this.cellIndex = cell.getCellIndex();
        this.positionX = cell.getPositionX();
        this.positionY = cell.getPositionY();
        this.cellState = cell.getCellState();
        this.walls = mapWalls(cell.getWalls());
        this.neighbourCellsId = cell.getNeighbourCells()
                .stream()
                .map(Cell::getCellId)
                .collect(Collectors.toList());
    }

    private Map<Direction, WallDto> mapWalls(Map<Direction, Wall> walls) {
        Map<Direction, WallDto> wallsFactory = new HashMap<>();
        walls.forEach((direction, wall) ->
                wallsFactory.put(direction, new WallDto(wall)));
        return wallsFactory;
    }
}
