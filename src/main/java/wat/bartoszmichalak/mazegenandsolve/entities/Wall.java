package wat.bartoszmichalak.mazegenandsolve.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "wall")
@NoArgsConstructor
@Getter
@Setter
public class Wall {

    @Id
    @GeneratedValue
    private Long wallId;

    @NotNull
    private int wallIndex;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "maze_id")
    private Maze maze;

    @NotNull
    private Boolean isVisible;

    @NotNull
    @ManyToMany
    private List<Cell> neighbourCells = new ArrayList<>();

    public Wall(Maze maze, int wallIndex) {
        this.maze = maze;
        this.wallIndex = wallIndex;
        this.isVisible = true;
        neighbourCells = new ArrayList<>();
    }

    public void addNeighbourMazeCell(Cell cell) {
        neighbourCells.add(cell);
    }

    public boolean hasCellAsNeighbour(Cell cell) {
        return neighbourCells.contains(cell);
    }

    public List<Cell> getUnvisitedNeighbourCells() {
        return neighbourCells.stream().filter(c -> c.getCellState().equals(CellState.UNVISITED)).collect(Collectors.toList());
    }
}
