
package abalone;

import java.util.*;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.*;

public class AbalonePaintPanel extends JPanel {    
    private final double MARBLESIZE = 0.47;
    private int[] xPoints = null;
    private int[] yPoints = null;
    private int[] xPoints1 = null;
    private int[] yPoints1 = null;
    private int[][] xCenters = null;
    private int[][] yCenters = null;
    private double ddx;
    private double ddy;
    private int rh;
    private int rv;
    private int rh2;
    private int rv2;
    private int dxMouse, dyMouse, xMouse, yMouse;
    private boolean bMouseMove;
    private Color clrMove;
    private Color clr = new Color(0, 150, 150);
    private Color clrbk = new Color(223, 255, 200);
    private int rowMouseStart = -1;
    private int colMouseStart = -1;
    private int irowSelect1 = -1;
    private int icolSelect1 = -1;
    private int irowSelect2 = -1;
    private int icolSelect2 = -1;
    
    public AbalonePaintPanel() {
        setDoubleBuffered(true);
        xCenters = new int[9][9];
        yCenters = new int[9][9];
        bMouseMove = false;
    }
    
    public void setSelect1(int irowSelect1, int icolSelect1) {
        this.irowSelect1 = irowSelect1;
        this.icolSelect1 = icolSelect1;
    }
    
    public void setSelect2(int irowSelect2, int icolSelect2) {
        this.irowSelect2 = irowSelect2;
        this.icolSelect2 = icolSelect2;
    }
    
    public boolean getMouseMove() {
        return bMouseMove;
    }
        
    private void drawGrid(Graphics2D g2d, int[] xp, int[] yp,
        int i1, int j1, int i2, int j2) {
        double dx1 = (xp[j1] - xp[i1]) / 5.0;
        double dy1 = (yp[j1] - yp[i1]) / 5.0;
        double dx2 = (xp[j2] - xp[i2]) / 5.0;
        double dy2 = (yp[j2] - yp[i2]) / 5.0;
        for (int i = 1; i<=4; i++) {
            int x1 = (int)(xp[i1] + i * dx1);
            int y1 = (int)(yp[i1] + i * dy1);
            int x2 = (int)(xp[i2] + i * dx2);
            int y2 = (int)(yp[i2] + i * dy2);
            g2d.drawLine(x1, y1, x2, y2);
        }
    }
    
    protected static final int[] deltacol = new int[]{0, 0, 0, 0, 0, 1, 2, 3, 4};
    protected static final int[] sizecol = new int[]{5, 6, 7, 8, 9, 8, 7, 6, 5};
    
    private void drawCircles(Graphics2D g2d, int x1, int x2, int y,
        int nc, int irow) {
        double dx = (x2 - x1) / (nc + 1.0);
        for (int i = 1; i <= nc; i++) {
            int xc = (int)(x1 + i * dx);
            int xleft = xc - rh;
            int yleft = y - rv;
            g2d.fillOval(xleft, yleft, 2 * rh, 2 * rv);
            xCenters[irow][i-1 + deltacol[irow]] = xc;
            yCenters[irow][i-1 + deltacol[irow]] = y;
        }
    }
    
    private void drawMarble(Graphics2D g2d, int i, int j, Marble mb, int ival) {
        Color c = Board.BLUE1;
        if (ival > -1)
            c = Board.MOVE_BLUE;
        switch (mb) {
            case RED:
                c = Board.RED1;
                if (ival > -1)
                    c = Board.MOVE_RED;
                break;

            case GREEN:
                c = Board.GREEN1;
                if (ival > -1)
                    c = Board.MOVE_GREEN;
                break;

            case ORANGE:
                c = Board.ORANGE1;
                if (ival > -1)
                    c = Board.MOVE_ORANGE;
                break;
        }
        g2d.setColor(c);
        g2d.fillOval(xCenters[i][j] - rh, yCenters[i][j] - rv, 2 * rh, 2 * rv);
        if (ival == 1 || ival == 2){
            c = Board.BLUE1;
            switch (mb) {
                case RED:
                    c = Board.RED1;
                    break;

                case GREEN:
                    c = Board.GREEN1;
                    break;

                case ORANGE:
                    c = Board.ORANGE1;
                    break;
            }
            Font fnt = new Font(Font.DIALOG, Font.PLAIN, 20);
            g2d.setFont(fnt);
            g2d.setColor(c);
            g2d.drawString("" + ival, xCenters[i][j] - rh/4, yCenters[i][j] + rv/4);
        }
    }
    
