package wat.bartoszmichalak.mazegenandsolve.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "wall")
public class Wall {

    @Id
    @GeneratedValue
    private Long id;

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

    public Wall() {

    }

    public void addNeighbourMazeCell(Cell cell) {
        neighbourCells.add(cell);
    }

    public boolean hasCellAsNeighbour(Cell cell) {
        return neighbourCells.contains(cell);
    }

    public boolean hasUnvisitedNeighbourCell() {
        return neighbourCells.stream().anyMatch(c -> c.getCellState().equals(CellState.UNVISITED));
    }

    //TODO Add exception throw
    public Cell getUnvisitedNeighbourCell() {
        return neighbourCells.stream().filter(c -> c.getCellState().equals(CellState.UNVISITED)).findFirst().orElseThrow();
    }
}
