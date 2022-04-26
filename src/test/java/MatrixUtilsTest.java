import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import utils.MatrixUtils;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class MatrixUtilsTest {
    @Test
    public void multiplyTest(){
        float[][] a = new float[][]{{1, -1, 2},{0,-3,1}};
        float[] b = new float[]{2,1,0};
        MatrixUtils.multiply(a,b);
        System.out.println(Arrays.deepToString(a));
    }
}
