package wat.bartoszmichalak.mazegenandsolve.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.Direction;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.exceptions.MazeCellNotFoundException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity(name = "maze")
@NoArgsConstructor
public class Maze {

    @Id
    @GeneratedValue
    private Long mazeId;

    @NotNull
    private int height;

    @NotNull
    private int width;

    @NotNull
    private GenerateAlgorithmType algorithmType;

    @NotNull
    private double generateTime;

    @NotNull
    @OneToMany(mappedBy = "maze", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Cell> cells = new ArrayList<>();

    @NotNull
    @OneToMany(mappedBy = "maze", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Wall> walls = new ArrayList<>();

    @NotNull
    @OneToMany(mappedBy = "mazeId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<SolvedMaze> solvedMazes = new ArrayList<>();

    public Maze(int height, int width, GenerateAlgorithmType algorithmType) throws MazeCellNotFoundException {
        this.height = height;
        this.width = width;
        this.algorithmType = algorithmType;
        initMazeWallsList();
        initMazeCellsList();
    }

    private void initMazeWallsList() {
        for (int y = 0; y < 2 * width * height + width + height; y++) {
            this.walls.add(new Wall(this, y));
        }
    }

    private void initMazeCellsList() throws MazeCellNotFoundException {
        HashMap<Direction, Wall> setOfWalls;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                setOfWalls = getSetOfWallsForCell(x, y);
                cells.add(new Cell(this, cells.size(), x, y, setOfWalls));
            }
        }
        setNeighboursCells();
    }

    private HashMap<Direction, Wall> getSetOfWallsForCell(int x, int y) {
        HashMap<Direction, Wall> wallList = new HashMap<>();

        if (x == 0) {
            wallList.put(Direction.LEFT, walls.get(y));
            wallList.put(Direction.RIGHT, walls.get(2 * (width + height) + y));
        } else if (x == width - 1) {
            wallList.put(Direction.RIGHT, walls.get(height + y));
            wallList.put(Direction.LEFT, walls.get(2 * (width + height) + height * (x - 1) + y));
        } else {
            wallList.put(Direction.LEFT, walls.get(2 * (width + height) + (x - 1) * height + y));
            wallList.put(Direction.RIGHT, walls.get(2 * (width + height) + x * height + y));
        }

        if (y == 0) {
            wallList.put(Direction.TOP, walls.get(2 * height + x));
            wallList.put(Direction.BOTTOM, walls.get(2 * width + width * height + height + x));
        } else if (y == height - 1) {
            wallList.put(Direction.BOTTOM, walls.get(2 * height + width + x));
            wallList.put(Direction.TOP, walls.get(2 * (width + height) + (width - 1) * height + (y - 1) * width + x));
        } else {
            wallList.put(Direction.TOP, walls.get(2 * width + width * height + height + (y - 1) * width + x));
            wallList.put(Direction.BOTTOM, walls.get(2 * width + width * height + height + y * width + x));
        }

        return wallList;
    }

    private void setNeighboursCells() throws MazeCellNotFoundException {
        for (Cell cell : cells) {
            List<Cell> neighbourCells = getNeighboursCell(cell.getPositionX(), cell.getPositionY());
            cell.addNeighbourCells(neighbourCells);
        }
    }

    private List<Cell> getNeighboursCell(int x, int y) throws MazeCellNotFoundException {
        List<Cell> neighbourCells = new ArrayList<>();

        List<Cell> xList = cells.stream().filter(c -> c.getPositionX() == x).collect(Collectors.toList());
        List<Cell> yList = cells.stream().filter(c -> c.getPositionY() == y).collect(Collectors.toList());

        neighbourCells.addAll(getNeighbourCellByTheRow(x, yList));
        neighbourCells.addAll(getNeighbourCellByTheColumn(y, xList));

        return neighbourCells;
    }

    private List<Cell> getNeighbourCellByTheRow(int x, List<Cell> yList) throws MazeCellNotFoundException {
        List<Cell> neighbourCells = new ArrayList<>();
        if (x == 0) {
            neighbourCells.add(yList.stream()
                    .filter(c -> c.getPositionX() == (x + 1))
                    .findFirst()
                    .orElseThrow(() -> new MazeCellNotFoundException((long) (x + 1))));
        } else if (x == width - 1) {
            neighbourCells.add(yList.stream()
                    .filter(c -> c.getPositionX() == (x - 1))
                    .findFirst()
                    .orElseThrow(() -> new MazeCellNotFoundException((long) (x - 1))));
        } else {
            neighbourCells.add(yList.stream()
                    .filter(c -> c.getPositionX() == (x + 1))
                    .findFirst()
                    .orElseThrow(() -> new MazeCellNotFoundException((long) (x + 1))));
            neighbourCells.add(yList.stream()
                    .filter(c -> c.getPositionX() == (x - 1))
                    .findFirst()
                    .orElseThrow(() -> new MazeCellNotFoundException((long) (x - 1))));
        }
        return neighbourCells;
    }

    private List<Cell> getNeighbourCellByTheColumn(int y, List<Cell> xList) throws MazeCellNotFoundException {
        List<Cell> neighbourCells = new ArrayList<>();
        if (y == 0) {
            neighbourCells.add(xList.stream()
                    .filter(c -> c.getPositionY() == (y + 1)).findFirst()
                    .orElseThrow(() -> new MazeCellNotFoundException((long) (y + 1))));
        } else if (y == height - 1) {
            neighbourCells.add(xList.stream()
                    .filter(c -> c.getPositionY() == (y - 1)).findFirst()
                    .orElseThrow(() -> new MazeCellNotFoundException((long) (y - 1))));
        } else {
            neighbourCells.add(xList.stream()
                    .filter(c -> c.getPositionY() == (y - 1)).findFirst()
                    .orElseThrow(() -> new MazeCellNotFoundException((long) (y - 1))));
            neighbourCells.add(xList.stream()
                    .filter(c -> c.getPositionY() == (y + 1))
                    .findFirst()
                    .orElseThrow(() -> new MazeCellNotFoundException((long) (y + 1))));
        }
        return neighbourCells;
    }

    public boolean hasUnvisitedCells() {
        return this.cells.stream().anyMatch(c -> c.getCellState().equals(CellState.UNVISITED));
    }
}