    private void drawMarble(Graphics2D g2d, int xC, int yC) {
        g2d.setColor(clrMove);
        g2d.fillOval(xC - rh, yC - rv, 2 * rh, 2 * rv);
    }
    
    public void closestMarble(int x, int y, int arr[]) {
        int irow = -1;
        if (y < yCenters[8][8]) {
            if (yCenters[8][8] - y < ddy / 2)
                irow = 8;
        }
        else if (y > yCenters[0][0]) {
            if (y - yCenters[0][0] < ddy / 2)
                irow = 0;
        }
        else {
            irow = 8 - (int)Math.round((y - yCenters[8][8]) / ddy);
        }
        int icol = -1;
        if (irow != -1) {
            if (x < xCenters[irow][deltacol[irow]]) {
                if (xCenters[irow][deltacol[irow]] - x < ddx / 2) {
                    icol = deltacol[irow];
                }
            } else if (x > xCenters[irow][deltacol[irow] + sizecol[irow] - 1]) {
                if (x - xCenters[irow][deltacol[irow] + sizecol[irow] - 1] < ddx / 2) {
                    icol = deltacol[irow] + sizecol[irow] - 1;
                }
            } else {
                icol = deltacol[irow] + (int) Math.round((x - xCenters[irow][deltacol[irow]]) / ddx);
            }
        }
        arr[0] = irow;
        arr[1] = icol;
    }

    public boolean isOnMarble(int x, int y, int arr[]) {
        closestMarble(x, y, arr);
        if (arr[0] == -1 || arr[1] == -1)
            return false;
        int irow = arr[0];
        int icol = arr[1];
        int h = xCenters[irow][icol];
        int v = yCenters[irow][icol];
        return (x - h) * (x - h) / (rh * rh) + (y - v) * (y - v) / (rv * rv) <= 1.0;
    }
        
    public void setMarbleStart(int irowStart, int icolStart, int xM, int yM, Marble mrb) {
        xMouse = xM;
        yMouse = yM;
        dxMouse = xMouse - xCenters[irowStart][icolStart];
        dyMouse = yMouse - yCenters[irowStart][icolStart];
        clrMove = Board.MOVE_BLUE;
        switch (mrb) {
            case RED:
                clrMove = Board.MOVE_RED;
                break;

            case GREEN:
                clrMove = Board.MOVE_GREEN;
                break;

            case ORANGE:
                clrMove = Board.MOVE_ORANGE;
                break;
        }
        rowMouseStart = irowStart;
        colMouseStart = icolStart;
        bMouseMove = true;
    }
    
    public void setMarbleMove(int xM, int yM) {    
        xMouse = xM;
        yMouse = yM;
        int xMouseCenter = xM - dxMouse;
        int yMouseCenter = yM - dyMouse;
        int arr[] = { -1, -1 };
        closestMarble(xMouseCenter, yMouseCenter, arr);
        if (arr[0] == -1 || arr[1] == -1)
            bMouseMove = false;
        else if (Math.abs(rowMouseStart - arr[0]) == 1 && Math.abs(colMouseStart - arr[1]) == 1 &&
            rowMouseStart - arr[0] != colMouseStart - arr[1])
            bMouseMove = false;
        else if (Math.abs(rowMouseStart - arr[0]) > 1 || Math.abs(colMouseStart - arr[1]) > 1)
            bMouseMove = false;
    }
    
