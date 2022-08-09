package datareaders.mnistreader;

import java.util.Arrays;

public class MnistMatrix {

    private double [][] data;

    private int nRows;
    private int nCols;

    private int label;

    public MnistMatrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;

        data = new double[nRows][nCols];
    }

    public double[][] getData() {
        return data;
    }

    public double getValue(int r, int c) {
        return data[r][c];
    }

    public void setValue(int row, int col, double value) {
        data[row][col] = value;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getNumberOfRows() {
        return nRows;
    }

    public int getNumberOfColumns() {
        return nCols;
    }

    public double[] getAnswer(){
        double[] answers = new double[10];
        Arrays.fill(answers, 0d);
        answers[label] = 1d;
        return answers;
    }

}