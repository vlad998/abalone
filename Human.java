package abalone;

public class Human extends Strategy {

    @Override
    public boolean isHuman() {
        return true;
    }

    @Override
    public String move() {
        return "H";
    }
}
