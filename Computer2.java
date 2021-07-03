package abalone;

public class Computer2 extends Strategy {

    public Computer2(int ix) {
        mp1 = Marble.BLUE;
        ma1 = Marble.RED;
        if (ix == 1) {
            mp1 = Marble.RED;
            ma1 = Marble.BLUE;
        }
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    protected int powerLeft(position pos) {
        Marble mrb = Board.getInstance().getField(pos.i, pos.j - 1).mrb;
        int pow = 0;
        if (mrb == ma1) {
            pow++;
            mrb = Board.getInstance().getField(pos.i, pos.j - 2).mrb;
            if (mrb == ma1) {
                pow++;
                mrb = Board.getInstance().getField(pos.i, pos.j - 3).mrb;
                if (mrb == ma1) {
                    pow++;
                } else if (mrb == Marble.EMPTY) {
                    return pow;
                } else if (mrb == Marble.OUT) {
                    return -pow;
                } else {
                    return Integer.MAX_VALUE;
                }
            } else if (mrb == Marble.EMPTY) {
                return pow;
            } else if (mrb == Marble.OUT) {
                return -pow;
            } else {
                return Integer.MAX_VALUE;
            }
        } else if (mrb == Marble.OUT) {
            return Integer.MAX_VALUE;
        }
        return pow;
    }

    @Override
    protected int powerRight(position pos) {
        Marble mrb = Board.getInstance().getField(pos.i, pos.j + 1).mrb;
        int pow = 0;
        if (mrb == ma1) {
            pow++;
            mrb = Board.getInstance().getField(pos.i, pos.j + 2).mrb;
            if (mrb == ma1) {
                pow++;
                mrb = Board.getInstance().getField(pos.i, pos.j + 3).mrb;
                if (mrb == ma1) {
                    pow++;
                } else if (mrb == Marble.EMPTY) {
                    return pow;
                } else if (mrb == Marble.OUT) {
                    return -pow;
                } else {
                    return Integer.MAX_VALUE;
                }
            } else if (mrb == Marble.EMPTY) {
                return pow;
            } else if (mrb == Marble.OUT) {
                return -pow;
            } else {
                return Integer.MAX_VALUE;
            }
        } else if (mrb == Marble.OUT) {
            return Integer.MAX_VALUE;
        }
        return pow;
    }

    @Override
    protected int powerDown(position pos) {
        Marble mrb = Board.getInstance().getField(pos.i - 1, pos.j).mrb;
        int pow = 0;
        if (mrb == ma1) {
            pow++;
            mrb = Board.getInstance().getField(pos.i - 2, pos.j).mrb;
            if (mrb == ma1) {
                pow++;
                mrb = Board.getInstance().getField(pos.i - 3, pos.j).mrb;
                if (mrb == ma1) {
                    pow++;
                } else if (mrb == Marble.EMPTY) {
                    return pow;
                } else if (mrb == Marble.OUT) {
                    return -pow;
                } else {
                    return Integer.MAX_VALUE;
                }
            } else if (mrb == Marble.EMPTY) {
                return pow;
            } else if (mrb == Marble.OUT) {
                return -pow;
            } else {
                return Integer.MAX_VALUE;
            }
        } else if (mrb == Marble.OUT) {
            return Integer.MAX_VALUE;
        }
        return pow;
    }

    @Override
    protected int powerUp(position pos) {
        Marble mrb = Board.getInstance().getField(pos.i + 1, pos.j).mrb;
        int pow = 0;
        if (mrb == ma1) {
            pow++;
            mrb = Board.getInstance().getField(pos.i + 2, pos.j).mrb;
            if (mrb == ma1) {
                pow++;
                mrb = Board.getInstance().getField(pos.i + 3, pos.j).mrb;
                if (mrb == ma1) {
                    pow++;
                } else if (mrb == Marble.EMPTY) {
                    return pow;
                } else if (mrb == Marble.OUT) {
                    return -pow;
                } else {
                    return Integer.MAX_VALUE;
                }
            } else if (mrb == Marble.EMPTY) {
                return pow;
            } else if (mrb == Marble.OUT) {
                return -pow;
            } else {
                return Integer.MAX_VALUE;
            }
        } else if (mrb == Marble.OUT) {
            return Integer.MAX_VALUE;
        }
        return pow;
    }

    @Override
    protected int powerDiagDown(position pos) {
        Marble mrb = Board.getInstance().getField(pos.i - 1, pos.j - 1).mrb;
        int pow = 0;
        if (mrb == ma1) {
            pow++;
            mrb = Board.getInstance().getField(pos.i - 2, pos.j - 2).mrb;
            if (mrb == ma1) {
                pow++;
                mrb = Board.getInstance().getField(pos.i - 3, pos.j - 3).mrb;
                if (mrb == ma1) {
                    pow++;
                } else if (mrb == Marble.EMPTY) {
                    return pow;
                } else if (mrb == Marble.OUT) {
                    return -pow;
                } else { // mrb == mp
                    return Integer.MAX_VALUE;
                }
            } else if (mrb == Marble.EMPTY) {
                return pow;
            } else if (mrb == Marble.OUT) {
                return -pow;
            } else { // mrb == mp
                return Integer.MAX_VALUE;
            }
        } else if (mrb == Marble.OUT) {
            return Integer.MAX_VALUE;
        }
        return pow;
    }

    @Override
    protected int powerDiagUp(position pos) {
        Marble mrb = Board.getInstance().getField(pos.i + 1, pos.j + 1).mrb;
        int pow = 0;
        if (mrb == ma1) {
            pow++;
            mrb = Board.getInstance().getField(pos.i + 2, pos.j + 2).mrb;
            if (mrb == ma1) {
                pow++;
                mrb = Board.getInstance().getField(pos.i + 3, pos.j + 3).mrb;
                if (mrb == ma1) {
                    pow++;
                } else if (mrb == Marble.EMPTY) {
                    return pow;
                } else if (mrb == Marble.OUT) {
                    return -pow;
                } else {
                    return Integer.MAX_VALUE;
                }
            } else if (mrb == Marble.EMPTY) {
                return pow;
            } else if (mrb == Marble.OUT) {
                return -pow;
            } else {
                return Integer.MAX_VALUE;
            }
        } else if (mrb == Marble.OUT) {
            return Integer.MAX_VALUE;
        }
        return pow;
    }
}
