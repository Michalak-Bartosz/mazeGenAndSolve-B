package wat.bartoszmichalak.mazegenandsolve.exceptions;

public class MazeCellNotFountException extends Exception {
    private static final String message = "Not found cells for maze.";

    public MazeCellNotFountException() {
        super(message);
    }
}
