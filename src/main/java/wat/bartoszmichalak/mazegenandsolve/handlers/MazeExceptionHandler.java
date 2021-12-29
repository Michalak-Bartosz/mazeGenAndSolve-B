package wat.bartoszmichalak.mazegenandsolve.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wat.bartoszmichalak.mazegenandsolve.exceptions.MazeCellNotFoundException;
import wat.bartoszmichalak.mazegenandsolve.exceptions.SolveNotFoundException;
import wat.bartoszmichalak.mazegenandsolve.exceptions.MazeNotFoundException;

@ControllerAdvice
public class MazeExceptionHandler {

    @ExceptionHandler(value = MazeNotFoundException.class)
    public ResponseEntity<Object> handleMazeNotFoundException(MazeNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = SolveNotFoundException.class)
    public ResponseEntity<Object> handleSolveNotFoundException(SolveNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MazeCellNotFoundException.class)
    public ResponseEntity<Object> handleMazeCellNotFoundException(MazeCellNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
