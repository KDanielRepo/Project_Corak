import neuralnetwork.ConvolutionalNeuralNet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ConvolutionalNeuralNetTest {

    private float[][][] filter;

    @Test
    public void test(){
        byte[][] bytes = new byte[5][5];
        createFilter();

        System.out.println("przed: ");
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < bytes[i].length; j++) {
                bytes[i][j] = (byte) (i+j);
                System.out.print(bytes[i][j]+" ");
            }
            System.out.println();
        }
        ConvolutionalNeuralNet neuralNet = new ConvolutionalNeuralNet();
        neuralNet.setFilterValue(filter);
        float[][] a = neuralNet.convolution(bytes, 0, 1);
        System.out.println("______________________________________________");
        System.out.println("po: ");

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j]+" ");
            }
            System.out.println();
        }
    }

    private float[][][] createFilter(){
        filter = new float[1][3][3];
        for (int i = 0; i < filter.length; i++) {
            for (int j = 0; j < filter[i].length; j++) {
                for (int k = 0; k < filter[i][j].length; k++) {
                    filter[i][j][k] = 10;
                }
            }
        }
        return filter;
    }
}
