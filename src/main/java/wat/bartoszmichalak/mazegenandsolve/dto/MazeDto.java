package wat.bartoszmichalak.mazegenandsolve.dto;

import lombok.Getter;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;

@Getter
public class MazeDto {
    private final Long mazeId;
    private final int height;
    private final int width;
    private final GenerateAlgorithmType genAlgorithmType;
    private final double generateTime;

    public MazeDto(Maze maze) {
        this.mazeId = maze.getMazeId();
        this.height = maze.getHeight();
        this.width = maze.getWidth();
        this.genAlgorithmType = maze.getAlgorithmType();
        this.generateTime = maze.getGenerateTime();
    }
}
