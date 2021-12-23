package wat.bartoszmichalak.mazegenandsolve.dto;

import lombok.Getter;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.SolveAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;
import wat.bartoszmichalak.mazegenandsolve.entities.SolvedMaze;

import java.util.List;

@Getter
public class SolveParamsDto {
    private Long mazeId;
    private SolveAlgorithmType solveAlgorithmType;
    private Long startCellId;
    private Long endCellId;

    public SolveParamsDto() {
    }

    public SolveParamsDto(SolvedMaze solvedMaze) {
        this.mazeId = solvedMaze.getMazeId();
        this.solveAlgorithmType = solvedMaze.getSolveAlgorithmType();
        this.startCellId = extractStartCellId(solvedMaze.getAlgorithmSteps());
        this.endCellId = extractEndCellId(solvedMaze.getAlgorithmSteps());
    }

    private Long extractStartCellId(List<Cell> algorithmSteps) {
        return algorithmSteps.stream()
                .filter(cell -> cell.getCellState().equals(CellState.START))
                .findFirst()
                .map(Cell::getId)
                .orElse(null);
    }

    private Long extractEndCellId(List<Cell> algorithmSteps) {
        return algorithmSteps.stream()
                .filter(cell -> cell.getCellState().equals(CellState.END))
                .findFirst()
                .map(Cell::getId)
                .orElse(null);
    }
}
