package neuralnetwork;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import neuralnetwork.enums.ActivationFunctionType;
import utils.NeuralNetworkUtils;

@AllArgsConstructor
@NoArgsConstructor
public class CalculationThread implements Runnable{
    private int startIndex;
    private int endIndex;
    private double[][][] weights;
    private double[][] outputs;
    private double[][] bias;
    private int y;
    private ActivationFunctionType outputActivationFunctionType;


    @Override
    public void run() {
        for (int k = startIndex; k < endIndex; k+=1) {
            double sum = 0f;
            for (int j = 0; j < weights[y][k].length; j++) {
                sum += weights[y][k][j] * outputs[y][j];
                if (j == weights[y][k].length - 1) {
                    if (y == outputs.length - 2 && ActivationFunctionType.SOFTMAX == outputActivationFunctionType) {
                        outputs[y + 1][k] = NeuralNetworkUtils.softmax(((sum / weights[y].length) + bias[y][k]), outputs[y + 1]);
                    }
                    outputs[y + 1][k] = NeuralNetworkUtils.sigmoid((sum / weights[y].length) + bias[y][k]);
                }
            }
            //System.out.println(Thread.currentThread().getName() + ": " + k);
        }
    }
}