    public void setMarbleStop() {
        bMouseMove = false;
        rowMouseStart = -1;
        colMouseStart = -1;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setBackground(clrbk);
        Rectangle r = getBounds();
        int w = r.width - 6;
        int h = r.height - 6;
        int w4 = w / 4 + 4;
        int w34 = 3 * w / 4 + 4;
        int h2 = h / 2 + 4;
        Color clrGray = Board.getInstance().GRAY1;
        g2d.setStroke(new BasicStroke(3));
        xPoints = new int[]{w4, w34, w, w34, w4, 4};
        yPoints = new int[]{4, 4, h2, h, h, h2};
        g2d.setColor(clrGray);
        g2d.fillPolygon(xPoints, yPoints, 6);
        g2d.setColor(clr);
        g2d.drawPolygon(xPoints, yPoints, 6);
        int dx = (int) (0.08 * w);
        int dx2 = (int) (0.5 * dx);
        int dy = (int) (0.08 * h);
        xPoints1 = new int[]{xPoints[0] + dx2, xPoints[1] - dx2, xPoints[2] - dx,
            xPoints[3] - dx2, xPoints[4] + dx2, xPoints[5] + dx};
        yPoints1 = new int[]{yPoints[0] + dy, yPoints[1] + dy, yPoints[2],
            yPoints[3] - dy, yPoints[4] - dy, yPoints[5]};
        g2d.setColor(clrbk);
        g2d.fillPolygon(xPoints1, yPoints1, 6);
        g2d.setColor(clr);
        g2d.drawPolygon(xPoints1, yPoints1, 6);
        g2d.drawPolygon(xPoints1, yPoints1, 6);
        g2d.drawLine(xPoints1[5], yPoints1[5], xPoints1[2], yPoints1[2]);
        g2d.drawLine(xPoints1[0], yPoints1[0], xPoints1[3], yPoints1[3]);
        g2d.drawLine(xPoints1[1], yPoints1[1], xPoints1[4], yPoints1[4]);
        drawGrid(g2d, xPoints1, yPoints1, 5, 4, 2, 3);
        drawGrid(g2d, xPoints1, yPoints1, 5, 0, 2, 1);
        drawGrid(g2d, xPoints1, yPoints1, 0, 1, 3, 2);
        drawGrid(g2d, xPoints1, yPoints1, 0, 5, 3, 4);
        drawGrid(g2d, xPoints1, yPoints1, 1, 0, 4, 5);
        drawGrid(g2d, xPoints1, yPoints1, 1, 2, 4, 3);
        ddx = (xPoints1[1] - xPoints1[0]) / 5.0;
        ddy = (yPoints1[2] - yPoints1[1]) / 5.0;
        rh = (int)(ddx * MARBLESIZE);
        rv = (int)(ddy * MARBLESIZE);
        rh2 = (int)(ddx * MARBLESIZE / 2.0);
        rv2 = (int)(ddy * MARBLESIZE / 2.0);
        int fsz = 25;
        Font fnt = new Font(Font.DIALOG, Font.BOLD, fsz);
        g.setFont(fnt);
        for (int i = 1; i<=5; i++) {
            g.drawString("" + i, (int)(xPoints1[4] + i * ddx), yPoints1[4] + fsz);
        }
        for (int i = 1; i<=4; i++) {
            g.drawString("" + (i + 5), (int)(xPoints1[3] + i * ddx / 2),
                    (int)(yPoints1[3] - i * ddy  + fsz));
        }
        for (int i = 1; i<=5; i++) {
            g.drawString("" + (char)('A' + i - 1), (int)(xPoints1[4] - i * ddx / 2 - fsz),
                    (int)(yPoints1[4] - i * ddy  + 3 * fsz / 4));
        }
        for (int i = 1; i<=4; i++) {
            g.drawString("" + (char)('F' + i - 1), (int)(xPoints1[5] + i * ddx / 2 - 4 * fsz / 3),
                    (int)(yPoints1[5] - i * ddy + fsz / 3));
        }
        g2d.setColor(clrGray);
        drawCircles(g2d, (int) (xPoints1[4] - ddx / 2.0), (int) (xPoints1[3] + ddx / 2.0),
            (int) (yPoints1[4] - ddy), 5, 0);
        drawCircles(g2d, (int) (xPoints1[4] - ddx), (int) (xPoints1[3] + ddx),
            (int) (yPoints1[4] - 2.0 * ddy), 6, 1);
        drawCircles(g2d, (int) (xPoints1[4] - 3.0 * ddx / 2.0), (int) (xPoints1[3] + 3.0 * ddx / 2.0),
            (int) (yPoints1[4] - 3.0 * ddy), 7, 2);
        drawCircles(g2d, (int) (xPoints1[4] - 2.0 * ddx), (int) (xPoints1[3] + 2.0 * ddx),
            (int) (yPoints1[4] - 4.0 * ddy), 8, 3);
        drawCircles(g2d, xPoints1[5], xPoints1[2], yPoints1[5], 9, 4);
        drawCircles(g2d, (int) (xPoints1[0] - 2.0 * ddx), (int) (xPoints1[1] + 2.0 * ddx),
            (int) (yPoints1[0] + 4.0 * ddy), 8, 5);
        drawCircles(g2d, (int) (xPoints1[0] - 3.0 * ddx / 2.0), (int) (xPoints1[1] + 3.0 * ddx / 2.0),
            (int) (yPoints1[0] + 3.0 * ddy), 7, 6);
        drawCircles(g2d, (int) (xPoints1[0] - ddx), (int) (xPoints1[1] + ddx),
            (int) (yPoints1[0] + 2.0 * ddy), 6, 7);
        drawCircles(g2d, (int) (xPoints1[0] - ddx / 2.0), (int) (xPoints1[1] + ddx / 2.0),
            (int) (yPoints1[0] + ddy), 5, 8);
        Board board = Board.getInstance();
        Set<Position> set = Board.getInstance().getMoved();
        for (int i = 0; i < Board.DIM_ROWS; i++) {
            for (int j = 0; j < Board.DIM_COLS; j++) {
                Marble mb = board.getMarble(i, j);
                if (mb == Marble.BLUE || mb == Marble.RED || mb == Marble.GREEN ||
                    mb == Marble.ORANGE) {
                    if (i == irowSelect1 && j == icolSelect1) 
                        drawMarble(g2d, i, j, mb, 1);
                    else if (i == irowSelect2 && j == icolSelect2)
                        drawMarble(g2d, i, j, mb, 2);
                    else if (Board.getInstance().getMoved().contains(new Position(i, j))) {
                        drawMarble(g2d, i, j, mb, 3);
                    }
                    else if (!(i == rowMouseStart && j == colMouseStart))
                        drawMarble(g2d, i, j, mb, -1);
                }
            }
        }
        int eb = Board.getInstance().getElimBlue();
        int er = Board.getInstance().getElimRed();
        if (Board.getInstance().getGamemode() == 2) {
            if (er > 0) {
                g2d.setColor(Board.RED1);
                for (int i = 0; i < er; i++) {
                    g2d.fillOval(i * rh2, 0, rh2-2, rv2-2);
                }
            }
            if (eb > 0) {
                g2d.setColor(Board.BLUE1);
                for (int i = 0; i < eb; i++) {
                    g2d.fillOval(i * rh2, h - rv2, rh2-2, rv2-2);
                }
            }
        }
        else if (Board.getInstance().getGamemode() == 3) {
            int eg = Board.getInstance().getElimGreen();
            if (er > 0) {
                g2d.setColor(Board.RED1);
                for (int i = 0; i < er; i++) {
                    g2d.fillOval(i *rh2, 0, rh2-2, rv2-2);
                }
            }
            if (eb > 0) {
                g2d.setColor(Board.BLUE1);
                for (int i = 0; i < eb; i++) {
                    g2d.fillOval(i * rh2, h - rv2, rh2-2, rv2-2);
                }
            }
            if (eg > 0) {
                g2d.setColor(Board.GREEN1);
                for (int i = 0; i < eg; i++) {
                    g2d.fillOval(w - (i + 1) * rh2, 0, rh2-2, rv2-2);
                }
            }
        }
        else {
            int eg = Board.getInstance().getElimGreen();
            int eo = Board.getInstance().getElimOrange();
            if (eg > 0) {
                g2d.setColor(Board.GREEN1);
                for (int i = 0; i < eg; i++) {
                    g2d.fillOval(i * rh2, 0, rh2-2, rv2-2);
                }
            }
            if (er > 0) {
                g2d.setColor(Board.RED1);
                for (int i = 0; i < er; i++) {
                    g2d.fillOval(i * rh2, h - rv2, rh2-2, rv2-2);
                }
            }
            if (eo > 0) {
                g2d.setColor(Board.ORANGE1);
                for (int i = 0; i < eo; i++) {
                    g2d.fillOval(w - (i + 1) * rh2, 0, rh2-2, rv2-2);
                }
            }
            if (eb > 0) {
                g2d.setColor(Board.BLUE1);
                for (int i = 0; i < eb; i++) {
                    g2d.fillOval(w - (i + 1) * rh2, h - rv2, rh2-2, rv2-2);
                }
            }
        }
        if (bMouseMove) {
            int xMouseCenter = xMouse - dxMouse;
            int yMouseCenter = yMouse - dyMouse;
            drawMarble(g2d, xMouseCenter, yMouseCenter);
        }   
    }
}

