package wat.bartoszmichalak.mazegenandsolve.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wat.bartoszmichalak.mazegenandsolve.dto.*;
import wat.bartoszmichalak.mazegenandsolve.exceptions.MazeCellNotFoundException;
import wat.bartoszmichalak.mazegenandsolve.exceptions.MazeNotFoundException;
import wat.bartoszmichalak.mazegenandsolve.exceptions.SolveNotFoundException;
import wat.bartoszmichalak.mazegenandsolve.services.MazeService;

import java.util.List;

@RestController
@RequestMapping(value = "/mazeGenSolve")
public class MazeController {

    private final MazeService mazeService;

    public MazeController(MazeService mazeService) {
        this.mazeService = mazeService;
    }

    @GetMapping("/mazes")
    public ResponseEntity<List<MazeDto>> getAllMazes() {
        return ResponseEntity.ok(mazeService.getAllMazes());
    }

    @GetMapping("/{mazeId}")
    public ResponseEntity<MazeDto> getMaze(@PathVariable Long mazeId) throws MazeNotFoundException {
        return ResponseEntity.ok(mazeService.getMaze(mazeId));
    }

    @GetMapping("/solve/{solveId}")
    public ResponseEntity<SolvedMazeDto> getSolveMaze(@PathVariable Long solveId) throws SolveNotFoundException {
        return ResponseEntity.ok(mazeService.getSolveMaze(solveId));
    }

    @GetMapping("/{mazeId}/solve")
    public ResponseEntity<List<SolvedMazeDto>> getAllSolveMazes(@PathVariable Long mazeId) throws MazeNotFoundException {
        return ResponseEntity.ok(mazeService.getAllSolveMazes(mazeId));
    }

    @GetMapping("/{mazeId}/cells")
    public ResponseEntity<List<CellDto>> getMazeCells(@PathVariable Long mazeId) throws MazeNotFoundException {
        return ResponseEntity.ok(mazeService.getMazeCells(mazeId));
    }

    @GetMapping("/{mazeId}/{solveId}")
    public ResponseEntity<List<CellDto>> getSolveMazeCells(@PathVariable Long mazeId, @PathVariable Long solveId) throws SolveNotFoundException, MazeNotFoundException {
        return ResponseEntity.ok(mazeService.getSolveMazeCells(mazeId, solveId));
    }

    @PostMapping("/create")
    ResponseEntity<MazeDto> createMaze(@RequestBody GenerateMazeDto generateMazeDto) throws MazeCellNotFoundException {
        return ResponseEntity.ok(mazeService.generateMaze(generateMazeDto));
    }

    @PostMapping("/solve")
    ResponseEntity<SolvedMazeDto> solveMaze(@RequestBody SolveParamsDto solveParamsDto) throws MazeNotFoundException, MazeCellNotFoundException {
        return ResponseEntity.ok(mazeService.solveMaze(solveParamsDto));
    }

    @DeleteMapping("/maze/{mazeId}")
    public ResponseEntity<Void> deleteMaze(@PathVariable Long mazeId) throws MazeNotFoundException {
        mazeService.deleteMaze(mazeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/solve/{solveId}")
    public ResponseEntity<Void> deleteSolve(@PathVariable Long solveId) throws SolveNotFoundException {
        mazeService.deleteSolve(solveId);
        return ResponseEntity.ok().build();
    }
}
