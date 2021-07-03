package abalone;

public class Field {

    public Marble mrb;

    public Field() {
        mrb = Marble.OUT;
    }

    public Field(Marble mrb) {
        this.mrb = mrb;
    }

    public Field(Field f) {
        this.mrb = f.mrb;
    }
}
