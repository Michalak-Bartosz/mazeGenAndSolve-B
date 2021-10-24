package wat.bartoszmichalak.mazegenandsolve.entities;

import com.sun.istack.NotNull;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.Direction;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    @OneToMany(mappedBy = "maze", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private final List<Cell> cells = new ArrayList<>();

    @NotNull
    @OneToMany(mappedBy = "maze", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Wall> walls = new ArrayList<>();

    public Maze(int height, int width, GenerateAlgorithmType algorithmType) {
        this.height = height;
        this.width = width;
        this.algorithmType = algorithmType;
        initMazeWallsList();
        initMazeCellsList();
    }

    public Maze() {
    }

    private void initMazeWallsList() {
        for (int y = 0; y < 2 * width * height + width + height; y++) {
            this.walls.add(new Wall(this, y));
        }
    }

    private void initMazeCellsList() {
        HashMap<Direction, Wall> setOfWalls = new HashMap<>();
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

        if(x == 0) {
            wallList.put(Direction.LEFT, walls.get(y));
            wallList.put(Direction.RIGHT, walls.get(2 * (width + height) + y));
        } else if (x == width - 1) {
            wallList.put(Direction.RIGHT, walls.get(height + y));
            wallList.put(Direction.LEFT, walls.get(2 * (width + height) + height * (x - 1) + y));
        } else {
            wallList.put(Direction.LEFT, walls.get(2 * (width + height) + (x - 1) * height + y));
            wallList.put(Direction.RIGHT, walls.get(2 * (width + height) + x * height + y));
        }

        if(y == 0) {
            wallList.put(Direction.TOP, walls.get(2 * height + x));
            wallList.put(Direction.BOTTOM, walls.get(2 * width + width * height + height + x));
        } else if (y == height - 1) {
            wallList.put(Direction.BOTTOM, walls.get(2 * height + width + x));
            wallList.put(Direction.TOP, walls.get(2* (width + height) + (width - 1) * height + (y - 1) * width + x));
        } else {
            wallList.put(Direction.TOP, walls.get(2 * width + width * height + height + (y - 1) * width + x));
            wallList.put(Direction.BOTTOM, walls.get(2 * width + width * height + height + y * width + x));
        }

        return wallList;
    }

    private void setNeighboursCells() {
        for (Cell cell : cells) {
            List<Cell> neighbourCells = getNeighboursCell(cell.getPositionX(), cell.getPositionY());
            cell.addNeighbourCells(neighbourCells);
        }
    }

    private List<Cell> getNeighboursCell(int x, int y) {
        List<Cell> neighbourCells = new ArrayList<>();

        List<Cell> xList = cells.stream().filter(c -> c.getPositionX() == x).collect(Collectors.toList());
        List<Cell> yList = cells.stream().filter(c -> c.getPositionY() == y).collect(Collectors.toList());

        neighbourCells.addAll(getNeighbourCellByTheRow(x, y, yList));
        neighbourCells.addAll(getNeighbourCellByTheColumn(x, y, xList));

        return neighbourCells;
    }

    private List<Cell> getNeighbourCellByTheRow(int x, int y, List<Cell> yList) {
        List<Cell> neighbourCells = new ArrayList<>();
        if (x == 0) {
            neighbourCells.add(yList.stream().filter(c -> c.getPositionX() == (x + 1)).findFirst().orElseThrow());
        } else if (x == width - 1) {
            neighbourCells.add(yList.stream().filter(c -> c.getPositionX() == (x - 1)).findFirst().orElseThrow());
        } else {
            neighbourCells.add(yList.stream().filter(c -> c.getPositionX() == (x + 1)).findFirst().orElseThrow());
            neighbourCells.add(yList.stream().filter(c -> c.getPositionX() == (x - 1)).findFirst().orElseThrow());
        }
        return neighbourCells;
    }

    private List<Cell> getNeighbourCellByTheColumn(int x, int y, List<Cell> xList) {
        List<Cell> neighbourCells = new ArrayList<>();
        if (y == 0) {
            neighbourCells.add(xList.stream().filter(c -> c.getPositionY() == (y + 1)).findFirst().orElseThrow());
        } else if (y == height - 1) {
            neighbourCells.add(xList.stream().filter(c -> c.getPositionY() == (y - 1)).findFirst().orElseThrow());
        } else {
            neighbourCells.add(xList.stream().filter(c -> c.getPositionY() == (y - 1)).findFirst().orElseThrow());
            neighbourCells.add(xList.stream().filter(c -> c.getPositionY() == (y + 1)).findFirst().orElseThrow());
        }
        return neighbourCells;
    }

    private void setCellsNeighbours() {

    }

    private void setWallsNeighbours() {

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

    public List<Cell> getCells() {
        return this.cells;
    }

    public List<Wall> getWalls() {
        return this.walls;
    }

    public boolean hasUnvisitedCells() {
        return this.cells.stream().anyMatch(c -> c.getCellState().equals(CellState.UNVISITED));
    }

    public void printMazeASCII() {
        List<Wall> printedWalls = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int finalX = x;
                int finalY = y;
                Cell cell = cells.stream().filter(c -> c.getPositionY() == finalY && c.getPositionX() == finalX).findFirst().orElseThrow();
                printedWalls.add(cell.printCellASCIITop(printedWalls));
            }
            System.out.println();
            for (int x = 0; x < width; x++) {
                int finalX = x;
                int finalY = y;
                Cell cell = cells.stream().filter(c -> c.getPositionY() == finalY && c.getPositionX() == finalX).findFirst().orElseThrow();
                printedWalls.addAll(cell.printCellASCIIMiddle(printedWalls));
            }
            System.out.println();
            for (int x = 0; x < width; x++) {
                int finalX = x;
                int finalY = y;
                Cell cell = cells.stream().filter(c -> c.getPositionY() == finalY && c.getPositionX() == finalX).findFirst().orElseThrow();
                printedWalls.add(cell.printCellASCIIBottom(printedWalls));
            }
        }
        System.out.println();
    }
}
