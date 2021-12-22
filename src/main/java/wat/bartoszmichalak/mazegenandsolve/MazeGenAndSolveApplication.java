package wat.bartoszmichalak.mazegenandsolve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.SolveAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.dto.MazeDto;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;
import wat.bartoszmichalak.mazegenandsolve.entities.Wall;
import wat.bartoszmichalak.mazegenandsolve.repositories.CellRepository;
import wat.bartoszmichalak.mazegenandsolve.repositories.WallRepository;
import wat.bartoszmichalak.mazegenandsolve.repositories.MazeRepository;
import wat.bartoszmichalak.mazegenandsolve.services.GenerateService;
import wat.bartoszmichalak.mazegenandsolve.services.MazeService;
import wat.bartoszmichalak.mazegenandsolve.services.SolveService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class MazeGenAndSolveApplication {

    @Autowired
    MazeRepository mazeRepository;

    @Autowired
    CellRepository cellRepository;

    @Autowired
    WallRepository wallRepository;

    @Autowired
    MazeService mazeService;

    public static void main(String[] args) {
        SpringApplication.run(MazeGenAndSolveApplication.class, args);
    }

    @Bean
    public void configureDB() {
        List<Maze> mazeList = new ArrayList<>();
        for (GenerateAlgorithmType generateAlgorithmType : GenerateAlgorithmType.values()) {
            mazeList.add(new Maze(4, 4, generateAlgorithmType));
        }
        Maze maze = mazeList.get(0);

        System.out.println(maze.getMazeId());
        System.out.println(maze.getAlgorithmType());
        System.out.println(maze.getWidth());
        System.out.println(maze.getHeight());

        Cell checkCell1 = maze.getCells().get(8);
        checkCell1.printNeighboursCells();

        Cell checkCell2 = maze.getCells().get(8);
        checkCell2.printNeighboursCells();

        System.out.println("\nCheck numbers of walls in maze: " + maze.getWalls().size() + "\n");

        Wall checkWall1 = maze.getWalls().get(0);
        checkWall1.printNeighbourMazeCells();

        Wall checkWall2 = maze.getWalls().get(14);
        checkWall2.printNeighbourMazeCells();

        Wall checkWall3 = maze.getWalls().get(20);
        checkWall3.printNeighbourMazeCells();

        Wall checkWall4 = maze.getWalls().get(12);
        checkWall4.printNeighbourMazeCells();

        Cell checkWallForCell1 = maze.getCells().get(8);
        checkWallForCell1.printNeighboursWalls();

        System.out.println("\nMaze schema:");
        maze.printMazeASCII();

        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(6);
        int currentMaze = 0;
        for (GenerateAlgorithmType generateAlgorithmType : GenerateAlgorithmType.values()) {
            System.out.println("\nGenerate maze by: " + generateAlgorithmType.name());
            float startNano = System.nanoTime();
            switch (generateAlgorithmType) {
                case RandomDFS:
                    GenerateService.generateByRandomDFS(mazeList.get(currentMaze));
                    break;
                case RandomKruskal:
                    GenerateService.generateByRandomKruskal(mazeList.get(currentMaze));
                    break;
                case RandomPrim:
                    GenerateService.generateByRandomPrim(mazeList.get(currentMaze));
                    break;
                case AldousBroder:
                    GenerateService.generateByAldousBroder(mazeList.get(currentMaze));
                    break;
                default:
                    break;
            }
            float finishNano = System.nanoTime();
            mazeList.get(currentMaze).printMazeASCII();

            float timeElapsedNano = finishNano - startNano;
            float timeElapsedMillis = timeElapsedNano / 1_000_000;
            float timeElapsedSek = timeElapsedMillis / 1_000;
            float timeElapsedMin = timeElapsedMillis / 60_000;
            float timeElapsedHour = timeElapsedSek / 3_600;
            System.out.println("\nTime difference: " +
                    "\n1) " + df.format(timeElapsedNano) + " ns" +
                    "\n2) " + df.format(timeElapsedMillis) + " ms" +
                    "\n3) " + df.format(timeElapsedSek) + " s" +
                    "\n4) " + df.format(timeElapsedMin) + " min" +
                    "\n5) " + df.format(timeElapsedHour) + " h\n");
            currentMaze++;
        }

        mazeRepository.save(maze);
        cellRepository.saveAll(maze.getCells());
        wallRepository.saveAll(maze.getWalls());

        currentMaze = 0;
        for (SolveAlgorithmType solveAlgorithmType : SolveAlgorithmType.values()) {
            System.out.println("\nSolve maze by: " + solveAlgorithmType.name());
            float startNano = System.nanoTime();
            Maze currentMazeTmp = mazeList.get(currentMaze);
            switch (solveAlgorithmType) {
                case Dijkstra:
                    SolveService.solveByDijkstra(currentMazeTmp.getCells(), currentMazeTmp.getWidth(), currentMazeTmp.getHeight(), null, null);
                    break;
                case Astar:
                    SolveService.solveByAstar(currentMazeTmp.getCells(), currentMazeTmp.getWidth(), currentMazeTmp.getHeight(), null, null);
                    break;
                case BFS:
                    SolveService.solveByBFS(currentMazeTmp.getCells(), currentMazeTmp.getWidth(), currentMazeTmp.getHeight(), null, null);
                    break;
                case DFS:
                    SolveService.solveByDFS(currentMazeTmp.getCells(), currentMazeTmp.getWidth(), currentMazeTmp.getHeight(), null, null);
                    break;
                default:
                    break;
            }
            float finishNano = System.nanoTime();
            mazeList.get(currentMaze).printMazeASCII();

            float timeElapsedNano = finishNano - startNano;
            float timeElapsedMillis = timeElapsedNano / 1_000_000;
            float timeElapsedSek = timeElapsedMillis / 1_000;
            float timeElapsedMin = timeElapsedMillis / 60_000;
            float timeElapsedHour = timeElapsedSek / 3_600;
            System.out.println("\nTime difference: " +
                    "\n1) " + df.format(timeElapsedNano) + " ns" +
                    "\n2) " + df.format(timeElapsedMillis) + " ms" +
                    "\n3) " + df.format(timeElapsedSek) + " s" +
                    "\n4) " + df.format(timeElapsedMin) + " min" +
                    "\n5) " + df.format(timeElapsedHour) + " h\n");
            currentMaze++;

            System.out.println(new ArrayList<>(mazeService.getAllMazes()));
        }
    }
}
