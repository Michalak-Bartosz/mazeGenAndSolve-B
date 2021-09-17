package wat.bartoszmichalak.mazegenandsolve.entities;

import com.sun.istack.NotNull;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.Direction;

import javax.persistence.*;
import java.util.*;

@Entity(name = "maze_cell")
public class MazeCell {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "maze_id")
    private Maze maze;

    @NotNull
    private int positionX;

    @NotNull
    private int positionY;

    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    private List<MazeCellWall> walls = new ArrayList<>(4);

    public MazeCell(Maze maze, int positionX, int positionY, List<MazeCellWall> walls) {
        this.maze = maze;
        this.positionX = positionX;
        this.positionY = positionY;
        this.walls = walls;
    }

    public MazeCell() { }

    public Long getId() {
        return id;
    }

    public Maze getMaze() {
        return this.maze;
    }

    public int getPositionX() {
        return this.positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public List<MazeCellWall> getWalls() {
        return walls;
    }

    public void setWalls(List<MazeCellWall> walls) {
        this.walls = walls;
    }

    public void setWall(MazeCellWall newWall) {
        //TODO exception
        MazeCellWall firstWall = this.walls.stream()
                .filter(mazeCellWall -> mazeCellWall.direction.equals(newWall.direction))
                .findFirst()
                .orElseThrow();
        firstWall.isVisible = newWall.isVisible;
        firstWall.neighbourMazeCells = newWall.neighbourMazeCells;
    }
}
