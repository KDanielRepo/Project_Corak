package neuralnetwork;

import lombok.Data;

@Data
public class CoracsBrain {
    private ConvolutionalNeuralNet convolutionalNeuralNet;
    private NeuralNet neuralNet;

    public CoracsBrain(){
        //convolutionalNeuralNet = new ConvolutionalNeuralNet();
        //neuralNet = new NeuralNet();
        //neuralNet = new NeuralNet();
        //convolutionalNeuralNet.setFullyConnectedNLayer(neuralNet);
    }
}
