import java.io.Serializable;

public class History implements Serializable {
    private int id;
    private int wins;
    private int losses;

    public History(int i, int w, int l){
        id = i;
        wins = w;
        losses = l;
    }


    public int getId() {
        return id;
    }

    public int getL() {
        return losses;
    }

    public int getW() {
        return wins;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setW(int w) {
        this.wins = w;
    }

    public void setL(int l) {
        this.losses = l;
    }
}
