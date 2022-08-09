import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import utils.MatrixUtils;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class MatrixUtilsTest {
    @Test
    public void multiplyTest(){
        double[][] a = new double[][]{{1, -1, 2},{0,-3,1}};
        double[] b = new double[]{2,1,0};
        MatrixUtils.multiply(a,b);
        System.out.println(Arrays.deepToString(a));
    }
}
