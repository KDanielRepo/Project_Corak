package neuralnetwork;

public class ConvolutionOutput {
    private float pc; //prawdopodobienstwo ze cos jest w danym zakresie, ze ktorakolwiek z klas ponizej jest na obszarze
    private int bx; //wartosc x srodka tego bounding Boxa
    private int by; // wartosc y srodka
    private int bw; // szerokosc boudning boxa
    private int bh; // wysokosc bb
    private float classes; // tutaj zamiast jednego parametru bedzie ich X, dla kazdego mozliwego obiektu bedzie kolejna klasa
}
