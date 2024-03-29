package wat.bartoszmichalak.mazegenandsolve.exceptions;

public class MazeNotFoundException extends Exception {
    private String message = "";

    public MazeNotFoundException(Long mazeId) {
        this.message = "Maze: " + mazeId + " doesn't exist.";
    }

    @Override
    public String getMessage() {
        if (message.trim().isEmpty()) {
            this.message = "Maze doesn't exist.";
        }
        return message;
    }
}
