package abalone;

import java.util.*;
import java.awt.Color;

public abstract class Board {

    protected static final int INI_MAX_TURNS = 30;
    protected static final int INI_MAX_TIME = 300;
    protected static final int DIM_ROWS = 9;
    protected static final int DIM_COLS = 9;
    protected static final int N_PLAYERS = 4;
    protected static final int WIN_SCORE = 6;
    protected static final int[] SIZES = new int[]{5, 6, 7, 8, 9, 8, 7, 6, 5};
    protected static final int[] SIZES1 = new int[]{4, 3, 2, 1, 0, 1, 2, 3, 4};
    protected static final Color BLUE1 = new Color(0, 0, 130);
    protected static final Color RED1 = new Color(150, 0, 0);
    protected static final Color GREEN1 = new Color(0, 150, 0);
    protected static final Color ORANGE1 = new Color(150, 97, 0);
    protected static final Color GRAY1 = new Color(200, 200, 200);
    protected static final Color MOVE_BLUE = new Color(147, 147, 255);
    protected static final Color MOVE_RED = new Color(255, 91, 91);
    protected static final Color MOVE_GREEN = new Color(79, 211, 22);
    protected static final Color MOVE_ORANGE = new Color(247, 155, 64);
    protected Field arr[][] = null;
    protected Strategy arrs[] = null;
    protected int gamemode = 0;
    protected int gameturn = 0;
    protected int scoreb = 0;
    protected int scorer = 0;
    protected int scoreg = 0;
    protected int elimBlue = 0;
    protected int elimRed = 0;
    protected int elimGreen = 0;
    protected int elimOrange = 0;
    protected int[] aturns = new int[N_PLAYERS];

    protected String msg;
    protected int r1, c1, r2, c2, r3, c3;
    protected static int maxturns = INI_MAX_TURNS;
    protected static int maxtime = INI_MAX_TIME;

    protected static Board instance = null;

    protected Set<Position> moved = new HashSet();

    public static Board getInstance() {
        return instance;
    }

    public void resetMoved() {
        moved.clear();
    }

    public Set<Position> getMoved() {
        return moved;
    }

    public static void CreateBoard(int mode) {
        switch (mode) {
            case 2:
                Board2.CreateBoard();
                break;

            case 3:
                Board3.CreateBoard();
                break;

            case 4:
                Board4.CreateBoard();
                break;
        }
    }

    protected Board() {
        arr = new Field[DIM_ROWS][DIM_COLS];
        for (int i = 0; i < DIM_ROWS; i++) {
            for (int j = 0; j < DIM_COLS; j++) {
                arr[i][j] = new Field();
            }
        }
        init();
        arrs = new Strategy[N_PLAYERS];
    }

    public boolean checkWins() {
        return (scoreb >= WIN_SCORE) || (scorer >= WIN_SCORE);
    }

    public boolean isHuman(int ix) {
        return arrs[ix].isHuman();
    }

    public abstract void setStrategy(int ix, char type);

    public void resetStrategy() {
        setStrategy(0, 'H');
        setStrategy(1, 'H');
        setStrategy(2, 'H');
        setStrategy(3, 'H');
    }

    public static int getMaxturns() {
        return maxturns;
    }

    public static void setMaxturns(int maxturns) {
        Board.maxturns = maxturns;
    }

    public static int getMaxtime() {
        return maxtime;
    }

    public static void setMaxtime(int maxtime) {
        Board.maxtime = maxtime;
    }

    public int getElimBlue() {
        return elimBlue;
    }

    public void setElimBlue(int val) {
        elimBlue = val;
    }

    public int getElimRed() {
        return elimRed;
    }

    public void setElimRed(int val) {
        elimRed = val;
    }

    public int getElimGreen() {
        return elimGreen;
    }

    public void setElimGreen(int val) {
        elimGreen = val;
    }

    public int getElimOrange() {
        return elimOrange;
    }

    public void setElimOrange(int val) {
        elimOrange = val;
    }

    public boolean CheckMaxTurns() {
        boolean res = true;
        for (int i = 0; i < gamemode; i++) {
            res = res && (aturns[i] >= maxturns);
        }
        return !res;
    }

    public Field getField(int i, int j) {
        if (0 <= i && i < DIM_ROWS && 0 <= j && j < DIM_COLS) {
            return arr[i][j];
        } else {
            return new Field();
        }
    }

    public void setField(int i, int j, Field val) {
        if (0 <= i && i < DIM_ROWS && 0 <= j && j < DIM_COLS) {
            arr[i][j] = new Field(val);
        }
    }

    public int getTurns(int i) {
        return aturns[i];
    }

    public void setTurns(int i, int val) {
        aturns[i] = val;
    }

    public String nextCommand() {
        String cmd = arrs[gameturn].move();
        return cmd;
    }

    public int getGamemode() {
        return gamemode;
    }

    public int getGameturn() {
        return gameturn;
    }

    public void setGameturn(int gameturn) {
        this.gameturn = gameturn;
    }

    public abstract int getScore(int player);

    public abstract void setScore(int player, int val);

    public String getMessage() {
        return msg;
    }

