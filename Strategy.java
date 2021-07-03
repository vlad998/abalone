package abalone;

import java.util.Random;
import java.util.ArrayList;

public abstract class Strategy {

    protected static final int WIN_POINTS = 20;
    protected static final int MOVE_POINTS = 10;

    protected Marble mp1 = null, mp2 = null;
    protected Marble ma1 = null, ma2 = null;

    protected ArrayList<movedata> moves = new ArrayList<>();
    protected Random rnd = new Random();

    public abstract boolean isHuman();

    public String getStrMoves() {
        int i, j;
        String strmoves = "";
        for (i = Board.DIM_ROWS - 1; i >= 0; i--) {
            strmoves += (char) (97 + i) + " ";
            for (j = 0; j < Board.DIM_COLS; j++) {
                Field f = Board.getInstance().getField(i, j);
                switch (f.mrb) {
                    case EMPTY:
                        strmoves += "--";
                        break;
                    case BLUE:
                        strmoves += "B ";
                        break;
                    case RED:
                        strmoves += "R ";
                        break;
                    case GREEN:
                        strmoves += "G ";
                        break;
                    case ORANGE:
                        strmoves += "O ";
                        break;
                    case OUT:
                        strmoves += "##";
                        break;
                }
            }
            strmoves += "\n";
        }
        strmoves += "  1 2 3 4 5 6 7 8 9\n";
        strmoves += "\n\n";
        generateMoves();
        for (movedata md : moves) {
            strmoves += md.smove + " " + md.power + "|";
        }
        return strmoves;
    }

    protected class position {

        protected int i, j;

