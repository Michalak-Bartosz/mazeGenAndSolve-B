package wat.bartoszmichalak.mazegenandsolve.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.SolveAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.dto.CreateMazeDto;
import wat.bartoszmichalak.mazegenandsolve.dto.MazeDto;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;
import wat.bartoszmichalak.mazegenandsolve.repositories.CellRepository;
import wat.bartoszmichalak.mazegenandsolve.repositories.MazeRepository;
import wat.bartoszmichalak.mazegenandsolve.repositories.WallRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MazeService {

    private final MazeRepository mazeRepository;
    private final CellRepository cellRepository;
    private final WallRepository wallRepository;
    private final MazeCellService mazeCellService;
    private final SolveService solveService;

    public MazeService(MazeRepository mazeRepository,
                       CellRepository cellRepository,
                       WallRepository wallRepository,
                       MazeCellService mazeCellService,
                       SolveService solveService) {
        this.mazeRepository = mazeRepository;
        this.cellRepository = cellRepository;
        this.wallRepository = wallRepository;
        this.mazeCellService = mazeCellService;
        this.solveService = solveService;
    }

    @Transactional
    public MazeDto generateMaze(CreateMazeDto createMazeDto) {
        int width = createMazeDto.getWidth();
        int height = createMazeDto.getHeight();
        GenerateAlgorithmType generateAlgorithmType = createMazeDto.getAlgorithmType();

        Maze maze = new Maze(height, width, generateAlgorithmType);

        switch (generateAlgorithmType) {
            case RandomDFS:
                GenerateService.generateByRandomDFS(maze);
                break;
            case RandomKruskal:
                GenerateService.generateByRandomKruskal(maze);
                break;
            case RandomPrim:
                GenerateService.generateByRandomPrim(maze);
                break;
            case AldousBroder:
                GenerateService.generateByAldousBroder(maze);
                break;
            default:
                break;
        }

        mazeRepository.save(maze);
        cellRepository.saveAll(maze.getCells());
        wallRepository.saveAll(maze.getWalls());
        //TODO add exception
        return new MazeDto(maze);
    }

    //TODO add exception
    public void solveMaze(Long mazeId, SolveAlgorithmType solveAlgorithmType) {
        Maze maze = mazeRepository.findById(mazeId).orElseThrow();

        switch (solveAlgorithmType) {
            case Dijkstra:
                solveService.solveByDijkstra(maze);
                break;
            case Astar:
                solveService.solveByAstar(maze);
                break;
            case BFS:
                solveService.solveByBFS(maze);
                break;
            case DFS:
                solveService.solveByDFS(maze);
                break;
            default:
                break;
        }
    }

    public List<MazeDto> getAllMazes() {
        return mazeRepository.findAll().stream().map(maze ->
                        new MazeDto(maze.getMazeId(), maze.getHeight(), maze.getWidth(),
                                maze.getAlgorithmType(), maze.getCells()))
                .collect(Collectors.toList());
    }

    public MazeDto getMaze(Long mazeId) {
        //TODO add exception
        Maze maze = mazeRepository.findById(mazeId).orElseThrow();
        return new MazeDto(maze);
    }
}