    protected void init() {
        gameturn = 0;
        scoreb = 0;
        scorer = 0;
        aturns[0] = 0;
        aturns[1] = 0;
        aturns[2] = 0;
        aturns[3] = 0;
        for (int i = 0; i < DIM_ROWS; i++) {
            for (int j = 0; j < DIM_COLS; j++) {
                if (i < 5) {
                    if (j < SIZES[i]) {
                        arr[i][j].mrb = Marble.EMPTY;
                    } else {
                        arr[i][j].mrb = Marble.OUT;
                    }
                } else {
                    if (j < DIM_COLS - SIZES[i]) {
                        arr[i][j].mrb = Marble.OUT;
                    } else {
                        arr[i][j].mrb = Marble.EMPTY;
                    }
                }
            }
        }
    }

    public Marble getMarble(int i, int j) {
        if (i >= 0 && i < DIM_ROWS && j >= 0 && j < DIM_COLS) {
            return arr[i][j].mrb;
        }
        return null;
    }

    public boolean isOnBoard(int r, int c) {
        if (r < 0 || r >= DIM_ROWS) {
            return false;
        }
        if (c < 0 || c >= DIM_COLS) {
            return false;
        }
        if (arr[r][c].mrb == Marble.OUT) {
            return false;
        }
        return true;
    }

    protected abstract boolean positionEnd(int irowStart, int icolStart, int[] arr);

    protected boolean move(char cr1, char cc1, char cr2, char cc2) {
        r1 = (cr1 - 'A');
        c1 = (cc1 - '1');
        r2 = (cr2 - 'A');
        c2 = (cc2 - '1');
        int dr = r2 - r1;
        int dc = c2 - c1;
        if (dr == 0) {
            if (dc == 0) {
                msg = "Positions should be different!";
                return false;
            }
        } else {
            if (dc != 0 && dc != dr) {
                msg = "Wrong move direction!";
                return false;
            }
        }
        if (!isOnBoard(r1, c1) || !isOnBoard(r2, c2)) {
            msg = "Position out of board!";
            return false;
        }
        int deltar = (int) Math.signum(r2 - r1);
        int deltac = (int) Math.signum(c2 - c1);
        if (deltar + deltac == 0) {
            msg = "Wrong move direction!";
            return false;
        }
        return true;
    }

    protected boolean sidemove(char cr1, char cc1, char cr2, char cc2, char cr3, char cc3) {
        r1 = (cr1 - 'A');
        c1 = (cc1 - '1');
        r2 = (cr2 - 'A');
        c2 = (cc2 - '1');
        r3 = (cr3 - 'A');
        c3 = (cc3 - '1');
        int dr = r2 - r1;
        int dc = c2 - c1;
        if (dr == 0) {
            if (dc == 0) {
                msg = "Positions should be different!";
                return false;
            }
            if (Math.abs(dc) > 2) {
                msg = "More than 3 marbles not allowed!";
                return false;
            }
        } else {
            if (dc != 0 && dc != dr) {
                msg = "Wrong side move direction!";
                return false;
            }
            if (Math.abs(dr) > 2) {
                msg = "More than 3 marbles not allowed!";
                return false;
            }
        }
        if ((r3 == r1 || r3 == r2) && (c3 == c1 || c3 == c2)) {
            msg = "Positions should be different!";
        }
        int deltar = (int) Math.signum(r2 - r1);
        int deltac = (int) Math.signum(c2 - c1);
        if (deltar + deltac == 0) {
            msg = "Wrong side move selection!";
            return false;
        }
        int dr1 = r3 - r1;
        int dc1 = c3 - c1;
        if (dr != 0) {
            if (dc != 0) {
                if (dr1 != 0 && dc1 != 0) {
                    msg = "Wrong side move direction!";
                    return false;
                }
            } else {
                if (dc1 == 0) {
                    msg = "Wrong side move direction!";
                    return false;
                }
            }
        } else if (dc != 0) {
            if (dr1 == 0) {
                msg = "Wrong side move direction!";
                return false;
            }
        }
        if (Math.abs(dr1) > 1 || Math.abs(dc1) > 1) {
            msg = "Wrong side move position!";
            return false;
        }
        if (!isOnBoard(r1, c1) || !isOnBoard(r2, c2) || !isOnBoard(r3, c3)) {
            msg = "Position out of board!";
            return false;
        }
        return true;
    }

    protected boolean sidemove1(Marble mp1, Marble mp2) {
        int deltar = (int) Math.signum(r2 - r1);
        int deltac = (int) Math.signum(c2 - c1);
        int deltar1 = (int) Math.signum(r3 - r1);
        int deltac1 = (int) Math.signum(c3 - c1);
        for (int r = r1, c = c1; (r != r2 + deltar) || (c != c2 + deltac); r += deltar, c += deltac) {
            if (arr[r][c].mrb != mp1) {
                msg = "Wrong marble selection!";
                return false;
            }
            if (!isOnBoard(r + deltar1, c + deltac1) || arr[r + deltar1][c + deltac1].mrb != Marble.EMPTY) {
                msg = "Wrong side move direction!";
                return false;
            }
        }
        moved.clear();
        for (int r = r1, c = c1; (r != r2 + deltar) || (c != c2 + deltac); r += deltar, c += deltac) {
            arr[r][c].mrb = Marble.EMPTY;
            arr[r + deltar1][c + deltac1].mrb = mp1;
            moved.add(new Position(r + deltar1, c + deltac1));
        }
        aturns[gameturn]++;
        gameturn = (gameturn + 1) % gamemode;
        msg = "OK";
        return true;
    }
}
