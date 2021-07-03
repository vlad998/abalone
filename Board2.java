package abalone;

public class Board2 extends Board {

    private Board2() {
        super();
        gamemode = 2;
        arrs[0] = new Human();
        arrs[1] = new Human();
        this.init();
    }

    public static void CreateBoard() {
        Board.instance = new Board2();
    }

    @Override
    public void setStrategy(int ix, char type) {
        if (ix >= 0 || ix <= 1) {
            if (type == 'h') {
                arrs[ix] = new Human();
                msg = "OK";
            } else if (type == 'c') {
                arrs[ix] = new Computer2(ix);
                msg = "OK";
            } else {
                msg = "Unknown strategy type!";
            }
        } else {
            msg = "Index out of range!";
        }
    }

    @Override
    public int getScore(int player) {
        switch (player) {
            case 0:
                return scoreb;

            case 1:
                return scorer;

            default:
                return -1;
        }
    }

    @Override
    public void setScore(int player, int val) {
        switch (player) {
            case 0:
                scoreb = val;
                break;

            case 1:
                scorer = val;
        }
    }

    @Override
    public void init() {
        super.init();
        for (int i = 0; i < SIZES[0]; i++) {
            arr[0][i].mrb = Marble.BLUE;
        }
        for (int i = 0; i < SIZES[1]; i++) {
            arr[1][i].mrb = Marble.BLUE;
        }
        arr[2][2].mrb = Marble.BLUE;
        arr[2][3].mrb = Marble.BLUE;
        arr[2][4].mrb = Marble.BLUE;
        for (int i = 9 - SIZES[8]; i < 9; i++) {
            arr[8][i].mrb = Marble.RED;
        }
        for (int i = 9 - SIZES[7]; i < 9; i++) {
            arr[7][i].mrb = Marble.RED;
        }
        arr[6][4].mrb = Marble.RED;
        arr[6][5].mrb = Marble.RED;
        arr[6][6].mrb = Marble.RED;
        gameturn = 0;
        scoreb = 0;
        scorer = 0;
    }

    @Override
    public boolean positionEnd(int r1, int c1, int[] arr1) {
        int r2 = arr1[0];
        int c2 = arr1[1];
        int dr = r2 - r1;
        int dc = c2 - c1;
        if (dr == 0 && dc == 0) {
            return false;
        }
        if (arr[r2][c2].mrb == Marble.EMPTY) {
            return true;
        }
        Marble mp = Marble.BLUE;
        Marble ma = Marble.RED;
        if (gameturn == 1) {
            mp = Marble.RED;
            ma = Marble.BLUE;
        }
        if (arr[r2][c2].mrb == ma) {
            return false;
        }
        r2 += dr;
        c2 += dc;
        if (r2 < 0 || r2 >= DIM_ROWS || c2 < 0 || c2 >= DIM_COLS) {
            return false;
        }
        if (arr[r2][c2].mrb == Marble.EMPTY || arr[r2][c2].mrb == ma) {
            arr1[0] = r2;
            arr1[1] = c2;
            return true;
        }
        if (arr[r2][c2].mrb == Marble.OUT) {
            return false;
        }
        r2 += dr;
        c2 += dc;
        if (r2 < 0 || r2 >= DIM_ROWS || c2 < 0 || c2 >= DIM_COLS) {
            return false;
        }
        if (arr[r2][c2].mrb == Marble.EMPTY || arr[r2][c2].mrb == ma) {
            arr1[0] = r2;
            arr1[1] = c2;
            return true;
        }
        return false;
    }

    @Override
    public boolean move(char cr1, char cc1, char cr2, char cc2) {
        if (!super.move(cr1, cc1, cr2, cc2)) {
            return false;
        }
        Marble mp = Marble.BLUE;
        Marble ma = Marble.RED;
        if (gameturn == 1) {
            mp = Marble.RED;
            ma = Marble.BLUE;
        }
        if (arr[r1][c1].mrb != mp) {
            msg = "Wrong marble selection!";
            return false;
        }
        if (arr[r2][c2].mrb == mp) {
            msg = "Wrong field selection!";
            return false;
        }
        int deltar = (int) Math.signum(r2 - r1);
        int deltac = (int) Math.signum(c2 - c1);
        int i, r, c;
        for (r = r1 + deltar, c = c1 + deltac; (r != r2) || (c != c2); r += deltar, c += deltac) {
            if (arr[r][c].mrb != mp) {
                msg = "Wrong marble selection!";
                return false;
            }
        }
        int powerp = Math.max(Math.abs(r2 - r1), Math.abs(c2 - c1));
        if (powerp > 3) {
            msg = "Power is limited to 3!";
            return false;
        }
        int powera = 0;
        if (arr[r2][c2].mrb == ma) {
            powera = 1;
            for (r = r2 + deltar, c = c2 + deltac;; r += deltar, c += deltac) {
                if (!isOnBoard(r, c)) {
                    break;
                }
                if (arr[r][c].mrb == mp) {
                    msg = "Cannot push!";
                    return false;
                }
                if (arr[r][c].mrb != ma) {
                    break;
                }
                powera++;
            }
            if (powerp <= powera) {
                msg = "Not enough push power!";
                return false;
            }
        }
        moved.clear();
        int pow = powerp + powera;
        for (i = 0, r = r1 + deltar * pow, c = c1 + deltac * pow; i < pow;
                r -= deltar, c -= deltac, i++) {
            if (isOnBoard(r, c)) {
                arr[r][c].mrb = arr[r - deltar][c - deltac].mrb;
                moved.add(new Position(r, c));
            } else {
                if (gameturn == 0) {
                    scoreb++;
                    elimRed++;
                } else {
                    scorer++;
                    elimBlue++;
                }
            }
        }
        arr[r1][c1].mrb = Marble.EMPTY;
        if (scoreb >= WIN_SCORE) {
            msg = "Blue Wins!";
            return true;
        }
        if (scorer >= WIN_SCORE) {
            msg = "Red Wins!";
            return true;
        }
        aturns[gameturn]++;
        gameturn = (gameturn + 1) % gamemode;
        msg = "OK";
        return true;
    }

    @Override
    public boolean sidemove(char cr1, char cc1, char cr2, char cc2, char cr3, char cc3) {
        if (!super.sidemove(cr1, cc1, cr2, cc2, cr3, cc3)) {
            return false;
        }
        Marble mp = Marble.BLUE;
        if (gameturn == 1) {
            mp = Marble.RED;
        }
        return sidemove1(mp, null);
    }
}
