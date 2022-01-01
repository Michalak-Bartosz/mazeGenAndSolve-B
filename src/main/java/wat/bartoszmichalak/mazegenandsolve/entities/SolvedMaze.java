package wat.bartoszmichalak.mazegenandsolve.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.SolveAlgorithmType;

import javax.persistence.*;
import java.util.List;

@Entity(name = "solveMaze")
@NoArgsConstructor
@Getter
public class SolvedMaze {

    @Id
    @GeneratedValue
    private Long solveId;

    @NotNull
    private Long mazeId;

    @NotNull
    private SolveAlgorithmType solveAlgorithmType;

    @NotNull
    private double solveTime;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cell> algorithmSteps;

    public SolvedMaze(Long mazeId, SolveAlgorithmType solveAlgorithmType, List<Cell> algorithmSteps, double solveTime) {
        this.mazeId = mazeId;
        this.solveAlgorithmType = solveAlgorithmType;
        this.algorithmSteps = algorithmSteps;
        this.solveTime = solveTime;
    }
}
