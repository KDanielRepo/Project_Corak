package mnistreader;

import java.util.Arrays;

public class MnistMatrix {

    private float [][] data;

    private int nRows;
    private int nCols;

    private int label;

    public MnistMatrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;

        data = new float[nRows][nCols];
    }

    public float[][] getData() {
        return data;
    }

    public float getValue(int r, int c) {
        return data[r][c];
    }

    public void setValue(int row, int col, float value) {
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

    public float[] getAnswer(){
        float[] answers = new float[10];
        Arrays.fill(answers, 0f);
        answers[label] = 1f;
        return answers;
    }

}