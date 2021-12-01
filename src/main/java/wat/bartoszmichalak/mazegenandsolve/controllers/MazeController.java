package wat.bartoszmichalak.mazegenandsolve.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wat.bartoszmichalak.mazegenandsolve.dto.CreateMazeDto;
import wat.bartoszmichalak.mazegenandsolve.dto.MazeDto;
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
    public ResponseEntity<MazeDto> getMaze(@PathVariable Long mazeId) {
        return ResponseEntity.ok(mazeService.getMaze(mazeId));
    }

    @PostMapping ("/maze")
    ResponseEntity<MazeDto> createMaze(@RequestBody CreateMazeDto createMazeDto) {
        return ResponseEntity.ok(mazeService.createMaze(createMazeDto));
    }
}
