package wat.bartoszmichalak.mazegenandsolve.entities;

import com.sun.istack.NotNull;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.Direction;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "cell")
public class Cell {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "maze_id")
    private Maze maze;

    @NotNull
    private int cellIndex;

    @NotNull
    private int positionX;

    @NotNull
    private int positionY;

    @NotNull
    private CellState cellState;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Map<Direction, Wall> walls;

    @ManyToMany
    private List<Cell> neighbourCells;

    public Cell(Maze maze, int cellIndex, int positionX, int positionY, HashMap<Direction, Wall> walls) {
        this.maze = maze;
        this.cellIndex = cellIndex;
        this.positionX = positionX;
        this.positionY = positionY;
        this.cellState = CellState.UNVISITED;
        this.walls = walls;
        setCellAsNeighbourWalls();
        this.neighbourCells = new ArrayList<>();
    }

    public Cell() {
    }

    private void setCellAsNeighbourWalls() {
        for (Direction direction : Direction.values()) {
            walls.get(direction).addNeighbourMazeCell(this);
        }
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public boolean isVisited() {
        return this.cellState.equals(CellState.VISITED);
    }
    public List<Cell> getUnvisitedNeighbourCells() {
        return this.neighbourCells.stream().filter(c -> c.getCellState() == CellState.UNVISITED).collect(Collectors.toList());
    }

    //TODO throw exception
    public Wall getSeparatingWall(Cell cell) {
        return this.walls.values().stream().filter(w -> w.hasCellAsNeighbour(cell)).findFirst().orElse(null);
    }

    public void addNeighbourCells(List<Cell> neighbourCells) {
        this.neighbourCells = neighbourCells;
    }

    public Long getId() {
        return id;
    }

    public Maze getMaze() {
        return this.maze;
    }

    public int getCellIndex() {
        return this.cellIndex;
    }

    public int getPositionX() {
        return this.positionX;
    }

    public int getPositionY() {
        return this.positionY;
    }

    public CellState getCellState() {
        return this.cellState;
    }

    public Map<Direction, Wall> getWalls() {
        return walls;
    }

    public List<Cell> getNeighbourCells() {
        return this.neighbourCells;
    }

    public void printNeighboursCells() {
        System.out.println("Neighbour cells for cell; x: " + positionX + " | y: " + positionY + " | id: " + id + " | index: " + cellIndex);
        for (Cell cell : neighbourCells) {
            System.out.println("x: " + cell.getPositionX() + " | y: " + cell.getPositionY() + " | id: " + cell.getId() + " | index: " + cell.getCellIndex());
        }
    }

    public void printNeighboursWalls() {
        System.out.println("Neighbour walls for cell; x: " + positionX + " | y: " + positionY + " | id: " + id + " | index: " + cellIndex);
        for (Wall wall : walls.values()) {
            System.out.println("is visible: " + wall.getVisible() + " | id: " + " | id: " + wall.getId() + " | index: " + wall.getWallIndex());
        }
    }

    public Wall printCellASCIITop(List<Wall> printedWalls) {
        String cellString = "";
        Wall usedWall = new Wall();

        if (!printedWalls.contains(walls.get(Direction.TOP))) {
            if (walls.get(Direction.TOP).getVisible()) {
                cellString += " --- ";
            } else {
                cellString += "     ";
            }
            usedWall = walls.get(Direction.TOP);
        }
        System.out.print(cellString);
        return usedWall;
    }

    public List<Wall> printCellASCIIMiddle(List<Wall> printedWalls) {
        String cellString = "";
        List<Wall> usedWalls = new ArrayList<>();

        String stateMarker = " ";
        if (this.cellState == CellState.START){
            stateMarker = "S";
        } else if (this.cellState == CellState.END) {
            stateMarker = "E";
        } else if (this.cellState == CellState.VISITED) {
            stateMarker = "X";
        }

        if (!printedWalls.contains(walls.get(Direction.LEFT)) && !printedWalls.contains(walls.get(Direction.RIGHT))) {
            if (walls.get(Direction.LEFT).getVisible() && walls.get(Direction.RIGHT).getVisible()) {
                cellString += "| " + stateMarker + " |";
            } else if (walls.get(Direction.LEFT).getVisible()) {
                cellString += "| " + stateMarker + "  ";
            } else if (walls.get(Direction.RIGHT).getVisible()) {
                cellString += "  " + stateMarker + "  |";
            } else {
                cellString += "  " + stateMarker + "   ";
            }
            usedWalls.add(walls.get(Direction.LEFT));
            usedWalls.add(walls.get(Direction.RIGHT));
        } else if (printedWalls.contains(walls.get(Direction.LEFT)) && !printedWalls.contains(walls.get(Direction.RIGHT))) {
            if (walls.get(Direction.LEFT).getVisible() && walls.get(Direction.RIGHT).getVisible()) {
                cellString += "  " + stateMarker + " |";
            } else if (walls.get(Direction.LEFT).getVisible()) {
                cellString += "  " + stateMarker + "  ";
            } else if (walls.get(Direction.RIGHT).getVisible()) {
                cellString += "  " + stateMarker + " |";
            } else {
                cellString += "  " + stateMarker + "  ";
            }
            usedWalls.add(walls.get(Direction.RIGHT));
        } else if (!printedWalls.contains(walls.get(Direction.LEFT)) && printedWalls.contains(walls.get(Direction.RIGHT))) {
            if (walls.get(Direction.LEFT).getVisible() && walls.get(Direction.RIGHT).getVisible()) {
                cellString += "| " + stateMarker + "  ";
            } else if (walls.get(Direction.LEFT).getVisible()) {
                cellString += "| " + stateMarker + "  ";
            } else if (walls.get(Direction.RIGHT).getVisible()) {
                cellString += "  " + stateMarker + "  ";
            } else {
                cellString += "  " + stateMarker + "  ";
            }
            usedWalls.add(walls.get(Direction.LEFT));
        } else {
            cellString += stateMarker + "  ";
        }
        System.out.print(cellString);
        return usedWalls;
    }

    public Wall printCellASCIIBottom(List<Wall> printedWalls) {
        String cellString = "";
        Wall usedWall = new Wall();

        if (!printedWalls.contains(walls.get(Direction.BOTTOM))) {
            if (walls.get(Direction.BOTTOM).getVisible()) {
                cellString += " --- ";
            } else {
                cellString += "     ";
            }
            usedWall = walls.get(Direction.BOTTOM);
        }
        System.out.print(cellString);
        return usedWall;
    }
}
