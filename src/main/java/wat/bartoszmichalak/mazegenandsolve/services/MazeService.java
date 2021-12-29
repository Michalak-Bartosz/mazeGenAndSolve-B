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
import wat.bartoszmichalak.mazegenandsolve.exceptions.MazeCellNotFoundException;
import wat.bartoszmichalak.mazegenandsolve.exceptions.MazeNotFoundException;
import wat.bartoszmichalak.mazegenandsolve.exceptions.SolveNotFoundException;
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
    public MazeDto generateMaze(GenerateMazeDto generateMazeDto) throws MazeCellNotFoundException {

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

    @Transactional
    public SolvedMazeDto solveMaze(SolveParamsDto solveParamsDto) throws MazeNotFoundException, MazeCellNotFoundException {
        Long mazeId = solveParamsDto.getMazeId();
        SolveAlgorithmType solveAlgorithmType = solveParamsDto.getSolveAlgorithmType();

        Maze maze = mazeRepository.findById(mazeId)
                .orElseThrow(() -> new MazeNotFoundException(mazeId));
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

    private Cell extractCell(Long cellId) throws MazeCellNotFoundException {
        return ObjectUtils.isEmpty(cellId) ? null : cellRepository.findById(cellId)
                .orElseThrow(() -> new MazeCellNotFoundException(cellId));
    }

    public List<MazeDto> getAllMazes() {
        return mazeRepository.findAll().stream().map(MazeDto::new)
                .collect(Collectors.toList());
    }

    public MazeDto getMaze(Long mazeId) throws MazeNotFoundException {
        Maze maze = mazeRepository.findById(mazeId)
                .orElseThrow(() -> new MazeNotFoundException(mazeId));
        return new MazeDto(maze);
    }

    public void deleteMaze(Long mazeId) throws MazeNotFoundException {
        mazeRepository.findById(mazeId)
                .orElseThrow(() -> new MazeNotFoundException(mazeId));
        mazeRepository.deleteById(mazeId);
    }

    public List<CellDto> getMazeCells(Long mazeId) throws MazeNotFoundException {
        Maze maze = mazeRepository.findById(mazeId)
                .orElseThrow(() -> new MazeNotFoundException(mazeId));
        return maze.getCells().stream().map(CellDto::new).collect(Collectors.toList());
    }

    public List<CellDto> getSolveMazeCells(Long mazeId, Long solveId) throws MazeNotFoundException, SolveNotFoundException {
        Maze maze = mazeRepository.findById(mazeId)
                .orElseThrow(() -> new MazeNotFoundException(mazeId));
        SolvedMaze solvedMaze = maze.getSolvedMazes().stream()
                .filter(sMaze -> sMaze.getSolveId().equals(solveId))
                .findFirst()
                .orElseThrow(() -> new SolveNotFoundException(solveId));
        return solvedMaze.getAlgorithmSteps().stream()
                .map(CellDto::new)
                .collect(Collectors.toList());
    }

    public SolvedMazeDto getSolveMaze(Long solveId) throws SolveNotFoundException {
        return solvedMazeRepository.findById(solveId)
                .map(SolvedMazeDto::new)
                .orElseThrow(() -> new SolveNotFoundException(solveId));
    }

    public List<SolvedMazeDto> getAllSolveMazes(Long mazeId) throws MazeNotFoundException {
        Maze maze = mazeRepository.findById(mazeId)
                .orElseThrow(() -> new MazeNotFoundException(mazeId));
        return maze.getSolvedMazes().stream()
                .map(SolvedMazeDto::new)
                .collect(Collectors.toList());
    }

    public void deleteSolve(Long solveId) throws SolveNotFoundException {
        solvedMazeRepository.findById(solveId)
                        .orElseThrow(() -> new SolveNotFoundException(solveId));
        solvedMazeRepository.deleteById(solveId);
    }
}
