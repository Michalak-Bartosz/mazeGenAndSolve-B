package wat.bartoszmichalak.mazegenandsolve.dto;

import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.Direction;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;
import wat.bartoszmichalak.mazegenandsolve.entities.Wall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CellDto {

    private final Long id;
    private final Long mazeId;
    private final int cellIndex;
    private final int positionX;
    private final int positionY;
    private final CellState cellState;
    private final Map<Direction, WallDto> walls;
    private final List<Long> neighbourCellsId;

    public CellDto(Cell cell) {
        this.id = cell.getId();
        this.mazeId = cell.getMaze().getMazeId();
        this.cellIndex = cell.getCellIndex();
        this.positionX = cell.getPositionX();
        this.positionY = cell.getPositionY();
        this.cellState = cell.getCellState();
        this.walls = mapWalls(cell.getWalls());
        this.neighbourCellsId = cell.getNeighbourCells()
                .stream()
                .map(Cell::getId)
                .collect(Collectors.toList());
    }

    private Map<Direction, WallDto> mapWalls(Map<Direction, Wall> walls) {
        Map<Direction, WallDto> wallsFactory = new HashMap<>();
        walls.forEach((direction, wall) ->
                wallsFactory.put(direction, new WallDto(wall)));
        return wallsFactory;
    }

    public Long getId() {
        return id;
    }

    public Long getMazeId() {
        return mazeId;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public CellState getCellState() {
        return cellState;
    }

    public Map<Direction, WallDto> getWalls() {
        return walls;
    }

    public List<Long> getNeighbourCellsId() {
        return neighbourCellsId;
    }
}
