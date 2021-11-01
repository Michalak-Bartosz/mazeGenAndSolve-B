package wat.bartoszmichalak.mazegenandsolve.services;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;
import wat.bartoszmichalak.mazegenandsolve.entities.Wall;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class SolveService {

    private static final Random rand = new Random();

    public static void solveByDijkstra(Maze maze) {
        Cell startCell = getRandomStartCell(maze.getCells());
        Cell endCell = getRandomEndCell(maze.getCells(), startCell, maze.getWidth(), maze.getHeight());

        System.out.println("START: " + startCell.getCellIndex());
        System.out.println("END: " + endCell.getCellIndex());

        HashMap<Integer, Integer> distanceFromStartCell = initDistanceFromStartCell(startCell, maze);
        HashMap<Integer, Integer> orderedDistanceFromStartCell = sortByDistance(distanceFromStartCell);
        HashMap<Integer, Integer> cellsValue = initCellsValue(maze.getCells(), startCell);
        HashMap<Integer, LinkedList<Cell>> pathToCell = initPathToCell(maze.getCells());

        Cell currentCell;
        for (Map.Entry<Integer, Integer> distanceEntry : orderedDistanceFromStartCell.entrySet()) {
            currentCell = maze.getCells().get(distanceEntry.getKey());
            List<Cell> neighbourCells = getConnectedNeighbourCells(currentCell);
            for (Cell cell : neighbourCells) {
                Integer newCellValue = distanceFromStartCell.get(cell.getCellIndex()) + distanceFromStartCell.get(currentCell.getCellIndex());
                if (newCellValue < cellsValue.get(cell.getCellIndex())) {
                    pathToCell.get(cell.getCellIndex()).addAll(pathToCell.get(currentCell.getCellIndex()));
                    pathToCell.get(cell.getCellIndex()).add(currentCell);
                    cellsValue.replace(cell.getCellIndex(), newCellValue);
                }
            }
        }

        Stack<Cell> algorithmSteps = new Stack<>();
        for (Cell cell : pathToCell.get(endCell.getCellIndex())) {
            algorithmSteps.push(cell);
            cell.setCellState(CellState.VISITED);
        }
        algorithmSteps.push(endCell);
        startCell.setCellState(CellState.START);
        endCell.setCellState(CellState.END);
        printAlgorithmStepsCells(algorithmSteps);
    }

    public static void solveByAstar(Maze maze) {
        Stack<Cell> algorithmSteps = new Stack<>();
        Cell startCell = getRandomStartCell(maze.getCells());
        Cell endCell = getRandomEndCell(maze.getCells(), startCell, maze.getWidth(), maze.getHeight());

        System.out.println("START: " + startCell.getCellIndex());
        System.out.println("END: " + endCell.getCellIndex());

        HashMap<Integer, Integer> distanceBetweenCells = initDistanceBetweenCells(maze.getCells());
        HashMap<Integer, Double> cellsScore = initCellsScore(maze, endCell);
        HashMap<Integer, Double> cellsValue = initCellsValueByDistanceAndScore(maze.getCells(), distanceBetweenCells, cellsScore);
        Cell currentCell = startCell;
        algorithmSteps.push(startCell);
        List<Cell> unvisitedNeighbourCells = new ArrayList<>();
        while (!currentCell.equals(endCell)) {
            currentCell.setCellState(CellState.VISITED);
            unvisitedNeighbourCells.addAll(getConnectedUnvisitedNeighbourCells(currentCell));
            currentCell = getMinValueCell(unvisitedNeighbourCells, cellsValue);
            algorithmSteps.push(currentCell);
            unvisitedNeighbourCells.remove(currentCell);
        }
        startCell.setCellState(CellState.START);
        endCell.setCellState(CellState.END);
        printAlgorithmStepsCells(algorithmSteps);
    }

    private static HashMap<Integer, Integer> initDistanceBetweenCells(List<Cell> cellList) {
        HashMap<Integer, Integer> distanceBetweenCells = new HashMap<>();
        for (Cell cell : cellList) {
            distanceBetweenCells.put(cell.getCellIndex(), 1);
        }
        return distanceBetweenCells;
    }

    private static Cell getMinValueCell(List<Cell> cellList, HashMap<Integer, Double> cellsValue) {
        Cell minValueCell = null;
        for (Cell cell : cellList) {
            if (minValueCell == null || cellsValue.get(cell.getCellIndex()) < cellsValue.get(minValueCell.getCellIndex()))
                minValueCell = cell;
        }
        return minValueCell;
    }

    private static HashMap<Integer, Double> initCellsValueByDistanceAndScore(List<Cell> cellList, HashMap<Integer, Integer> distanceBetweenCells, HashMap<Integer, Double> cellsScore) {
        HashMap<Integer, Double> cellsValue = new HashMap<>();
        for (Cell cell : cellList) {
            cellsValue.put(cell.getCellIndex(), distanceBetweenCells.get(cell.getCellIndex()) + cellsScore.get(cell.getCellIndex()));
        }
        return cellsValue;
    }

    private static HashMap<Integer, Double> initCellsScore(Maze maze, Cell endCell) {
        HashMap<Integer, Double> cellsScore = new HashMap<>();
        cellsScore.put(endCell.getCellIndex(), (double) 0);
        double x1 = endCell.getPositionX();
        double y1 = endCell.getPositionY();
        for (Cell cell : maze.getCells()) {
            double x2 = cell.getPositionX();
            double y2 = cell.getPositionY();
            if (!cell.equals(endCell)) {
                double distanceBetweenEndCell = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
                cellsScore.put(cell.getCellIndex(), distanceBetweenEndCell);
            }
        }
        return cellsScore;
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
            stack.peek().setCellState(CellState.VISITED);
            if (!stack.peek().getUnvisitedNeighbourCells().isEmpty()) {
                List<Cell> connectedUnvisitedNeighbourCells = getConnectedUnvisitedNeighbourCells(stack.peek());
                if (!connectedUnvisitedNeighbourCells.isEmpty()) {
                    stack.push(connectedUnvisitedNeighbourCells.get(0));
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

    private static HashMap<Integer, LinkedList<Cell>> initPathToCell(List<Cell> cellList) {
        HashMap<Integer, LinkedList<Cell>> pathToCell = new HashMap<>();
        for (Cell cell : cellList) {
            pathToCell.put(cell.getCellIndex(), new LinkedList<>());
        }
        return pathToCell;
    }

    private static HashMap<Integer, Integer> initCellsValue(List<Cell> cellList, Cell startCell) {
        HashMap<Integer, Integer> cellsValue = new HashMap<>();
        cellsValue.put(startCell.getCellIndex(), 0);
        for (Cell cell : cellList) {
            if (!cell.equals(startCell))
                cellsValue.put(cell.getCellIndex(), Integer.MAX_VALUE);
        }
        return cellsValue;
    }

    private static HashMap<Integer, Integer> sortByDistance(HashMap<Integer, Integer> map) {
        List<Map.Entry<Integer, Integer>> list
                = new LinkedList<>(
                map.entrySet());

        list.sort(Entry.comparingByValue());

        HashMap<Integer, Integer> temp
                = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : list) {
            temp.put(entry.getKey(), entry.getValue());
        }
        return temp;
    }

    private static HashMap<Integer, Integer> initDistanceFromStartCell(Cell startCell, Maze maze) {
        HashMap<Integer, Integer> distanceFromStartCell = new HashMap<>();
        distanceFromStartCell.put(startCell.getCellIndex(), 0);
        startCell.setCellState(CellState.VISITED);
        List<Cell> cellsToSetDistance = getConnectedUnvisitedNeighbourCells(startCell);
        int actualDistance = 1;
        while (!cellsToSetDistance.isEmpty()) {
            List<Cell> neighbourCells = new ArrayList<>();
            for (Cell cell : cellsToSetDistance) {
                distanceFromStartCell.put(cell.getCellIndex(), actualDistance);
                cell.setCellState(CellState.VISITED);
                neighbourCells.addAll(getConnectedUnvisitedNeighbourCells(cell));
            }
            cellsToSetDistance = new ArrayList<>(neighbourCells);
            actualDistance++;
        }
        maze.resetCellStatus();
        return distanceFromStartCell;
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
