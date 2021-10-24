package wat.bartoszmichalak.mazegenandsolve.services;

import org.springframework.stereotype.Service;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;
import wat.bartoszmichalak.mazegenandsolve.entities.Wall;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SolveService {

    private static final Random rand = new Random();

    public static void solveByDijkstra(Maze maze) {

    }

    public static void solveByAstar(Maze maze) {

    }

    public static void solveByBFS(Maze maze) {
        Stack<Cell> algorithmSteps = new Stack<>();
        LinkedList<Cell> linkedList = new LinkedList<>();
        Cell startCell = getRandomStartCell(maze.getCells());
        Cell endCell = getRandomEndCell(maze.getCells(), startCell, maze.getWidth(), maze.getHeight());

        System.out.println("START: " + startCell.getCellIndex());
        System.out.println("END: " + endCell.getCellIndex());

        linkedList.add(startCell);
        addUniqueCells(linkedList, getConnectedNeighbourCells(linkedList.getFirst()));
        algorithmSteps.push(linkedList.getFirst());
        while (!linkedList.getFirst().equals(endCell)) {
            linkedList.getFirst().setCellState(CellState.VISITED);
            linkedList.removeFirst();
            addUniqueCells(linkedList, getConnectedNeighbourCells(linkedList.getFirst()));
            algorithmSteps.push(linkedList.getFirst());
        }

        startCell.setCellState(CellState.START);
        endCell.setCellState(CellState.END);
        printAlgorithmStepsCells(algorithmSteps);
    }

    public static void solveByDFS(Maze maze) {
        Stack<Cell> algorithmSteps = new Stack<>();
        Stack<Cell> stack = new Stack<>();
        Cell startCell = getRandomStartCell(maze.getCells());
        Cell endCell = getRandomEndCell(maze.getCells(), startCell, maze.getWidth(), maze.getHeight());

        System.out.println("START: " + startCell.getCellIndex());
        System.out.println("END: " + endCell.getCellIndex());

        stack.push(startCell);
        algorithmSteps.push(startCell);
        while (!stack.peek().equals(endCell)) {
            if (!stack.peek().getUnvisitedNeighbourCells().isEmpty()) {
                List<Cell> connectedUnvisitedNeighbourCells = getConnectedUnvisitedNeighbourCells(stack.peek());
                if (!connectedUnvisitedNeighbourCells.isEmpty()) {
                    stack.push(connectedUnvisitedNeighbourCells.get(0));
                    stack.peek().setCellState(CellState.VISITED);
                } else {
                    stack.pop();
                }
            } else {
                stack.pop();
            }
            algorithmSteps.push(stack.peek());
        }

        startCell.setCellState(CellState.START);
        endCell.setCellState(CellState.END);
        printAlgorithmStepsCells(algorithmSteps);
    }

    //TODO add throw exception
    private static Cell getRandomStartCell(List<Cell> cellList) {
        List<Cell> cells = cellList.stream().filter(c -> c.getPositionX() == 0 || c.getPositionY() == 0).collect(Collectors.toList());
        return cells.get(rand.nextInt(cells.size()));
    }

    private static Cell getRandomEndCell(List<Cell> cellList, Cell startCell, int width, int height) {
        List<Cell> cells = cellList.stream().filter(c -> (c.getPositionX() == width - 1 || c.getPositionY() == height - 1) && !c.equals(startCell)).collect(Collectors.toList());
        return cells.get(rand.nextInt(cells.size()));
    }

    private static List<Cell> getConnectedNeighbourCells(Cell baseCell) {
        List<Cell> cellList = new ArrayList<>();
        for (Cell cell : baseCell.getNeighbourCells()) {
            if (!baseCell.getSeparatingWall(cell).getVisible())
                cellList.add(cell);
        }
        return cellList;
    }

    private static List<Cell> getConnectedUnvisitedNeighbourCells(Cell baseCell) {
        List<Cell> cellList = new ArrayList<>();
        for (Cell cell : baseCell.getUnvisitedNeighbourCells()) {
            if (!baseCell.getSeparatingWall(cell).getVisible())
                cellList.add(cell);
        }
        return cellList;
    }

    private static void addUniqueCells(LinkedList<Cell> baseCellsList, List<Cell> inputCellsList) {
        for (Cell cell : inputCellsList) {
            if (!baseCellsList.contains(cell))
                baseCellsList.add(cell);
        }
    }

    //Print algorithm steps
    private static void printAlgorithmStepsCells(Stack<Cell> algorithmSteps) {
        System.out.println("\nAlgorithm Steps (Cell indexes): ");
        for (Cell cell : algorithmSteps) {
            System.out.print(cell.getCellIndex() + " -> ");
        }
        System.out.println();
    }

    private static void printAlgorithmStepsWalls(Stack<Wall> algorithmSteps) {
        System.out.println("\nAlgorithm Steps (Wall indexes): ");
        for (Wall wall : algorithmSteps) {
            System.out.print(wall.getWallIndex() + " -> ");
        }
        System.out.println();
    }
}
