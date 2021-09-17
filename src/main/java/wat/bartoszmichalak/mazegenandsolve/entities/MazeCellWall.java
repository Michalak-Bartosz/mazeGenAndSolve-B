package wat.bartoszmichalak.mazegenandsolve.entities;

import com.sun.istack.NotNull;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.Direction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class MazeCellWall {

    @Id
    @GeneratedValue
    Long id;

    @NotNull
    Boolean isVisible;

    @NotNull
    Direction direction;

    @NotNull
    @ManyToMany
    List<MazeCell> neighbourMazeCells = new ArrayList<>(2);

    public MazeCellWall(Boolean isVisible, Direction direction) {
        this.isVisible = isVisible;
        this.direction = direction;
    }

    public MazeCellWall() {
    }

    public void addNeighbourMazeCell(MazeCell mazeCell) {
        neighbourMazeCells.add(mazeCell);
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }
}
