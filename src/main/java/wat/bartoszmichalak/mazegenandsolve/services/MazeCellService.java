package wat.bartoszmichalak.mazegenandsolve.services;

import org.springframework.stereotype.Service;
import wat.bartoszmichalak.mazegenandsolve.repositories.CellRepository;

@Service
public class MazeCellService {

    private final CellRepository cellRepository;

    public MazeCellService(CellRepository cellRepository) {
        this.cellRepository = cellRepository;
    }


//    public ArrayList<MazeCell> getNeighbours(MazeCell mazeCell) {
//        ArrayList<MazeCell> neighboursCells = new ArrayList<>();
//        Maze maze = mazeCell.getMaze();
//        ArrayList<MazeCell> mazeCells = maze.getMazeCellsList();
//
//        for (MazeCell cell: mazeCells) {
//            if(mazeCell.getPositionXY(). == 0 && cell.getRowId() == 1) {
//                neighboursCells.add(cell);
//            } else if(mazeCell.getRowId() == maze.getWight() && cell.getRowId() == maze.getWight() - 1) {
//                neighboursCells.add(cell);
//            } else if(cell.getRowId() == mazeCell.getRowId() - 1 || cell.getRowId() == mazeCell.getRowId() + 1) {
//                neighboursCells.add(cell);
//            }
//
//            if(mazeCell.getColumnId() == 0) {
//
//            } else if(mazeCell.getColumnId() == maze.getWight()) {
//
//            } else {
//
//            }
//        }
//
//        return neighboursCells;
//    }
}