        protected position(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    protected class movedata {

        protected String smove;
        protected int power;
        protected double prob;

        protected movedata(String smove, int power) {
            this.smove = smove;
            this.power = power;
        }
    }

    protected void calculateProbs() {
        int i, sum = 0;
        for (i = 0; i < moves.size(); i++) {
            sum += moves.get(i).power;
        }
        int psum = 0;
        for (i = 0; i < moves.size(); i++) {
            movedata ms = moves.get(i);
            psum += ms.power;
            ms.prob = psum / ((double) sum + 1.0e-7);
        }
    }

    protected int findMoveIndex(double prob) {
        for (int i = 0; i < moves.size(); i++) {
            if (prob <= moves.get(i).prob) {
                return i;
            }
        }
        return moves.size() - 1;
    }

    protected String generateRandomMove() {
        calculateProbs();
        double pr = rnd.nextDouble();
        int ix = findMoveIndex(pr);
        movedata md = moves.get(ix);
        return md.smove;
    }

    protected int powerLeft(position pos) {
        return 0;
    }

    protected int powerRight(position pos) {
        return 0;
    }

    protected void MoveLeft1(position pos) {
        if (Board.getInstance().getField(pos.i, pos.j - 1).mrb == Marble.EMPTY) {
            String str = "M " + (char) ('A' + pos.i) + (pos.j + 1) + " "
                    + (char) ('A' + pos.i) + pos.j;
            moves.add(new movedata(str, 1));
        }
    }

    protected void MoveRight1(position pos) {
        if (Board.getInstance().getField(pos.i, pos.j + 1).mrb == Marble.EMPTY) {
            String str = "M " + (char) ('A' + pos.i) + (pos.j + 1) + " "
                    + (char) ('A' + pos.i) + (pos.j + 2);
            moves.add(new movedata(str, 1));
        }
    }

    protected void MoveLeft2(position pos1, position pos2) {
        int powa = powerLeft(pos1);
        if (Math.abs(powa) <= 1) {
            int points = 2;
            if (powa >= 1) {
                points += MOVE_POINTS;
            }
            if (powa <= -1) {
                points += WIN_POINTS;
            }
            String str = "M " + (char) ('A' + pos2.i) + (pos2.j + 1) + " "
                    + (char) ('A' + pos1.i) + pos1.j;
            moves.add(new movedata(str, points));
        }
    }

    protected void MoveRight2(position pos1, position pos2) {
        int powa = powerRight(pos2);
        if (Math.abs(powa) <= 1) {
            int points = 2;
            if (powa >= 1) {
                points += MOVE_POINTS;
            }
            if (powa <= -1) {
                points += WIN_POINTS;
            }
            String str = "M " + (char) ('A' + pos1.i) + (pos1.j + 1) + " "
                    + (char) ('A' + pos2.i) + (pos2.j + 2);
            moves.add(new movedata(str, points));
        }
    }

    protected void SideMoveDown2(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i - 1, pos1.j).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i - 1, pos2.j).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i - 1) + (pos1.j + 1);
            moves.add(new movedata(str, 2));
        }
    }

    protected void SideMoveUp2(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i + 1, pos1.j).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i + 1, pos2.j).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i + 1) + (pos1.j + 1);
            moves.add(new movedata(str, 2));
        }
    }

    protected void SideMoveDiagDownR2(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i - 1, pos1.j - 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i - 1, pos2.j - 1).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i - 1) + pos1.j;
            moves.add(new movedata(str, 2));
        }
    }

    protected void SideMoveDiagUpR2(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i + 1, pos1.j + 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i + 1, pos2.j + 1).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i + 1) + (pos1.j + 2);
            moves.add(new movedata(str, 2));
        }
    }

    protected void MoveLeft3(position pos1, position pos2) {
        int powa = powerLeft(pos1);
        if (Math.abs(powa) <= 2) {
            int points = 3;
            if (powa >= 1) {
                points += MOVE_POINTS;
            }
            if (powa <= -1) {
                points += WIN_POINTS;
            }
            String str = "M " + (char) ('A' + pos2.i) + (pos2.j + 1) + " "
                    + (char) ('A' + pos1.i) + pos1.j;
            moves.add(new movedata(str, points));
        }
    }

    protected void MoveRight3(position pos1, position pos2) {
        int powa = powerRight(pos2);
        if (Math.abs(powa) <= 2) {
            int points = 3;
            if (powa >= 1) {
                points += MOVE_POINTS;
            }
            if (powa <= -1) {
                points += WIN_POINTS;
            }
            String str = "M " + (char) ('A' + pos1.i) + (pos1.j + 1) + " "
                    + (char) ('A' + pos2.i) + (pos2.j + 2);
            moves.add(new movedata(str, points));
        }
    }

    protected void SideMoveDown3(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i - 1, pos1.j).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i - 1, pos2.j).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos1.i - 1, pos1.j + 1).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i - 1) + (pos1.j + 1);
            moves.add(new movedata(str, 3));
        }
    }

    protected void SideMoveUp3(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i + 1, pos1.j).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i + 1, pos2.j).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos1.i + 1, pos1.j + 1).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i + 1) + (pos1.j + 1);
            moves.add(new movedata(str, 3));
        }
    }

    protected void SideMoveDiagDownR3(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i - 1, pos1.j - 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i - 1, pos2.j - 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos1.i - 1, pos1.j).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i - 1) + pos1.j;
            moves.add(new movedata(str, 3));
        }
    }

    protected void SideMoveDiagUpR3(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i + 1, pos1.j + 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i + 1, pos2.j + 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos1.i + 1, pos1.j + 2).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i + 1) + (pos1.j + 2);
            moves.add(new movedata(str, 3));
        }
    }

    protected void generateMovesRow(ArrayList<position> gr) {
        position pos1, pos2;
        switch (gr.size()) {
            case 1: {
                MoveLeft1(gr.get(0));
                MoveRight1(gr.get(0));
            }
            break;

            case 2: {
                MoveLeft1(gr.get(0));
                MoveRight1(gr.get(1));
                pos1 = gr.get(0);
                pos2 = gr.get(1);
                MoveLeft2(pos1, pos2);
                MoveRight2(pos1, pos2);
                SideMoveDown2(pos1, pos2);
                SideMoveUp2(pos1, pos2);
                SideMoveDiagDownR2(pos1, pos2);
                SideMoveDiagUpR2(pos1, pos2);
            }
            break;

            default: {
                MoveLeft1(gr.get(0));
                MoveRight1(gr.get(gr.size() - 1));
                pos1 = gr.get(0);
                pos2 = gr.get(1);
                MoveLeft2(pos1, pos2);
                pos1 = gr.get(gr.size() - 2);
                pos2 = gr.get(gr.size() - 1);
                MoveRight2(pos1, pos2);
                for (int i = 0; i < gr.size() - 1; i++) {
                    pos1 = gr.get(i);
                    pos2 = gr.get(i + 1);
                    SideMoveDown2(pos1, pos2);
                    SideMoveUp2(pos1, pos2);
                    SideMoveDiagDownR2(pos1, pos2);
                    SideMoveDiagUpR2(pos1, pos2);
                }
                pos1 = gr.get(0);
                pos2 = gr.get(2);
                MoveLeft3(pos1, pos2);
                pos1 = gr.get(gr.size() - 3);
                pos2 = gr.get(gr.size() - 1);
                MoveRight3(pos1, pos2);
                for (int i = 0; i < gr.size() - 2; i++) {
                    pos1 = gr.get(i);
                    pos2 = gr.get(i + 2);
                    SideMoveDown3(pos1, pos2);
                    SideMoveUp3(pos1, pos2);
                    SideMoveDiagDownR3(pos1, pos2);
                    SideMoveDiagUpR3(pos1, pos2);
                }
            }
        }
    }

    protected int powerDown(position pos) {
        return 0;
    }

    protected int powerUp(position pos) {
        return 0;
    }

    protected void MoveDown1(position pos) {
        if (Board.getInstance().getField(pos.i - 1, pos.j).mrb == Marble.EMPTY) {
            String str = "M " + (char) ('A' + pos.i) + (pos.j + 1) + " "
                    + (char) ('A' + pos.i - 1) + (pos.j + 1);
            moves.add(new movedata(str, 1));
        }
    }

    protected void MoveUp1(position pos) {
        if (Board.getInstance().getField(pos.i + 1, pos.j).mrb == Marble.EMPTY) {
            String str = "M " + (char) ('A' + pos.i) + (pos.j + 1) + " "
                    + (char) ('A' + pos.i + 1) + (pos.j + 1);
            moves.add(new movedata(str, 1));
        }
    }

    protected void MoveDown2(position pos1, position pos2) {
        int powa = powerDown(pos1);
        if (Math.abs(powa) <= 1) {
            int points = 2;
            if (powa >= 1) {
                points += MOVE_POINTS;
            }
            if (powa <= -1) {
                points += WIN_POINTS;
            }
            String str = "M " + (char) ('A' + pos2.i) + (pos2.j + 1) + " "
                    + (char) ('A' + pos1.i - 1) + (pos1.j + 1);
            moves.add(new movedata(str, points));
        }
    }

    protected void MoveUp2(position pos1, position pos2) {
        int powa = powerUp(pos2);
        if (Math.abs(powa) <= 1) {
            int points = 2;
            if (powa >= 1) {
                points += MOVE_POINTS;
            }
            if (powa <= -1) {
                points += WIN_POINTS;
            }
            String str = "M " + (char) ('A' + pos1.i) + (pos1.j + 1) + " "
                    + (char) ('A' + pos2.i + 1) + (pos2.j + 1);
            moves.add(new movedata(str, points));
        }
    }

    protected void SideMoveLeft2(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i, pos1.j - 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i, pos2.j - 1).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i) + pos1.j;
            moves.add(new movedata(str, 2));
        }
    }

    protected void SideMoveRight2(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i, pos1.j + 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i, pos2.j + 1).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i) + (pos1.j + 2);
            moves.add(new movedata(str, 2));
        }
    }

    protected void SideMoveDiagDownC2(position pos1, position pos2) {
        SideMoveDiagDownR2(pos1, pos2);
    }

    protected void SideMoveDiagUpC2(position pos1, position pos2) {
        SideMoveDiagUpR2(pos1, pos2);
    }

    protected void MoveDown3(position pos1, position pos2) {
        int powa = powerDown(pos1);
        if (Math.abs(powa) <= 2) {
            int points = 3;
            if (powa >= 1) {
                points += MOVE_POINTS;
            }
            if (powa <= -1) {
                points += WIN_POINTS;
            }
            String str = "M " + (char) ('A' + pos2.i) + (pos2.j + 1) + " "
                    + (char) ('A' + pos1.i - 1) + (pos1.j + 1);
            moves.add(new movedata(str, points));
        }
    }

    protected void MoveUp3(position pos1, position pos2) {
        int powa = powerUp(pos2);
        if (Math.abs(powa) <= 2) {
            int points = 3;
            if (powa >= 1) {
                points += MOVE_POINTS;
            }
            // negative means wins 1
            if (powa <= -1) {
                points += WIN_POINTS;
            }
            String str = "M " + (char) ('A' + pos1.i) + (pos1.j + 1) + " "
                    + (char) ('A' + pos2.i + 1) + (pos2.j + 1);
            moves.add(new movedata(str, points));
        }
    }

    protected void SideMoveLeft3(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i, pos1.j - 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i, pos2.j - 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos1.i + 1, pos1.j - 1).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i) + pos1.j;
            moves.add(new movedata(str, 3));
        }
    }

    protected void SideMoveRight3(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i, pos1.j + 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i, pos2.j + 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos1.i + 1, pos1.j + 1).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i) + (pos1.j + 2);
            moves.add(new movedata(str, 3));
        }
    }

    protected void SideMoveDiagDownC3(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i - 1, pos1.j - 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i - 1, pos2.j - 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos1.i, pos1.j - 1).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i - 1) + pos1.j;
            moves.add(new movedata(str, 3));
        }
    }

    protected void SideMoveDiagUpC3(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i + 1, pos1.j + 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i + 1, pos2.j + 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos1.i + 2, pos1.j + 1).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i + 1) + (pos1.j + 2);
            moves.add(new movedata(str, 3));
        }
    }

    protected void generateMovesColumn(ArrayList<position> gr) {
        position pos1, pos2;
        switch (gr.size()) {
            case 1: {
                MoveDown1(gr.get(0));
                MoveUp1(gr.get(0));
            }
            break;

            case 2: {
                MoveDown1(gr.get(0));
                MoveUp1(gr.get(1));
                pos1 = gr.get(0);
                pos2 = gr.get(1);
                MoveDown2(pos1, pos2);
                MoveUp2(pos1, pos2);
                SideMoveLeft2(pos1, pos2);
                SideMoveRight2(pos1, pos2);
                SideMoveDiagDownC2(pos1, pos2);
                SideMoveDiagUpC2(pos1, pos2);
            }
            break;

            default: {
                MoveDown1(gr.get(0));
                MoveUp1(gr.get(gr.size() - 1));
                pos1 = gr.get(0);
                pos2 = gr.get(1);
                MoveDown2(pos1, pos2);
                pos1 = gr.get(gr.size() - 2);
                pos2 = gr.get(gr.size() - 1);
                MoveUp2(pos1, pos2);
                for (int i = 0; i < gr.size() - 1; i++) {
                    pos1 = gr.get(i);
                    pos2 = gr.get(i + 1);
                    SideMoveLeft2(pos1, pos2);
                    SideMoveRight2(pos1, pos2);
                    SideMoveDiagDownC2(pos1, pos2);
                    SideMoveDiagUpC2(pos1, pos2);
                }
                pos1 = gr.get(0);
                pos2 = gr.get(2);
                MoveDown3(pos1, pos2);
                pos1 = gr.get(gr.size() - 3);
                pos2 = gr.get(gr.size() - 1);
                MoveUp3(pos1, pos2);
                for (int i = 0; i < gr.size() - 2; i++) {
                    pos1 = gr.get(i);
                    pos2 = gr.get(i + 2);
                    SideMoveLeft3(pos1, pos2);
                    SideMoveRight3(pos1, pos2);
                    SideMoveDiagDownC3(pos1, pos2);
                    SideMoveDiagUpC3(pos1, pos2);
                }
            }
        }
    }

    protected int powerDiagDown(position pos) {
        return 0;
    }

    protected int powerDiagUp(position pos) {
        return 0;
    }

    protected void MoveDiagDown1(position pos) {
        if (Board.getInstance().getField(pos.i - 1, pos.j - 1).mrb == Marble.EMPTY) {
            String str = "M " + (char) ('A' + pos.i) + (pos.j + 1) + " "
                    + (char) ('A' + pos.i - 1) + pos.j;
            moves.add(new movedata(str, 1));
        }
    }

    protected void MoveDiagUp1(position pos) {
        if (Board.getInstance().getField(pos.i + 1, pos.j + 1).mrb == Marble.EMPTY) {
            String str = "M " + (char) ('A' + pos.i) + (pos.j + 1) + " "
                    + (char) ('A' + pos.i + 1) + (pos.j + 2);
            moves.add(new movedata(str, 1));
        }
    }

    protected void MoveDiagDown2(position pos1, position pos2) {
        int powa = powerDiagDown(pos1);
        if (Math.abs(powa) <= 1) {
            int points = 2;
            if (powa >= 1) {
                points += MOVE_POINTS;
            }
            if (powa <= -1) {
                points += WIN_POINTS;
            }
            String str = "M " + (char) ('A' + pos2.i) + (pos2.j + 1) + " "
                    + (char) ('A' + pos1.i - 1) + pos1.j;
            moves.add(new movedata(str, points));
        }
    }

    protected void MoveDiagUp2(position pos1, position pos2) {
        int powa = powerDiagUp(pos2);
        if (Math.abs(powa) <= 1) {
            int points = 2;
            if (powa >= 1) {
                points += MOVE_POINTS;
            }
            if (powa <= -1) {
                points += WIN_POINTS;
            }
            String str = "M " + (char) ('A' + pos1.i) + (pos1.j + 1) + " "
                    + (char) ('A' + pos2.i + 1) + (pos2.j + 2);
            moves.add(new movedata(str, points));
        }
    }

    protected void SideMoveLeftD2(position pos1, position pos2) {
        SideMoveLeft2(pos1, pos2);
    }

    protected void SideMoveRightD2(position pos1, position pos2) {
        SideMoveRight2(pos1, pos2);
    }

    protected void SideMoveDownD2(position pos1, position pos2) {
        SideMoveDown2(pos1, pos2);
    }

    protected void SideMoveUpD2(position pos1, position pos2) {
        SideMoveUp2(pos1, pos2);
    }

    protected void MoveDiagDown3(position pos1, position pos2) {
        int powa = powerDiagDown(pos1);
        if (Math.abs(powa) <= 2) {
            int points = 3;
            if (powa >= 1) {
                points += MOVE_POINTS;
            }
            if (powa <= -1) {
                points += WIN_POINTS;
            }
            String str = "M " + (char) ('A' + pos2.i) + (pos2.j + 1) + " "
                    + (char) ('A' + pos1.i - 1) + pos1.j;
            moves.add(new movedata(str, points));
        }
    }

    protected void MoveDiagUp3(position pos1, position pos2) {
        int powa = powerDiagUp(pos2);
        if (Math.abs(powa) <= 2) {
            int points = 3;
            if (powa >= 1) {
                points += MOVE_POINTS;
            }
            if (powa <= -1) {
                points += WIN_POINTS;
            }
            String str = "M " + (char) ('A' + pos1.i) + (pos1.j + 1) + " "
                    + (char) ('A' + pos2.i + 1) + (pos2.j + 2);
            moves.add(new movedata(str, points));
        }
    }

    protected void SideMoveLeftD3(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i, pos1.j - 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i, pos2.j - 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos1.i + 1, pos1.j).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i) + pos1.j;
            moves.add(new movedata(str, 3));
        }
    }

    protected void SideMoveRightD3(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i, pos1.j + 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i, pos2.j + 1).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos1.i + 1, pos1.j + 2).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i) + (pos1.j + 2);
            moves.add(new movedata(str, 3));
        }
    }

    protected void SideMoveDownD3(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i - 1, pos1.j).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i - 1, pos2.j).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos1.i, pos1.j + 1).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i - 1) + (pos1.j + 1);
            moves.add(new movedata(str, 3));
        }
    }

    protected void SideMoveUpD3(position pos1, position pos2) {
        if (Board.getInstance().getField(pos1.i + 1, pos1.j).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos2.i + 1, pos2.j).mrb == Marble.EMPTY
                && Board.getInstance().getField(pos1.i + 2, pos1.j + 1).mrb == Marble.EMPTY) {
            String str = "S " + (char) ('A' + pos1.i) + (pos1.j + 1)
                    + " " + (char) ('A' + pos2.i) + (pos2.j + 1)
                    + " " + (char) ('A' + pos1.i + 1) + (pos1.j + 1);
            moves.add(new movedata(str, 3));
        }
    }

    protected void generateMovesDiag(ArrayList<position> gr) {
        position pos1, pos2;
        switch (gr.size()) {
            case 1: {
                MoveDiagDown1(gr.get(0));
                MoveDiagUp1(gr.get(0));
            }
            break;

            case 2: {
                MoveDiagDown1(gr.get(0));
                MoveDiagUp1(gr.get(1));
                pos1 = gr.get(0);
                pos2 = gr.get(1);
                MoveDiagDown2(pos1, pos2);
                MoveDiagUp2(pos1, pos2);
                SideMoveLeftD2(pos1, pos2);
                SideMoveRightD2(pos1, pos2);
                SideMoveDownD2(pos1, pos2);
                SideMoveUpD2(pos1, pos2);
            }
            break;

            default: {
                MoveDiagDown1(gr.get(0));
                MoveDiagUp1(gr.get(gr.size() - 1));
                pos1 = gr.get(0);
                pos2 = gr.get(1);
                MoveDiagDown2(pos1, pos2);
                pos1 = gr.get(gr.size() - 2);
                pos2 = gr.get(gr.size() - 1);
                MoveDiagUp2(pos1, pos2);
                for (int i = 0; i < gr.size() - 1; i++) {
                    pos1 = gr.get(i);
                    pos2 = gr.get(i + 1);
                    SideMoveLeftD2(pos1, pos2);
                    SideMoveRightD2(pos1, pos2);
                    SideMoveDownD2(pos1, pos2);
                    SideMoveUpD2(pos1, pos2);
                }
                pos1 = gr.get(0);
                pos2 = gr.get(2);
                MoveDiagDown3(pos1, pos2);
                pos1 = gr.get(gr.size() - 3);
                pos2 = gr.get(gr.size() - 1);
                MoveDiagUp3(pos1, pos2);
                for (int i = 0; i < gr.size() - 2; i++) {
                    pos1 = gr.get(i);
                    pos2 = gr.get(i + 2);
                    SideMoveLeftD3(pos1, pos2);
                    SideMoveRightD3(pos1, pos2);
                    SideMoveDownD3(pos1, pos2);
                    SideMoveUpD3(pos1, pos2);
                }
            }
        }
    }

    protected void generateMoves() {
        int i, j, k;
        moves.clear();
        ArrayList<position> group = new ArrayList<>();
        for (i = 0; i < 9; i++) {
            j = 0;
            while (j < 9) {
                while (j < 9 && Board.getInstance().getField(i, j).mrb != mp1) {
                    j++;
                }
                group.clear();
                while (j < 9 && Board.getInstance().getField(i, j).mrb == mp1) {
                    group.add(new position(i, j));
                    j++;
                }
                if (group.size() > 0) {
                    generateMovesRow(group);
                }
            }
        }
        for (j = 0; j < 9; j++) {
            i = 0;
            while (i < 9) {
                while (i < 9 && Board.getInstance().getField(i, j).mrb != mp1) {
                    i++;
                }
                group.clear();
                while (i < 9 && Board.getInstance().getField(i, j).mrb == mp1) {
                    group.add(new position(i, j));
                    i++;
                }
                if (group.size() > 0) {
                    generateMovesColumn(group);
                }
            }
        }
        for (k = 0; k < 5; k++) {
            i = k;
            j = 0;
            while (Board.getInstance().getField(i, j).mrb != Marble.OUT) {
                while (Board.getInstance().getField(i, j).mrb != Marble.OUT && Board.getInstance().getField(i, j).mrb != mp1) {
                    i++;
                    j++;
                }
                group.clear();
                while (Board.getInstance().getField(i, j).mrb == mp1) {
                    group.add(new position(i, j));
                    i++;
                    j++;
                }
                if (group.size() > 0) {
                    generateMovesDiag(group);
                }
            }
        }
        for (k = 1; k < 5; k++) {
            i = 0;
            j = k;
            while (Board.getInstance().getField(i, j).mrb != Marble.OUT) {
                while (Board.getInstance().getField(i, j).mrb != Marble.OUT && Board.getInstance().getField(i, j).mrb != mp1) {
                    i++;
                    j++;
                }
                group.clear();
                while (Board.getInstance().getField(i, j).mrb == mp1) {
                    group.add(new position(i, j));
                    i++;
                    j++;
                }
                if (group.size() > 0) {
                    generateMovesDiag(group);
                }
            }
        }
    }

    public String move() {
        generateMoves();
        String move = generateRandomMove();
        return move;
    }
}
