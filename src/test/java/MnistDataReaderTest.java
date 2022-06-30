import mnistreader.MnistDataReader;
import mnistreader.MnistMatrix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class MnistDataReaderTest {
    @Test
    public void readTest() throws IOException {
        MnistMatrix[] mnistMatrix = MnistDataReader.readData("C:\\Users\\Daniel\\Downloads\\train-images.idx3-ubyte", "C:\\Users\\Daniel\\Downloads\\train-labels.idx1-ubyte");
        printMnistMatrix(mnistMatrix[mnistMatrix.length - 1]);
        mnistMatrix = MnistDataReader.readData("C:\\Users\\Daniel\\Downloads\\t10k-images.idx3-ubyte", "C:\\Users\\Daniel\\Downloads\\t10k-labels.idx1-ubyte");
        printMnistMatrix(mnistMatrix[0]);
    }

    private static void printMnistMatrix(final MnistMatrix matrix) {
        System.out.println("label: " + matrix.getLabel());
        for (int r = 0; r < matrix.getNumberOfRows(); r++ ) {
            for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                System.out.print(matrix.getValue(r, c) + " ");
            }
            System.out.println();
        }
    }
}
