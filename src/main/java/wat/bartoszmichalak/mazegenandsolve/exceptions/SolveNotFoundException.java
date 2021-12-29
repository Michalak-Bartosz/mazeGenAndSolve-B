package wat.bartoszmichalak.mazegenandsolve.exceptions;

public class SolveNotFoundException extends Exception {
    private String message = "";

    public SolveNotFoundException(Long solveId) {
        this.message = "Maze: " + solveId + " doesn't exist.";
    }

    @Override
    public String getMessage() {
        if (message.trim().isEmpty()) {
            this.message = "Solve doesn't exist.";
        }
        return message;
    }
}
