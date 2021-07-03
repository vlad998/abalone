package abalone;

public class Position {

    int i;
    int j;

    Position(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        Position p = (Position) o;
        return (p.i == this.i && p.j == this.j);
    }

    @Override
    public int hashCode() {
        int res = 7;
        res = 13 * res + this.i;
        res = 13 * res + this.j;
        return res;
    }
}
