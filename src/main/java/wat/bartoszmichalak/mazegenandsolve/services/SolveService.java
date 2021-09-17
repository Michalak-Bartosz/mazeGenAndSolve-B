package wat.bartoszmichalak.mazegenandsolve.services;

import org.springframework.stereotype.Service;
import wat.bartoszmichalak.mazegenandsolve.entities.MazeCell;

import java.util.ArrayList;
import java.util.List;

@Service
public class SolveService {

    public List<Integer> solveByDijkstra(List<MazeCell> mazeCells) {
        return new ArrayList<>();
    }

    public List<Integer> solveByAstar(List<MazeCell> mazeCells) {
        return new ArrayList<>();
    }

    public List<Integer> solveByBFS(List<MazeCell> mazeCells) {
        return new ArrayList<>();
    }

    public List<Integer> solveByDFS(List<MazeCell> mazeCells) {
        return new ArrayList<>();
    }
}
