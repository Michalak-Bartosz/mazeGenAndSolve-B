package wat.bartoszmichalak.mazegenandsolve.dto;

import wat.bartoszmichalak.mazegenandsolve.algorithmHelper.GenerateAlgorithmType;

public class CreateMazeDto {
    private int height;
    private int width;
    private GenerateAlgorithmType algorithmType;

    public CreateMazeDto() { }

    public CreateMazeDto(int height, int width, GenerateAlgorithmType algorithmType) {
        this.height = height;
        this.width = width;
        this.algorithmType = algorithmType;
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public GenerateAlgorithmType getAlgorithmType() {
        return algorithmType;
    }
}
