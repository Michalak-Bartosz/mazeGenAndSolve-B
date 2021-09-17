package wat.bartoszmichalak.mazegenandsolve.entities;

import com.sun.istack.NotNull;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.Direction;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "maze")
public class Maze {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private int height;

    @NotNull
    private int width;

    @NotNull
    private GenerateAlgorithmType algorithmType;

    @NotNull
    @OneToMany(mappedBy = "maze", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<MazeCell> mazeCells = new ArrayList<>();

    public Maze(int height, int width, GenerateAlgorithmType algorithmType) {
        this.height = height;
        this.width = width;
        this.algorithmType = algorithmType;
        initMazeCellsList();
    }

    public Maze() { }

    public void initMazeCellsList() {
        MazeCell mazeCell;
        List<MazeCellWall> mazeCellWalls = new ArrayList<>();
        for(int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                mazeCellWalls = initWalls(x, y, mazeCellWalls);
                mazeCell = new MazeCell(this, x, y, mazeCellWalls);
                setNeighbourMazeCellInWalls(mazeCellWalls, mazeCell);
                this.mazeCells.add(mazeCell);
            }
        }
    }

    private List<MazeCellWall> initWalls(int x, int y, List<MazeCellWall> pastMazeCellWalls) {
        List<MazeCellWall> walls = new ArrayList<>();

        if(x == 0 && y == 0) {
            for (Direction direction: Direction.values()) {
                walls.add(new MazeCellWall(false, direction));
            }
        } else if (x == 0) {
            walls.add(pastMazeCellWalls.stream().filter(wall -> wall.direction.equals(Direction.LEFT)).findFirst().orElseThrow());
            walls.add(new MazeCellWall(false, Direction.TOP));
            walls.add(new MazeCellWall(false, Direction.RIGHT));
            walls.add(new MazeCellWall(false, Direction.BOTTOM));
        } else if (y == 0){
            walls.add(pastMazeCellWalls.stream().filter(wall -> wall.direction.equals(Direction.TOP)).findFirst().orElseThrow());
            walls.add(new MazeCellWall(false, Direction.LEFT));
            walls.add(new MazeCellWall(false, Direction.RIGHT));
            walls.add(new MazeCellWall(false, Direction.BOTTOM));
        } else {
            walls.add(pastMazeCellWalls.stream().filter(wall -> wall.direction.equals(Direction.TOP)).findFirst().orElseThrow());
            walls.add(pastMazeCellWalls.stream().filter(wall -> wall.direction.equals(Direction.LEFT)).findFirst().orElseThrow());
            walls.add(new MazeCellWall(false, Direction.RIGHT));
            walls.add(new MazeCellWall(false, Direction.BOTTOM));
        }

        return walls;
    }

    private void setNeighbourMazeCellInWalls(List<MazeCellWall> walls, MazeCell mazeCell) {
        for (MazeCellWall wall : walls) {
            wall.addNeighbourMazeCell(mazeCell);
        }
    }

    public Long getId() {
        return id;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public GenerateAlgorithmType getAlgorithmType() {
        return this.algorithmType;
    }

    public List<MazeCell> getMazeCells() {
        return this.mazeCells;
    }
}
