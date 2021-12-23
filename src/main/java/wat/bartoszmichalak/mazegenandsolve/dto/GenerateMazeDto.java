package wat.bartoszmichalak.mazegenandsolve.dto;

import lombok.Getter;
import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;

@Getter
public class GenerateMazeDto {
    private int height;
    private int width;
    private GenerateAlgorithmType algorithmType;

    public GenerateMazeDto() { }

    public GenerateMazeDto(int height, int width, GenerateAlgorithmType algorithmType) {
        this.height = height;
        this.width = width;
        this.algorithmType = algorithmType;
    }
}
