package wat.bartoszmichalak.mazegenandsolve.entities;

import com.sun.istack.NotNull;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.SolveAlgorithmType;

import javax.persistence.*;
import java.util.List;

@Entity(name = "solveMaze")
public class SolvedMaze {

    @Id
    @GeneratedValue
    private Long solveId;

    @NotNull
    private Long mazeId;

    @NotNull
    private SolveAlgorithmType solveAlgorithmType;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cell> algorithmSteps;

    public SolvedMaze(Long mazeId, SolveAlgorithmType solveAlgorithmType, List<Cell> algorithmSteps) {
        this.mazeId = mazeId;
        this.solveAlgorithmType = solveAlgorithmType;
        this.algorithmSteps = algorithmSteps;
    }

    public SolvedMaze() {

    }

    public List<Cell> getAlgorithmSteps() {
        return algorithmSteps;
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
