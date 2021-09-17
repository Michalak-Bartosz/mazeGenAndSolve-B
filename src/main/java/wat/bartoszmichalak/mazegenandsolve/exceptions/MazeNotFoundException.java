package wat.bartoszmichalak.mazegenandsolve.exceptions;

public class MazeNotFoundException extends Exception{
    private static final String message = "Not found maze.";

    public MazeNotFoundException() {
        super(message);
    }
}
