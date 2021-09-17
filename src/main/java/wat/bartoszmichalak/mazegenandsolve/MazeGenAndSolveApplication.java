package wat.bartoszmichalak.mazegenandsolve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;
import wat.bartoszmichalak.mazegenandsolve.entities.Maze;
import wat.bartoszmichalak.mazegenandsolve.repositories.MazeCellRepository;
import wat.bartoszmichalak.mazegenandsolve.repositories.MazeCellWallRepository;
import wat.bartoszmichalak.mazegenandsolve.repositories.MazeRepository;

@SpringBootApplication
public class MazeGenAndSolveApplication {

    @Autowired
    MazeRepository mazeRepository;

    @Autowired
    MazeCellRepository mazeCellRepository;

    @Autowired
    MazeCellWallRepository mazeCellWallRepository;

    public static void main(String[] args) {
        SpringApplication.run(MazeGenAndSolveApplication.class, args);
    }

    @Bean
    public void configureDB(){
        Maze maze = new Maze(10, 10, GenerateAlgorithmType.RandomDFS);
        System.out.println(maze.getId());
        System.out.println(maze.getAlgorithmType());
        System.out.println(maze.getWidth());
        System.out.println(maze.getHeight());
        System.out.println(maze.getMazeCells().toString());
        mazeRepository.save(maze);
        mazeCellRepository.saveAll(maze.getMazeCells());
        maze.getMazeCells().forEach(mazeCell -> mazeCellWallRepository.saveAll(mazeCell.getWalls()));
    }

}
