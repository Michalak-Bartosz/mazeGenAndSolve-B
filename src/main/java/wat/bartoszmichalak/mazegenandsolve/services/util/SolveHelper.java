package wat.bartoszmichalak.mazegenandsolve.services.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@UtilityClass
public class SolveHelper {

    private static final Random rand = new Random();

    public static List<Cell> solveByDijkstra(List<Cell> cells, int width, int height, Cell start, Cell end, StopWatch stopWatch) {
        List<Cell> algorithmSteps = new Stack<>();
        Cell startCell = getStartCell(start, cells);
        Cell endCell = getEndCell(end, cells, startCell, width, height);
        Cell currentCell;

        System.out.println("START: " + startCell.getCellIndex());
        System.out.println("END: " + endCell.getCellIndex());

        HashMap<Integer, Integer> distanceFromStartCell = initDistanceFromStartCell(startCell, cells);
        HashMap<Integer, Integer> orderedDistanceFromStartCell = sortByDistance(distanceFromStartCell);
        HashMap<Integer, Integer> cellsValue = initCellsValue(cells, startCell);
        HashMap<Integer, LinkedList<Cell>> pathToCell = initPathToCell(cells);

        stopWatch.start();
        for (Map.Entry<Integer, Integer> distanceEntry : orderedDistanceFromStartCell.entrySet()) {
            currentCell = cells.get(distanceEntry.getKey());
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
        stopWatch.stop();

        for (Cell cell : pathToCell.get(endCell.getCellIndex())) {
            algorithmSteps.add(cell);
            cell.setCellState(CellState.VISITED);
        }
        algorithmSteps.add(endCell);
        startCell.setCellState(CellState.START);
        endCell.setCellState(CellState.END);
        printAlgorithmStepsCells(algorithmSteps);

        resetCellStatus(cells);
        return algorithmSteps;
    }

    public static Stack<Cell> solveByAstar(List<Cell> cells, int width, int height, Cell start, Cell end, StopWatch stopWatch) {
        Stack<Cell> algorithmSteps = new Stack<>();
        Cell startCell = getStartCell(start, cells);
        Cell endCell = getEndCell(end, cells, startCell, width, height);

        System.out.println("START: " + startCell.getCellIndex());
        System.out.println("END: " + endCell.getCellIndex());

        HashMap<Integer, Integer> distanceBetweenCells = initDistanceBetweenCells(cells);
        HashMap<Integer, Double> cellsScore = initCellsScore(cells, endCell);
        HashMap<Integer, Double> cellsValue = initCellsValueByDistanceAndScore(cells, distanceBetweenCells, cellsScore);
        Cell currentCell = startCell;
        algorithmSteps.push(startCell);
        List<Cell> unvisitedNeighbourCells = new ArrayList<>();

        stopWatch.start();
        while (!currentCell.equals(endCell)) {
            currentCell.setCellState(CellState.VISITED);
            unvisitedNeighbourCells.addAll(getConnectedUnvisitedNeighbourCells(currentCell));
            currentCell = getMinValueCell(unvisitedNeighbourCells, cellsValue);
            algorithmSteps.push(currentCell);
            unvisitedNeighbourCells.remove(currentCell);
        }
        stopWatch.stop();

        startCell.setCellState(CellState.START);
        endCell.setCellState(CellState.END);
        printAlgorithmStepsCells(algorithmSteps);

        resetCellStatus(cells);
        return algorithmSteps;
    }

    public static List<Cell> solveByBFS(List<Cell> cells, int width, int height, Cell start, Cell end, StopWatch stopWatch) {
        List<Cell> algorithmSteps = new Stack<>();
        LinkedList<Cell> linkedList = new LinkedList<>();
        Cell startCell = getStartCell(start, cells);
        Cell endCell = getEndCell(end, cells, startCell, width, height);

        System.out.println("START: " + startCell.getCellIndex());
        System.out.println("END: " + endCell.getCellIndex());

        linkedList.add(startCell);
        addUniqueCells(linkedList, getConnectedUnvisitedNeighbourCells(linkedList.getFirst()));
        algorithmSteps.add(linkedList.getFirst());

        stopWatch.start();
        while (!linkedList.getFirst().equals(endCell)) {
            linkedList.getFirst().setCellState(CellState.VISITED);
            linkedList.removeFirst();
            addUniqueCells(linkedList, getConnectedUnvisitedNeighbourCells(linkedList.getFirst()));
            algorithmSteps.add(linkedList.getFirst());
        }
        stopWatch.stop();

        startCell.setCellState(CellState.START);
        endCell.setCellState(CellState.END);
        printAlgorithmStepsCells(algorithmSteps);

        resetCellStatus(cells);
        return algorithmSteps;
    }

    public static Stack<Cell> solveByDFS(List<Cell> cells, int width, int height, Cell start, Cell end, StopWatch stopWatch) {
        Stack<Cell> algorithmSteps = new Stack<>();
        Stack<Cell> stack = new Stack<>();
        Cell startCell = getStartCell(start, cells);
        Cell endCell = getEndCell(end, cells, startCell, width, height);

        System.out.println("START: " + startCell.getCellIndex());
        System.out.println("END: " + endCell.getCellIndex());

        stack.push(startCell);
        algorithmSteps.push(startCell);

        stopWatch.start();
        while (!stack.peek().equals(endCell)) {
            Cell currentCell = stack.peek();
            currentCell.setCellState(CellState.VISITED);
            List<Cell> connectedUnvisitedNeighbourCells = getConnectedUnvisitedNeighbourCells(currentCell);
            if (!connectedUnvisitedNeighbourCells.isEmpty()) {
                stack.push(connectedUnvisitedNeighbourCells.get(0));
                algorithmSteps.push(connectedUnvisitedNeighbourCells.get(0));
            } else {
                stack.pop();
            }
        }
        stopWatch.stop();

        startCell.setCellState(CellState.START);
        endCell.setCellState(CellState.END);
        printAlgorithmStepsCells(algorithmSteps);

        resetCellStatus(cells);
        return algorithmSteps;
    }

    //TODO add throw exception
    private static Cell getStartCell(Cell start, List<Cell> cellList) {
        List<Cell> cells = cellList.stream().filter(c -> c.getPositionX() == 0 || c.getPositionY() == 0).collect(Collectors.toList());
        return ObjectUtils.isEmpty(start) ? cells.get(rand.nextInt(cells.size())) : start;
    }

    private static Cell getEndCell(Cell end, List<Cell> cellList, Cell startCell, int width, int height) {
        List<Cell> cells = cellList.stream().filter(c -> (c.getPositionX() == width - 1 || c.getPositionY() == height - 1) && !c.equals(startCell)).collect(Collectors.toList());
        return ObjectUtils.isEmpty(end) ? cells.get(rand.nextInt(cells.size())) : end;
    }

    private static List<Cell> getConnectedNeighbourCells(Cell baseCell) {
        List<Cell> cellList = new ArrayList<>();
        for (Cell cell : baseCell.getNeighbourCells()) {
            if (!baseCell.getSeparatingWall(cell).getIsVisible())
                cellList.add(cell);
        }
        return cellList;
    }

    private static List<Cell> getConnectedUnvisitedNeighbourCells(Cell baseCell) {
        List<Cell> cellList = new ArrayList<>();
        for (Cell cell : baseCell.getUnvisitedNeighbourCells()) {
            if (!baseCell.getSeparatingWall(cell).getIsVisible())
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

    private static HashMap<Integer, Integer> initDistanceFromStartCell(Cell startCell, List<Cell> cells) {
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
        resetCellStatus(cells);
        return distanceFromStartCell;
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

    private static HashMap<Integer, Double> initCellsScore(List<Cell> cells, Cell endCell) {
        HashMap<Integer, Double> cellsScore = new HashMap<>();
        cellsScore.put(endCell.getCellIndex(), (double) 0);
        double x1 = endCell.getPositionX();
        double y1 = endCell.getPositionY();
        for (Cell cell : cells) {
            double x2 = cell.getPositionX();
            double y2 = cell.getPositionY();
            if (!cell.equals(endCell)) {
                double distanceBetweenEndCell = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
                cellsScore.put(cell.getCellIndex(), distanceBetweenEndCell);
            }
        }
        return cellsScore;
    }

    //Print algorithm steps
    private static void printAlgorithmStepsCells(List<Cell> algorithmSteps) {
        System.out.println("\nAlgorithm Steps (Cell indexes): ");
        for (Cell cell : algorithmSteps) {
            System.out.print(cell.getCellIndex() + " -> ");
        }
        System.out.println();
    }

    public static void resetCellStatus(List<Cell> cells) {
        for (Cell cell : cells) {
            cell.setCellState(CellState.UNVISITED);
        }
    }
}
