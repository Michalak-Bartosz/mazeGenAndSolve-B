package wat.bartoszmichalak.mazegenandsolve.exceptions;

public class MazeCellNotFoundException extends Exception {
    private String message = "";

    public MazeCellNotFoundException(Long cellId) {
        this.message = "Cell: " + cellId + " doesn't exist.";
    }

    @Override
    public String getMessage() {
        if (message.trim().isEmpty()) {
            this.message = "Cell doesn't exist.";
        }
        return message;
    }
}
