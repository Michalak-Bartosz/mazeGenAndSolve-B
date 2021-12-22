package wat.bartoszmichalak.mazegenandsolve.dto;

import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.SolveAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.entities.SolvedMaze;

public class SolvedMazeDto {
    private final Long solveId;
    private final Long mazeId;
    private final SolveAlgorithmType solveAlgorithmType;

    public SolvedMazeDto(SolvedMaze solvedMaze) {
        this.solveId = solvedMaze.getSolveId();
        this.mazeId = solvedMaze.getMazeId();
        this.solveAlgorithmType = solvedMaze.getSolveAlgorithmType();
    }

    public Long getSolveId() {
        return solveId;
    }

    public Long getMazeId() {
        return mazeId;
    }

    public SolveAlgorithmType getSolveAlgorithmType() {
        return solveAlgorithmType;
    }
}
