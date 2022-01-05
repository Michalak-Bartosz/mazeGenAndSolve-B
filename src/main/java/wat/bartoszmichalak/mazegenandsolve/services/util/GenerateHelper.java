package wat.bartoszmichalak.mazegenandsolve.services.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.CellState;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;
import wat.bartoszmichalak.mazegenandsolve.entities.Wall;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class GenerateHelper {

    private static final Random rand = new Random();
    Stack<Cell> algorithmCellSteps;

    public static void generateByRandomDFS(Maze maze, StopWatch stopWatch) {
        Stack<Cell> stack = new Stack<>();
        algorithmCellSteps = new Stack<>();

        stopWatch.start();
        Cell currentCell = getRandomCell(maze.getCells());

        stack.push(currentCell);
        algorithmCellSteps.push(currentCell);

        while (!stack.isEmpty()) {
            currentCell = stack.pop();
            currentCell.setCellState(CellState.VISITED);
            List<Cell> neighbourCells = currentCell.getUnvisitedNeighbourCells();
            if (!CollectionUtils.isEmpty(neighbourCells)) {
                stack.push(currentCell);
                Cell chosenNeighbourCell = getRandomCell(neighbourCells);
                Wall separatingWall = currentCell.getSeparatingWall(chosenNeighbourCell);
                separatingWall.setIsVisible(false);
                stack.push(chosenNeighbourCell);
                algorithmCellSteps.push(chosenNeighbourCell);
            }
        }
        stopWatch.stop();

        resetCellStatus(maze.getCells());
    }

    public static void generateByRandomKruskal(Maze maze, StopWatch stopWatch) {
        algorithmCellSteps = new Stack<>();
        int startInsideWallIndex = 2 * (maze.getWidth() + maze.getHeight());
        int endInsideWallIndex = maze.getWalls().size() - 1;
        List<Wall> wallList = maze.getWalls()
                .subList(startInsideWallIndex, endInsideWallIndex);

        Set<Set<Integer>> mergedCellSet = new HashSet<>();
        for (Cell cell : maze.getCells()) {
            Set<Integer> cellSet = Collections.singleton(cell.getCellIndex());
            mergedCellSet.add(cellSet);
        }

        stopWatch.start();
        while (!isAllCellsMerged(mergedCellSet)) {
            Wall chosenWall = getRandomWall(wallList);
            List<Integer> neighbourCellIndexes = chosenWall.getNeighbourCells().stream()
                    .map(Cell::getCellIndex)
                    .collect(Collectors.toList());
            if (!isCellsAreInSameSet(mergedCellSet, neighbourCellIndexes)) {
                chosenWall.setIsVisible(false);
                mergeTwoCellIndexSets(mergedCellSet, neighbourCellIndexes);
                chosenWall.getNeighbourCells()
                        .forEach(cell -> algorithmCellSteps.push(cell));
                wallList.remove(chosenWall);
            }
        }
        stopWatch.stop();

        resetCellStatus(maze.getCells());
    }

    public static void generateByRandomPrim(Maze maze, StopWatch stopWatch) {
        algorithmCellSteps = new Stack<>();

        Cell currentCell = getRandomCell(maze.getCells());
        algorithmCellSteps.push(currentCell);
        currentCell.setCellState(CellState.VISITED);

        List<Wall> wallList = new ArrayList<>(currentCell.getWalls().values());

        stopWatch.start();
        while (maze.hasUnvisitedCells()) {
            Wall chosenWall = getRandomWall(wallList);
            if (!CollectionUtils.isEmpty(chosenWall.getUnvisitedNeighbourCells())) {
                List<Cell> unvisitedNeighbourCells = chosenWall.getUnvisitedNeighbourCells();
                unvisitedNeighbourCells.forEach(cell -> {
                    cell.setCellState(CellState.VISITED);
                    wallList.addAll(cell.getWalls().values());
                    algorithmCellSteps.push(cell);
                });
                chosenWall.setIsVisible(false);
            }
            wallList.remove(chosenWall);
        }
        stopWatch.stop();

        resetCellStatus(maze.getCells());
    }

    public static void generateByAldousBroder(Maze maze, StopWatch stopWatch) {
        algorithmCellSteps = new Stack<>();

        Cell currentCell = getRandomCell(maze.getCells());
        currentCell.setCellState(CellState.VISITED);

        stopWatch.start();
        while (maze.hasUnvisitedCells()) {
            Cell chosenNeighbourCell = getRandomCell(currentCell.getNeighbourCells());
            if (!chosenNeighbourCell.isVisited()) {
                Wall separatingWall = currentCell.getSeparatingWall(chosenNeighbourCell);
                separatingWall.setIsVisible(false);
                chosenNeighbourCell.setCellState(CellState.VISITED);
                algorithmCellSteps.push(chosenNeighbourCell);
            }
            currentCell = chosenNeighbourCell;
        }
        stopWatch.stop();

        resetCellStatus(maze.getCells());
    }

    //Help method

    private static Cell getRandomCell(List<Cell> cellList) {
        return CollectionUtils.isEmpty(cellList) ? null : cellList.get(rand.nextInt(cellList.size()));
    }

    private static Wall getRandomWall(List<Wall> wallList) {
        return CollectionUtils.isEmpty(wallList) ? null : wallList.get(rand.nextInt(wallList.size()));
    }

    //Kruskal
    private static boolean isCellsAreInSameSet(Set<Set<Integer>> mergedCellSet, List<Integer> searchedCells) {
        for (Set<Integer> cellSet : mergedCellSet) {
            if (cellSet.containsAll(searchedCells))
                return true;
        }
        return false;
    }

    private static void mergeTwoCellIndexSets(Set<Set<Integer>> mergedCellSet, List<Integer> searchedCells) {
        Set<Integer> setIncludeCellIndex1 = new HashSet<>();
        Set<Integer> setIncludeCellIndex2 = new HashSet<>();
        for (Set<Integer> cellSet : mergedCellSet) {
            if (cellSet.contains(searchedCells.get(0)))
                setIncludeCellIndex1 = cellSet;
            else if (cellSet.contains(searchedCells.get(1)))
                setIncludeCellIndex2 = cellSet;
            else if (setIncludeCellIndex1.size() != 0 && setIncludeCellIndex2.size() != 0)
                break;
        }
        mergedCellSet.remove(setIncludeCellIndex1);
        mergedCellSet.remove(setIncludeCellIndex2);
        mergedCellSet.add(Stream.concat(setIncludeCellIndex1.stream(), setIncludeCellIndex2.stream()).collect(Collectors.toSet()));
    }

    private static boolean isAllCellsMerged(Set<Set<Integer>> mergedCellSet) {
        return mergedCellSet.size() == 1;
    }

    private static void resetCellStatus(List<Cell> cells) {
        for (Cell cell : cells) {
            cell.setCellState(CellState.UNVISITED);
        }
    }
}
