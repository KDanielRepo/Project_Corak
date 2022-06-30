package ui;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainingDataCoordinates {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    @Override
    public String toString() {
        return "startX=" + startX +
                ", startY=" + startY +
                ", endX=" + endX +
                ", endY=" + endY;
    }
}
