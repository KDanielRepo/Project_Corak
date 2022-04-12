package neuralnetwork;

import lombok.Data;

@Data
public class Dataset {
    private String label;
    private float[][] imageValues;
    private float[] answers;
}
