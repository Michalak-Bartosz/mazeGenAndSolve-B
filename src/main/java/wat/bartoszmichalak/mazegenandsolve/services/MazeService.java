package wat.bartoszmichalak.mazegenandsolve.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.SolveAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.dto.*;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;
import wat.bartoszmichalak.mazegenandsolve.entities.SolvedMaze;
import wat.bartoszmichalak.mazegenandsolve.repositories.CellRepository;
import wat.bartoszmichalak.mazegenandsolve.repositories.MazeRepository;
import wat.bartoszmichalak.mazegenandsolve.repositories.SolvedMazeRepository;
import wat.bartoszmichalak.mazegenandsolve.repositories.WallRepository;
import wat.bartoszmichalak.mazegenandsolve.services.util.GenerateHelper;
import wat.bartoszmichalak.mazegenandsolve.services.util.SolveHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
public class MazeService {

    private final MazeRepository mazeRepository;
    private final CellRepository cellRepository;
    private final WallRepository wallRepository;
    private final SolvedMazeRepository solvedMazeRepository;

    public MazeService(MazeRepository mazeRepository,
                       CellRepository cellRepository,
                       WallRepository wallRepository,
                       SolvedMazeRepository solvedMazeRepository) {
        this.mazeRepository = mazeRepository;
        this.cellRepository = cellRepository;
        this.wallRepository = wallRepository;
        this.solvedMazeRepository = solvedMazeRepository;
    }

    @Transactional
    public MazeDto generateMaze(GenerateMazeDto generateMazeDto) {

        int width = generateMazeDto.getWidth();
        int height = generateMazeDto.getHeight();
        GenerateAlgorithmType generateAlgorithmType = generateMazeDto.getAlgorithmType();

        Maze maze = new Maze(height, width, generateAlgorithmType);

        StopWatch stopWatch = new StopWatch();
        switch (generateAlgorithmType) {
            case RandomDFS:
                GenerateHelper.generateByRandomDFS(maze, stopWatch);
                break;
            case RandomKruskal:
                GenerateHelper.generateByRandomKruskal(maze, stopWatch);
                break;
            case RandomPrim:
                GenerateHelper.generateByRandomPrim(maze, stopWatch);
                break;
            case AldousBroder:
                GenerateHelper.generateByAldousBroder(maze, stopWatch);
                break;
            default:
                break;
        }
        BigDecimal generateTime = BigDecimal.valueOf(stopWatch.getTotalTimeSeconds())
                .setScale(6, RoundingMode.HALF_EVEN);
        maze.setGenerateTime(Double.parseDouble(String.valueOf(generateTime)));

        mazeRepository.save(maze);
        cellRepository.saveAll(maze.getCells());
        wallRepository.saveAll(maze.getWalls());

        return new MazeDto(maze);
    }

    //TODO add exception
    public SolvedMazeDto solveMaze(SolveParamsDto solveParamsDto) {
        Long mazeId = solveParamsDto.getMazeId();
        SolveAlgorithmType solveAlgorithmType = solveParamsDto.getSolveAlgorithmType();

        Maze maze = mazeRepository.findById(mazeId).orElseThrow();
        List<Cell> cells = maze.getCells();
        List<Cell> algorithmSteps = new Stack<>();

        int width = maze.getWidth();
        int height = maze.getHeight();
        Cell startCell = extractCell(solveParamsDto.getStartCellId());
        Cell endCell = extractCell(solveParamsDto.getEndCellId());

        StopWatch stopWatch = new StopWatch();
        switch (solveAlgorithmType) {
            case Dijkstra:
                algorithmSteps = SolveHelper.solveByDijkstra(cells, width, height, startCell, endCell, stopWatch);
                break;
            case Astar:
                algorithmSteps = SolveHelper.solveByAstar(cells, width, height, startCell, endCell, stopWatch);
                break;
            case BFS:
                algorithmSteps = SolveHelper.solveByBFS(cells, width, height, startCell, endCell, stopWatch);
                break;
            case DFS:
                algorithmSteps = SolveHelper.solveByDFS(cells, width, height, startCell, endCell, stopWatch);
                break;
            default:
                break;
        }
        BigDecimal solveTimeFactor = BigDecimal.valueOf(stopWatch.getTotalTimeSeconds())
                .setScale(6, RoundingMode.HALF_EVEN);

        double solveTime = Double.parseDouble(String.valueOf(solveTimeFactor));

        SolvedMaze solvedMaze = new SolvedMaze(maze.getMazeId(),
                solveAlgorithmType,
                algorithmSteps,
                solveTime);

        solvedMazeRepository.save(solvedMaze);
        return new SolvedMazeDto(solvedMaze);
    }

    private Cell extractCell(Long cellId) {
        return ObjectUtils.isEmpty(cellId) ? null : cellRepository.findById(cellId).orElse(null);
    }

    public List<MazeDto> getAllMazes() {
        return mazeRepository.findAll().stream().map(MazeDto::new)
                .collect(Collectors.toList());
    }

    public MazeDto getMaze(Long mazeId) {
        Maze maze = mazeRepository.findById(mazeId).orElseThrow();
        return new MazeDto(maze);
    }

    public void deleteMaze(Long mazeId) {
        mazeRepository.deleteById(mazeId);
    }

    public List<CellDto> getMazeCells(Long mazeId) {
        Maze maze = mazeRepository.findById(mazeId).orElseThrow();
        return maze.getCells().stream().map(CellDto::new).collect(Collectors.toList());
    }

    public List<CellDto> getSolveMazeCells(Long mazeId, Long solveId) {
        Maze maze = mazeRepository.findById(mazeId).orElseThrow();
        SolvedMaze solvedMaze = maze.getSolvedMazes().stream()
                .filter(sMaze -> sMaze.getSolveId().equals(solveId))
                .findFirst()
                .orElseThrow();
        return solvedMaze.getAlgorithmSteps().stream()
                .map(CellDto::new)
                .collect(Collectors.toList());
    }

    public SolvedMazeDto getSolveMaze(Long solveId) {
        return solvedMazeRepository.findById(solveId)
                .map(SolvedMazeDto::new)
                .orElseThrow();
    }

    public List<SolvedMazeDto> getAllSolveMazes(Long mazeId) {
        Maze maze = mazeRepository.findById(mazeId).orElseThrow();
        return maze.getSolvedMazes().stream()
                .map(SolvedMazeDto::new)
                .collect(Collectors.toList());
    }

    public void deleteSolve(Long solveId) {
        solvedMazeRepository.deleteById(solveId);
    }
}
