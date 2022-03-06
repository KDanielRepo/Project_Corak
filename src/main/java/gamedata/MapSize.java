package gamedata;

public enum MapSize {
    S(36),
    M(72),
    L(108),
    XL(144);

    private int size;

    MapSize(int size){
        this.size = size;
    }
}
