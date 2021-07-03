package abalone;

public class Board4 extends Board {

    private Board4() {
        super();
        gamemode = 4;
        arrs[0] = new Human();
        arrs[1] = new Human();
        arrs[2] = new Human();
        arrs[3] = new Human();
        this.init();
    }

    public static void CreateBoard() {
        Board.instance = new Board4();
    }

    @Override
    public void setStrategy(int ix, char type) {
        if (ix >= 0 || ix <= 3) {
            if (type == 'h') {
                arrs[ix] = new Human();
                msg = "OK";
            } else if (type == 'c') {
                arrs[ix] = new Computer4(ix);
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
            case 2:
                return scoreb;

            case 1:
            case 3:
                return scorer;

            default:
                return -1;
        }
    }

    @Override
    public void setScore(int player, int val) {
        switch (player) {
            case 0:
            case 2:
                scoreb = val;
                break;

            case 1:
            case 3:
                scorer = val;
        }
    }

    @Override
    public void init() {
        super.init();
        for (int i = 1; i < 5; i++) {
            arr[0][i].mrb = Marble.BLUE;
        }
        for (int i = 2; i < 5; i++) {
            arr[1][i].mrb = Marble.BLUE;
        }
        for (int i = 3; i < 5; i++) {
            arr[2][i].mrb = Marble.BLUE;
        }
        for (int i = 1; i < 5; i++) {
            arr[8][8 - i].mrb = Marble.GREEN;
        }
        for (int i = 2; i < 5; i++) {
            arr[7][8 - i].mrb = Marble.GREEN;
        }
        for (int i = 3; i < 5; i++) {
            arr[6][8 - i].mrb = Marble.GREEN;
        }
        for (int i = 1; i < 5; i++) {
            arr[i][0].mrb = Marble.RED;
        }
        for (int i = 2; i < 5; i++) {
            arr[i][1].mrb = Marble.RED;
        }
        for (int i = 3; i < 5; i++) {
            arr[i][2].mrb = Marble.RED;
        }
        for (int i = 1; i < 5; i++) {
            arr[8 - i][8].mrb = Marble.ORANGE;
        }
        for (int i = 2; i < 5; i++) {
            arr[8 - i][7].mrb = Marble.ORANGE;
        }
        for (int i = 3; i < 5; i++) {
            arr[8 - i][6].mrb = Marble.ORANGE;
        }
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
        Marble mp1 = Marble.BLUE, mp2 = Marble.GREEN,
                ma1 = Marble.RED, ma2 = Marble.ORANGE;
        switch (gameturn) {
            case 1:
                mp1 = Marble.RED;
                mp2 = Marble.ORANGE;
                ma1 = Marble.BLUE;
                ma2 = Marble.GREEN;
                break;

            case 2:
                mp1 = Marble.GREEN;
                mp2 = Marble.BLUE;
                ma1 = Marble.RED;
                ma2 = Marble.ORANGE;
                break;

            case 3:
                mp1 = Marble.ORANGE;
                mp2 = Marble.RED;
                ma1 = Marble.BLUE;
                ma2 = Marble.GREEN;
                break;
        }
        if (arr[r2][c2].mrb == ma1 || arr[r2][c2].mrb == ma2) {
            return false;
        }
        r2 += dr;
        c2 += dc;
        if (r2 < 0 || r2 >= DIM_ROWS || c2 < 0 || c2 >= DIM_COLS) {
            return false;
        }
        if (arr[r2][c2].mrb == Marble.EMPTY || arr[r2][c2].mrb == ma1 || arr[r2][c2].mrb == ma2) {
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
        if (arr[r2][c2].mrb == Marble.EMPTY || arr[r2][c2].mrb == ma1 || arr[r2][c2].mrb == ma2) {
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
        Marble mp1 = Marble.BLUE, mp2 = Marble.GREEN,
                ma1 = Marble.RED, ma2 = Marble.ORANGE;
        switch (gameturn) {
            case 1:
                mp1 = Marble.RED;
                mp2 = Marble.ORANGE;
                ma1 = Marble.BLUE;
                ma2 = Marble.GREEN;
                break;

            case 2:
                mp1 = Marble.GREEN;
                mp2 = Marble.BLUE;
                ma1 = Marble.RED;
                ma2 = Marble.ORANGE;
                break;

            case 3:
                mp1 = Marble.ORANGE;
                mp2 = Marble.RED;
                ma1 = Marble.BLUE;
                ma2 = Marble.GREEN;
                break;
        }
        if (arr[r1][c1].mrb != mp1) {
            msg = "Wrong first marble selection!";
            return false;
        }
        if (arr[r2][c2].mrb == mp1 || arr[r2][c2].mrb == mp2) {
            msg = "Wrong field selection!";
            return false;
        }
        int deltar = (int) Math.signum(r2 - r1);
        int deltac = (int) Math.signum(c2 - c1);
        int i, r, c;
        for (r = r1 + deltar, c = c1 + deltac; (r != r2) || (c != c2); r += deltar, c += deltac) {
            if (arr[r][c].mrb != mp1 && arr[r][c].mrb != mp2) {
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
        if (arr[r2][c2].mrb == ma1 || arr[r2][c2].mrb == ma2) {
            powera = 1;
            for (r = r2 + deltar, c = c2 + deltac;; r += deltar, c += deltac) {
                if (!isOnBoard(r, c)) {
                    break;
                }
                if (arr[r][c].mrb == mp1 || arr[r][c].mrb == mp2) {
                    msg = "Cannot push!";
                    return false;
                }
                if (arr[r][c].mrb != ma1 && arr[r][c].mrb != ma2) {
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
                int ii = r - deltar;
                int jj = c - deltac;
                if (gameturn == 0 || gameturn == 2) {
                    if (arr[ii][jj].mrb == Marble.RED) {
                        elimRed++;
                    } else if (arr[ii][jj].mrb == Marble.ORANGE) {
                        elimOrange++;
                    }
                    //scoreb is team (b + g)
                    scoreb++;
                } else if (gameturn == 1 || gameturn == 3) {
                    if (arr[ii][jj].mrb == Marble.BLUE) {
                        elimBlue++;
                    } else if (arr[ii][jj].mrb == Marble.GREEN) {
                        elimGreen++;
                    }
                    scorer++;
                }
            }
        }
        arr[r1][c1].mrb = Marble.EMPTY;
        if (scoreb >= WIN_SCORE) {
            msg = "Blue&Green Wins!";
            return true;
        }
        if (scorer >= WIN_SCORE) {
            msg = "Red&Orange Wins!";
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
        Marble mp1 = Marble.EMPTY, mp2 = Marble.EMPTY;
        switch (gameturn) {
            case 0:
                mp1 = Marble.BLUE;
                mp2 = Marble.GREEN;
                break;

            case 1:
                mp1 = Marble.RED;
                mp2 = Marble.ORANGE;
                break;

            case 2:
                mp1 = Marble.GREEN;
                mp2 = Marble.BLUE;
                break;

            case 3:
                mp1 = Marble.ORANGE;
                mp2 = Marble.RED;
                break;
        }
        return sidemove1(mp1, mp2);
    }

    @Override
    protected boolean sidemove1(Marble mp1, Marble mp2) {
        int deltar = (int) Math.signum(r2 - r1);
        int deltac = (int) Math.signum(c2 - c1);
        int deltar1 = (int) Math.signum(r3 - r1);
        int deltac1 = (int) Math.signum(c3 - c1);
        int cntmp1 = 0;
        for (int r = r1, c = c1; (r != r2 + deltar) || (c != c2 + deltac); r += deltar, c += deltac) {
            if (arr[r][c].mrb != mp1 && arr[r][c].mrb != mp2) {
                msg = "Wrong marble selection!";
                return false;
            }
            if (!isOnBoard(r + deltar1, c + deltac1) || arr[r + deltar1][c + deltac1].mrb != Marble.EMPTY) {
                msg = "Wrong side move direction!";
                return false;
            }
            if (arr[r][c].mrb == mp1) {
                cntmp1++;
            }
        }
        if (cntmp1 == 0) {
            msg = "Wrong marble selection!";
            return false;
        }
        moved.clear();
        for (int r = r1, c = c1; (r != r2 + deltar) || (c != c2 + deltac); r += deltar, c += deltac) {
            Marble aux = arr[r][c].mrb;
            arr[r][c].mrb = Marble.EMPTY;
            arr[r + deltar1][c + deltac1].mrb = aux;
            moved.add(new Position(r + deltar1, c + deltac1));
        }
        aturns[gameturn]++;
        gameturn = (gameturn + 1) % gamemode;
        msg = "OK";
        return true;
    }
}
