package wat.bartoszmichalak.mazegenandsolve.dto;

import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;

public class MazeDto {
    private final Long id;
    private final int height;
    private final int width;
    private final GenerateAlgorithmType algorithmType;

    public MazeDto(Maze maze) {
        id = maze.getMazeId();
        height = maze.getHeight();
        width = maze.getWidth();
        algorithmType = maze.getAlgorithmType();
    }

    public GenerateAlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Long getId() {
        return id;
    }
}
