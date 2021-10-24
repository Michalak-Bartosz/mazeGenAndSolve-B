package wat.bartoszmichalak.mazegenandsolve.entities;

import com.sun.istack.NotNull;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.Direction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "wall")
public class Wall {

    @Id
    @GeneratedValue
    Long id;

    @NotNull
    private int wallIndex;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "maze_id")
    private Maze maze;

    @NotNull
    Boolean isVisible;

    @NotNull
    @ManyToMany
    List<Cell> neighbourCells = new ArrayList<>();

    public Wall(Maze maze, int wallIndex) {
        this.maze = maze;
        this.wallIndex = wallIndex;
        this.isVisible = true;
        neighbourCells = new ArrayList<>();
    }

    public Wall() {

    }

    public Long getId() {
        return id;
    }

    public int getWallIndex() {
        return wallIndex;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public List<Cell> getNeighbourCells() {
        return neighbourCells;
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

    public void printNeighbourMazeCells() {
        System.out.println("Neighbour cells for wall id: " + id + " | index: " + wallIndex);
        for (Cell cell : neighbourCells) {
            System.out.println("x: " + cell.getPositionX() + " | y: " + cell.getPositionY() + " | id: " + cell.getId() + " | index: " + cell.getCellIndex());
        }
    }
}
