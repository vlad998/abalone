package abalone;

import java.util.ArrayList;

public class Computer4 extends Computer3 {

    public Computer4(int ix) {
        super(ix);
        mp1 = Marble.BLUE;
        mp2 = Marble.GREEN;
        ma1 = Marble.RED;
        ma2 = Marble.ORANGE;
        if (ix == 1) {
            mp1 = Marble.RED;
            mp2 = Marble.ORANGE;
            ma1 = Marble.BLUE;
            ma2 = Marble.GREEN;
        } else if (ix == 2) {
            mp1 = Marble.GREEN;
            mp2 = Marble.BLUE;
            ma1 = Marble.RED;
            ma2 = Marble.ORANGE;
        } else if (ix == 3) {
            mp1 = Marble.ORANGE;
            mp2 = Marble.RED;
            ma1 = Marble.BLUE;
            ma2 = Marble.GREEN;
        }
    }

    @Override
    protected void generateMovesRow(ArrayList<position> gr) {
        position pos1, pos2, pos3;
        switch (gr.size()) {
            case 1: {
                pos1 = gr.get(0);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveLeft1(pos1);
                    MoveRight1(pos1);
                }
            }
            break;

            case 2: {
                pos1 = gr.get(0);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveLeft1(pos1);
                }
                pos1 = gr.get(1);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveRight1(pos1);
                }
                pos1 = gr.get(0);
                pos2 = gr.get(1);
                if (Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                    MoveLeft2(pos1, pos2);
                }
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveRight2(pos1, pos2);
                }
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1 || Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                    SideMoveDown2(pos1, pos2);
                    SideMoveUp2(pos1, pos2);
                    SideMoveDiagDownR2(pos1, pos2);
                    SideMoveDiagUpR2(pos1, pos2);
                }
            }
            break;

            default: {
                pos1 = gr.get(0);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveLeft1(pos1);
                }
                pos1 = gr.get(gr.size() - 1);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveRight1(pos1);
                }
                pos1 = gr.get(0);
                pos2 = gr.get(1);
                if (Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                    MoveLeft2(pos1, pos2);
                }
                pos1 = gr.get(gr.size() - 2);
                pos2 = gr.get(gr.size() - 1);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveRight2(pos1, pos2);
                }
                for (int i = 0; i < gr.size() - 1; i++) {
                    pos1 = gr.get(i);
                    pos2 = gr.get(i + 1);
                    if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1
                            || Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                        SideMoveDown2(pos1, pos2);
                        SideMoveUp2(pos1, pos2);
                        SideMoveDiagDownR2(pos1, pos2);
                        SideMoveDiagUpR2(pos1, pos2);
                    }
                }
                pos1 = gr.get(0);
                pos2 = gr.get(2);
                if (Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                    MoveLeft3(pos1, pos2);
                }
                pos1 = gr.get(gr.size() - 3);
                pos2 = gr.get(gr.size() - 1);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveRight3(pos1, pos2);
                }
                for (int i = 0; i < gr.size() - 2; i++) {
                    pos1 = gr.get(i);
                    pos2 = gr.get(i + 1);
                    pos3 = gr.get(i + 2);
                    if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1
                            || Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1
                            || Board.getInstance().getField(pos3.i, pos3.j).mrb == mp1) {
                        SideMoveDown3(pos1, pos3);
                        SideMoveUp3(pos1, pos3);
                        SideMoveDiagDownR3(pos1, pos3);
                        SideMoveDiagUpR3(pos1, pos3);
                    }
                }
            }
        }
    }

    @Override
    protected void generateMovesColumn(ArrayList<position> gr) {
        position pos1, pos2, pos3;
        switch (gr.size()) {
            case 1: {
                pos1 = gr.get(0);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveDown1(pos1);
                    MoveUp1(pos1);
                }
            }
            break;

            case 2: {
                pos1 = gr.get(0);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveDown1(pos1);
                }
                pos1 = gr.get(1);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveUp1(pos1);
                }
                pos1 = gr.get(0);
                pos2 = gr.get(1);
                if (Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                    MoveDown2(pos1, pos2);
                }
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveUp2(pos1, pos2);
                }
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1
                        || Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                    SideMoveLeft2(pos1, pos2);
                    SideMoveRight2(pos1, pos2);
                    SideMoveDiagDownC2(pos1, pos2);
                    SideMoveDiagUpC2(pos1, pos2);
                }
            }
            break;

            default: {
                pos1 = gr.get(0);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveDown1(pos1);
                }
                pos1 = gr.get(gr.size() - 1);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveUp1(pos1);
                }
                pos1 = gr.get(0);
                pos2 = gr.get(1);
                if (Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                    MoveDown2(pos1, pos2);
                }
                pos1 = gr.get(gr.size() - 2);
                pos2 = gr.get(gr.size() - 1);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveUp2(pos1, pos2);
                }
                for (int i = 0; i < gr.size() - 1; i++) {
                    pos1 = gr.get(i);
                    pos2 = gr.get(i + 1);
                    if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1
                            || Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                        SideMoveLeft2(pos1, pos2);
                        SideMoveRight2(pos1, pos2);
                        SideMoveDiagDownC2(pos1, pos2);
                        SideMoveDiagUpC2(pos1, pos2);
                    }
                }
                pos1 = gr.get(0);
                pos2 = gr.get(2);
                if (Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                    MoveDown3(pos1, pos2);
                }
                pos1 = gr.get(gr.size() - 3);
                pos2 = gr.get(gr.size() - 1);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveUp3(pos1, pos2);
                }
                for (int i = 0; i < gr.size() - 2; i++) {
                    pos1 = gr.get(i);
                    pos2 = gr.get(i + 1);
                    pos3 = gr.get(i + 2);
                    if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1
                            || Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1
                            || Board.getInstance().getField(pos3.i, pos3.j).mrb == mp1) {
                        SideMoveLeft3(pos1, pos3);
                        SideMoveRight3(pos1, pos3);
                        SideMoveDiagDownC3(pos1, pos3);
                        SideMoveDiagUpC3(pos1, pos3);
                    }
                }
            }
        }
    }

    @Override
    protected void generateMovesDiag(ArrayList<position> gr) {
        position pos1, pos2, pos3;
        switch (gr.size()) {
            case 1: {
                pos1 = gr.get(0);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    // Move 1
                    MoveDiagDown1(pos1);
                    MoveDiagUp1(pos1);
                }
            }
            break;

            case 2: {
                pos1 = gr.get(0);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveDiagDown1(pos1);
                }
                pos1 = gr.get(1);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveDiagUp1(pos1);
                }
                pos1 = gr.get(0);
                pos2 = gr.get(1);
                if (Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                    MoveDiagDown2(pos1, pos2);
                }
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveDiagUp2(pos1, pos2);
                }
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1
                        || Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                    SideMoveLeftD2(pos1, pos2);
                    SideMoveRightD2(pos1, pos2);
                    SideMoveDownD2(pos1, pos2);
                    SideMoveUpD2(pos1, pos2);
                }
            }
            break;

            default: {
                pos1 = gr.get(0);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveDiagDown1(pos1);
                }
                pos1 = gr.get(gr.size() - 1);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveDiagUp1(pos1);
                }
                pos1 = gr.get(0);
                pos2 = gr.get(1);
                if (Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                    MoveDiagDown2(pos1, pos2);
                }
                pos1 = gr.get(gr.size() - 2);
                pos2 = gr.get(gr.size() - 1);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveDiagUp2(pos1, pos2);
                }
                for (int i = 0; i < gr.size() - 1; i++) {
                    pos1 = gr.get(i);
                    pos2 = gr.get(i + 1);
                    if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1
                            || Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                        SideMoveLeftD2(pos1, pos2);
                        SideMoveRightD2(pos1, pos2);
                        SideMoveDownD2(pos1, pos2);
                        SideMoveUpD2(pos1, pos2);
                    }
                }
                pos1 = gr.get(0);
                pos2 = gr.get(2);
                if (Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1) {
                    MoveDiagDown3(pos1, pos2);
                }
                pos1 = gr.get(gr.size() - 3);
                pos2 = gr.get(gr.size() - 1);
                if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1) {
                    MoveDiagUp3(pos1, pos2);
                }
                for (int i = 0; i < gr.size() - 2; i++) {
                    pos1 = gr.get(i);
                    pos2 = gr.get(i + 1);
                    pos3 = gr.get(i + 2);
                    if (Board.getInstance().getField(pos1.i, pos1.j).mrb == mp1
                            || Board.getInstance().getField(pos2.i, pos2.j).mrb == mp1
                            || Board.getInstance().getField(pos3.i, pos3.j).mrb == mp1) {
                        SideMoveLeftD3(pos1, pos3);
                        SideMoveRightD3(pos1, pos3);
                        SideMoveDownD3(pos1, pos3);
                        SideMoveUpD3(pos1, pos3);
                    }
                }
            }
        }
    }

    @Override
    protected void generateMoves() {
        int i, j, k;
        moves.clear();
        ArrayList<position> group = new ArrayList<>();
        for (i = 0; i < Board.DIM_ROWS; i++) {
            j = 0;
            while (j < Board.DIM_COLS) {
                while (j < Board.DIM_COLS && Board.getInstance().getField(i, j).mrb != mp1
                        && Board.getInstance().getField(i, j).mrb != mp2) {
                    j++;
                }
                group.clear();
                while (j < Board.DIM_COLS && (Board.getInstance().getField(i, j).mrb == mp1
                        || Board.getInstance().getField(i, j).mrb == mp2)) {
                    group.add(new position(i, j));
                    j++;
                }
                if (group.size() > 0) {
                    generateMovesRow(group);
                }
            }
        }
        for (j = 0; j < Board.DIM_COLS; j++) {
            i = 0;
            while (i < Board.DIM_ROWS) {
                while (i < Board.DIM_ROWS && Board.getInstance().getField(i, j).mrb != mp1
                        && Board.getInstance().getField(i, j).mrb != mp2) {
                    i++;
                }
                group.clear();
                while (i < Board.DIM_ROWS && (Board.getInstance().getField(i, j).mrb == mp1
                        || Board.getInstance().getField(i, j).mrb == mp2)) {
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
                while (Board.getInstance().getField(i, j).mrb != Marble.OUT
                        && Board.getInstance().getField(i, j).mrb != mp1
                        && Board.getInstance().getField(i, j).mrb != mp2) {
                    i++;
                    j++;
                }
                group.clear();
                while (Board.getInstance().getField(i, j).mrb == mp1
                        || Board.getInstance().getField(i, j).mrb == mp2) {
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
                while (Board.getInstance().getField(i, j).mrb != Marble.OUT
                        && Board.getInstance().getField(i, j).mrb != mp1
                        && Board.getInstance().getField(i, j).mrb != mp2) {
                    i++;
                    j++;
                }
                group.clear();
                while (Board.getInstance().getField(i, j).mrb == mp1 || Board.getInstance().getField(i, j).mrb == mp2) {
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
}
