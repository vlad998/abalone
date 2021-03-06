package abalone;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class DlgAnalyzer extends javax.swing.JDialog {

    private AbaloneMainFrame amf;

    private Icon firstIcon;
    private Icon firstIcon1;
    private Icon nextIcon;
    private Icon nextIcon1;
    private Icon playIcon;
    private Icon playIcon1;
    private Icon pauseIcon;
    private Icon pauseIcon1;
    private Icon yieldIcon;
    private Icon takeIcon;

    private final List<String> cmds;
    private int modeold = 0;
    private int mode = 0;
    private int cursor = 0;

    private int playInterval = 1000;
    private Timer playTimer = null;

    private DefaultListModel lstmodel = new DefaultListModel();
    GameState yieldGS = null;

    public DlgAnalyzer(java.awt.Frame parent, final List<String> cmds, int mode, boolean modal) {
        super(parent, modal);
        initComponents();
        amf = (AbaloneMainFrame) parent;
        amf.blockControls();
        btnPause.setEnabled(false);
        firstIcon = new ImageIcon("first.png");
        firstIcon1 = new ImageIcon("first1.png");
        nextIcon = new ImageIcon("next.png");
        nextIcon1 = new ImageIcon("next1.png");
        playIcon = new ImageIcon("play.png");
        playIcon1 = new ImageIcon("play1.png");
        pauseIcon = new ImageIcon("pause.png");
        pauseIcon1 = new ImageIcon("pause1.png");
        yieldIcon = new ImageIcon("yield.png");
        takeIcon = new ImageIcon("take.png");
        this.cmds = cmds;
        this.mode = mode;
        modeold = Board.getInstance().getGamemode();
        Board.CreateBoard(mode);
        playTimer = new Timer(playInterval, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnNextActionPerformed(null);
                if (cursor == cmds.size()) {
                    btnPauseActionPerformed(null);
                }
            }
        });
        //
        amf.resetAnalyze();
        amf.repaint1();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pbProgress = new javax.swing.JProgressBar();
        btnPause = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstSaved = new javax.swing.JList<>();
        lblSaved = new javax.swing.JLabel();
        btnFirst = new javax.swing.JButton();
        spnTi = new javax.swing.JSpinner();
        lblTi = new javax.swing.JLabel();
        btnSaveState = new javax.swing.JButton();
        btnYieldControl = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Game Analyzer");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/abalone/pause1.png"))); // NOI18N
        btnPause.setToolTipText("Pause");
        btnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPauseActionPerformed(evt);
            }
        });

        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/abalone/play.png"))); // NOI18N
        btnPlay.setToolTipText("Play");
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/abalone/next.png"))); // NOI18N
        btnNext.setToolTipText("Next");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        lstSaved.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstSavedMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(lstSaved);

        lblSaved.setText("Saved States");

        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/abalone/first.png"))); // NOI18N
        btnFirst.setToolTipText("First");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        spnTi.setModel(new javax.swing.SpinnerNumberModel(1, 1, 5, 1));

        lblTi.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTi.setText("Time Interval");

        btnSaveState.setText("Save State");
        btnSaveState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveStateActionPerformed(evt);
            }
        });

        btnYieldControl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/abalone/yield.png"))); // NOI18N
        btnYieldControl.setToolTipText("Yield/Retake Control");
        btnYieldControl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYieldControlActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pbProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(btnSaveState)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTi, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnTi, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnFirst)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnPause))
                            .addComponent(lblSaved, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPlay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnYieldControl)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(pbProgress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPause, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnYieldControl, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveState)
                    .addComponent(lblTi, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnTi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSaved, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        cursor = 0;
        pbProgress.setValue(0);
        Board.CreateBoard(mode);
        amf.resetAnalyze();
        amf.showTurn(false);
        amf.repaint1();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPauseActionPerformed
        playTimer.stop();
        btnPlay.setEnabled(true);
        btnPlay.setIcon(playIcon);
        btnPause.setEnabled(false);
        btnPause.setIcon(pauseIcon1);
        btnFirst.setEnabled(true);
        btnFirst.setIcon(firstIcon);
        btnNext.setEnabled(true);
        btnNext.setIcon(nextIcon);
        lstSaved.setEnabled(true);
        spnTi.setEnabled(true);
        btnSaveState.setEnabled(true);
    }//GEN-LAST:event_btnPauseActionPerformed

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        btnPlay.setEnabled(false);
        btnPlay.setIcon(playIcon1);
        btnPause.setEnabled(true);
        btnPause.setIcon(pauseIcon);
        btnFirst.setEnabled(false);
        btnFirst.setIcon(firstIcon1);
        btnNext.setEnabled(false);
        btnNext.setIcon(nextIcon1);
        lstSaved.setEnabled(false);
        spnTi.setEnabled(false);
        btnSaveState.setEnabled(false);
        String sval = spnTi.getValue().toString();
        int val = Integer.parseInt(sval);
        playTimer.setDelay(1000 * val);
        playTimer.start();
    }//GEN-LAST:event_btnPlayActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        if (cursor < cmds.size()) {
            amf.executeCommand(cmds.get(cursor), null, false, true);
        }
        cursor++;
        if (cursor > cmds.size()) {
            cursor = cmds.size();
        }
        int val = (int) (100.0 * cursor / (double) cmds.size());
        pbProgress.setValue(val);
    }//GEN-LAST:event_btnNextActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Board.CreateBoard(modeold);
        amf.setMode(modeold);
        amf.resetAnalyze();
        amf.unblockControls();
    }//GEN-LAST:event_formWindowClosing

    private long startTime = -1;

    private final int DBLCLK_INTERVAL = 700;

    private void lstSavedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstSavedMouseClicked
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        } else {
            long finishTime = System.currentTimeMillis();
            long timeElapsed = finishTime - startTime;
            if (timeElapsed < DBLCLK_INTERVAL) {
                int ix = lstSaved.getSelectedIndex();
                GameState gs = (GameState) lstmodel.get(ix);
                cursor = gs.cursor;
                int val = (int) (100.0 * cursor / (double) cmds.size());
                pbProgress.setValue(val);
                gs.setState();
                amf.setState();
            } else {
                startTime = finishTime;
            }
        }
    }//GEN-LAST:event_lstSavedMouseClicked

    private class GameState {

        Field arr[][] = null;
        int[] aturns = null;
        int gameturn = 0;
        int scoreb = 0;
        int scorer = 0;
        int scoreg = 0;
        int elimBlue = 0;
        int elimRed = 0;
        int elimGreen = 0;
        int elimOrange = 0;
        int cursor;
        String desc;

        GameState(String desc, int cursor) {
            this.desc = desc;
            this.cursor = cursor;
            int i, j;
            Board brd = Board.getInstance();
            aturns = new int[Board.N_PLAYERS];
            for (i = 0; i < Board.N_PLAYERS; i++) {
                aturns[i] = brd.getTurns(i);
            }
            arr = new Field[Board.DIM_ROWS][Board.DIM_COLS];
            for (i = 0; i < Board.DIM_ROWS; i++) {
                for (j = 0; j < Board.DIM_COLS; j++) {
                    arr[i][j] = new Field(brd.getField(i, j));
                }
            }
            gameturn = brd.getGameturn();
            scoreb = brd.getScore(0);
            scorer = brd.getScore(1);
            scoreg = brd.getScore(2);
            elimBlue = brd.getElimBlue();
            elimRed = brd.getElimRed();
            elimGreen = brd.getElimGreen();
            elimOrange = brd.getElimOrange();
        }

        void setState() {
            Board brd = Board.getInstance();
            int i, j;
            for (i = 0; i < Board.N_PLAYERS; i++) {
                brd.setTurns(i, aturns[i]);
            }
            for (i = 0; i < Board.DIM_ROWS; i++) {
                for (j = 0; j < Board.DIM_COLS; j++) {
                    brd.setField(i, j, arr[i][j]);
                }
            }
            brd.resetMoved();
            brd.setGameturn(gameturn);
            brd.setScore(0, scoreb);
            brd.setScore(1, scorer);
            brd.setScore(2, scoreg);
            brd.setElimBlue(elimBlue);
            brd.setElimRed(elimRed);
            brd.setElimGreen(elimGreen);
            brd.setElimOrange(elimOrange);
        }

        @Override
        public String toString() {
            return desc;
        }
    }

    private void btnSaveStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveStateActionPerformed
        String desc = "";
        int i;
        while (true) {
            desc = JOptionPane.showInputDialog(this, "Enter state description",
                    "State description", JOptionPane.QUESTION_MESSAGE);
            for (i = 0; i < lstmodel.size(); i++) {
                GameState gs = (GameState) lstmodel.get(i);
                if (desc.equals(gs.desc)) {
                    break;
                }
            }
            if (i < lstmodel.size()) {
                JOptionPane.showMessageDialog(this, "State description already exists!");
            } else {
                break;
            }
        }
        lstmodel.addElement(new GameState(desc, cursor));
        lstSaved.setModel(lstmodel);
    }//GEN-LAST:event_btnSaveStateActionPerformed

    private boolean bYield = false;
    private boolean bIsRunningO = false;
    private boolean bPlayO = false;
    private boolean bPauseO = false;
    private boolean bFirstO = false;
    private boolean bNextO = false;
    private boolean bSavedO = false;
    private boolean bTIO = false;
    private boolean bSaveStateO = false;

    private void btnYieldControlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYieldControlActionPerformed
        bYield = !bYield;
        if (bYield) {
            bIsRunningO = playTimer.isRunning();
            if (bIsRunningO) {
                playTimer.stop();
            }
            bPlayO = btnPlay.isEnabled();
            btnPlay.setEnabled(false);
            btnPlay.setIcon(playIcon1);
            bPauseO = btnPause.isEnabled();
            btnPause.setEnabled(false);
            btnPause.setIcon(pauseIcon1);
            bFirstO = btnFirst.isEnabled();
            btnFirst.setEnabled(false);
            btnFirst.setIcon(firstIcon1);
            bNextO = btnNext.isEnabled();
            btnNext.setEnabled(false);
            btnNext.setIcon(nextIcon1);
            bSavedO = lstSaved.isEnabled();
            lstSaved.setEnabled(false);
            bTIO = spnTi.isEnabled();
            spnTi.setEnabled(false);
            bSaveStateO = btnSaveState.isEnabled();
            btnSaveState.setEnabled(false);
            pbProgress.setEnabled(false);
            btnYieldControl.setIcon(takeIcon);
            yieldGS = new GameState("", cursor);
            amf.setbPlayAnalyzer(true);
        } else {
            cursor = yieldGS.cursor;
            int val = (int) (100.0 * cursor / (double) cmds.size());
            pbProgress.setValue(val);
            yieldGS.setState();
            amf.setState();
            btnYieldControl.setIcon(yieldIcon);
            if (bIsRunningO) {
                playTimer.start();
            }
            if (bPlayO) {
                btnPlay.setEnabled(true);
                btnPlay.setIcon(playIcon);
            }
            if (bPauseO) {
                btnPause.setEnabled(true);
                btnPause.setIcon(pauseIcon);
            }
            if (bFirstO) {
                btnFirst.setEnabled(true);
                btnFirst.setIcon(firstIcon);
            }
            if (bNextO) {
                btnNext.setEnabled(true);
                btnNext.setIcon(nextIcon);
            }
            if (bSavedO) {
                lstSaved.setEnabled(true);
            }
            if (bTIO) {
                spnTi.setEnabled(true);
            }
            if (bSaveStateO) {
                btnSaveState.setEnabled(true);
            }
            pbProgress.setEnabled(true);
            amf.setbPlayAnalyzer(false);
        }
    }//GEN-LAST:event_btnYieldControlActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnSaveState;
    private javax.swing.JButton btnYieldControl;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSaved;
    private javax.swing.JLabel lblTi;
    private javax.swing.JList<String> lstSaved;
    private javax.swing.JProgressBar pbProgress;
    private javax.swing.JSpinner spnTi;
    // End of variables declaration//GEN-END:variables
}
