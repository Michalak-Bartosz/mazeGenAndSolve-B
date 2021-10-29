package wat.bartoszmichalak.mazegenandsolve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.SolveAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.entities.Cell;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;
import wat.bartoszmichalak.mazegenandsolve.entities.Wall;
import wat.bartoszmichalak.mazegenandsolve.repositories.CellRepository;
import wat.bartoszmichalak.mazegenandsolve.repositories.WallRepository;
import wat.bartoszmichalak.mazegenandsolve.repositories.MazeRepository;
import wat.bartoszmichalak.mazegenandsolve.services.GenerateService;
import wat.bartoszmichalak.mazegenandsolve.services.SolveService;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MazeGenAndSolveApplication {

    @Autowired
    MazeRepository mazeRepository;

    @Autowired
    CellRepository cellRepository;

    @Autowired
    WallRepository wallRepository;

    public static void main(String[] args) {
        SpringApplication.run(MazeGenAndSolveApplication.class, args);
    }

    @Bean
    public void configureDB() {
        List<Maze> mazeList = new ArrayList<>();
        for (GenerateAlgorithmType generateAlgorithmType : GenerateAlgorithmType.values()) {
            mazeList.add(new Maze(10, 10, generateAlgorithmType));
        }
        Maze maze = mazeList.get(0);

        System.out.println(maze.getId());
        System.out.println(maze.getAlgorithmType());
        System.out.println(maze.getWidth());
        System.out.println(maze.getHeight());
        mazeRepository.save(maze);
        cellRepository.saveAll(maze.getCells());
        wallRepository.saveAll(maze.getWalls());

        Cell checkCell1 = maze.getCells().get(8);
        checkCell1.printNeighboursCells();

        Cell checkCell2 = maze.getCells().get(11);
        checkCell2.printNeighboursCells();

        System.out.println("\nCheck numbers of walls in maze: " + maze.getWalls().size() + "\n");

        Wall checkWall1 = maze.getWalls().get(0);
        checkWall1.printNeighbourMazeCells();

        Wall checkWall2 = maze.getWalls().get(21);
        checkWall2.printNeighbourMazeCells();

        Wall checkWall3 = maze.getWalls().get(27);
        checkWall3.printNeighbourMazeCells();

        Wall checkWall4 = maze.getWalls().get(38);
        checkWall4.printNeighbourMazeCells();

        Cell checkWallForCell1 = maze.getCells().get(12);
        checkWallForCell1.printNeighboursWalls();

        int currentMaze = 0;
        for (GenerateAlgorithmType generateAlgorithmType : GenerateAlgorithmType.values()) {
            System.out.println("\nGenerate maze by: " + generateAlgorithmType.name());
            long startNano = System.nanoTime();
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
            long finishNano = System.nanoTime();
            mazeList.get(currentMaze).printMazeASCII();

            long timeElapsedNano = finishNano - startNano;
            long timeElapsedMillis = Math.round((double) timeElapsedNano / 1_000_000);
            long timeElapsedSek = Math.round((double) timeElapsedMillis / 1_000);
            long timeElapsedMin = Math.round((double) timeElapsedMillis / 60_000);
            long timeElapsedHour = Math.round((double) timeElapsedSek / 3_600);
            System.out.printf("\nTime difference: \n1) %d ns\n2) %d ms\n3) %d s\n4) %d min\n5) %d h\n", timeElapsedNano, timeElapsedMillis, timeElapsedSek, timeElapsedMin, timeElapsedHour);
            currentMaze++;
        }

        currentMaze = 0;
        for (SolveAlgorithmType solveAlgorithmType : SolveAlgorithmType.values()) {
            System.out.println("\nSolve maze by: " + solveAlgorithmType.name());
            long startNano = System.nanoTime();
            switch (solveAlgorithmType) {
                case Dijkstra:
                    SolveService.solveByDijkstra(mazeList.get(currentMaze));
                    break;
                case Astar:
                    SolveService.solveByAstar(mazeList.get(currentMaze));
                    break;
                case BFS:
                    SolveService.solveByBFS(mazeList.get(currentMaze));
                    break;
                case DFS:
                    SolveService.solveByDFS(mazeList.get(currentMaze));
                    break;
                default:
                    break;
            }
            long finishNano = System.nanoTime();
            mazeList.get(currentMaze).printMazeASCII();

            long timeElapsedNano = finishNano - startNano;
            long timeElapsedMillis = Math.round((double) timeElapsedNano / 1_000_000);
            long timeElapsedSek = Math.round((double) timeElapsedMillis / 1_000);
            long timeElapsedMin = Math.round((double) timeElapsedMillis / 60_000);
            long timeElapsedHour = Math.round((double) timeElapsedSek / 3_600);
            System.out.printf("\nTime difference: \n1) %d ns\n2) %d ms\n3) %d s\n4) %d min\n5) %d h\n", timeElapsedNano, timeElapsedMillis, timeElapsedSek, timeElapsedMin, timeElapsedHour);
            currentMaze++;
        }
    }
}
