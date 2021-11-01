package wat.bartoszmichalak.mazegenandsolve.dto;

import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;

import java.util.List;

public class MazeDto {
    private final Long id;
    private final int height;
    private final int wight;
    private final GenerateAlgorithmType algorithmType;
    private final List<Cell> cellsList;

    public MazeDto(Long id, int height, int wight, GenerateAlgorithmType algorithmType, List<Cell> cellsList) {
        this.id = id;
        this.height = height;
        this.wight = wight;
        this.algorithmType = algorithmType;
        this.cellsList = cellsList;
    }

    public MazeDto(Maze maze) {
        id = maze.getMazeId();
        height = maze.getHeight();
        wight = maze.getWidth();
        algorithmType = maze.getAlgorithmType();
        cellsList = maze.getCells();
    }

    public Long getId() {
        return id;
    }

    public int getHeight() {
        return height;
    }

    public int getWight() {
        return wight;
    }

    public GenerateAlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public List<Cell> getMazeCellsList() {
        return cellsList;
    }
}
