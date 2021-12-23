package wat.bartoszmichalak.mazegenandsolve.dto;

import lombok.Getter;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.SolveAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.entities.SolvedMaze;

@Getter
public class SolvedMazeDto {
    private final Long solveId;
    private final Long mazeId;
    private final SolveAlgorithmType solveAlgorithmType;
    private final double solveTime;

    public SolvedMazeDto(SolvedMaze solvedMaze) {
        this.solveId = solvedMaze.getSolveId();
        this.mazeId = solvedMaze.getMazeId();
        this.solveAlgorithmType = solvedMaze.getSolveAlgorithmType();
        this.solveTime = solvedMaze.getSolveTime();
    }
}
