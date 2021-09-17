package wat.bartoszmichalak.mazegenandsolve.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.SolveAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.dto.CreateMazeDto;
import wat.bartoszmichalak.mazegenandsolve.dto.MazeDto;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;
import wat.bartoszmichalak.mazegenandsolve.entities.MazeCell;
import wat.bartoszmichalak.mazegenandsolve.repositories.MazeCellRepository;
import wat.bartoszmichalak.mazegenandsolve.repositories.MazeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MazeService {

    private final MazeRepository mazeRepository;
    private final MazeCellRepository mazeCellRepository;
    private final MazeCellService mazeCellService;
    private final GenerateService generateService;
    private final SolveService solveService;

    public MazeService(MazeRepository mazeRepository, MazeCellRepository mazeCellRepository, MazeCellService mazeCellService, GenerateService generateService, SolveService solveService) {
        this.mazeRepository = mazeRepository;
        this.mazeCellRepository = mazeCellRepository;
        this.mazeCellService = mazeCellService;
        this.generateService = generateService;
        this.solveService = solveService;
    }

    @Transactional
    public MazeDto generateMaze(CreateMazeDto createMazeDto) {
        int width = createMazeDto.getWidth();
        int height = createMazeDto.getHeight();
        GenerateAlgorithmType generateAlgorithmType = createMazeDto.getAlgorithmType();

        Maze maze = new Maze(height, width, generateAlgorithmType);
        List<MazeCell> mazeCells = maze.getMazeCells();

        switch (generateAlgorithmType) {
            case RandomDFS:
                generateService.generateByRandomDFS(mazeCells);
                break;
            case RandomKruskal:
                generateService.generateByRandomKruskal(mazeCells);
                break;
            case RandomPrim:
                generateService.generateByRandomPrim(mazeCells);
                break;
            case AldousBroder:
                generateService.generateByAldousBroder(mazeCells);
                break;
            default:
                break;
        }

        mazeRepository.save(maze);
        mazeCellRepository.saveAll(mazeCells);
        //TODO add exception
        return new MazeDto(maze);
    }

    public List<Integer> solveMaze(MazeDto mazeDto, SolveAlgorithmType solveAlgorithmType) {
        List<MazeCell> mazeCells = mazeDto.getMazeCellsList();
        List<Integer> stepsList = new ArrayList<>();

        switch (solveAlgorithmType) {
            case Dijkstra:
                stepsList = solveService.solveByDijkstra(mazeCells);
                break;
            case Astar:
                stepsList = solveService.solveByAstar(mazeCells);
                break;
            case BFS:
                stepsList = solveService.solveByBFS(mazeCells);
                break;
            case DFS:
                stepsList = solveService.solveByDFS(mazeCells);
                break;
            default:
                break;
        }
        return stepsList;
    }

    public List<MazeDto> getAllMazes() {
        return mazeRepository.findAll().stream().map(maze ->
                new MazeDto(maze.getId(), maze.getHeight(), maze.getWidth(),
                        maze.getAlgorithmType(), maze.getMazeCells()))
                .collect(Collectors.toList());
    }

    public MazeDto getMaze(Long mazeId) {
        //TODO add exception
        Maze maze = mazeRepository.findById(mazeId).orElseThrow();
        return new MazeDto(maze);
    }
}
