package abalone;

import java.awt.Color;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;
import javax.swing.SwingUtilities;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AbaloneMainFrame extends javax.swing.JFrame {

    private final static int ONE_SECOND = 1000;
    private final static Color CLR_INACTIVE_SCORE = new Color(240, 240, 240);
    private final static Color CLR_BLUE_SCORE = new Color(0, 0, 255);
    private final static Color CLR_RED_SCORE = new Color(255, 0, 0);
    private final static Color CLR_GREEN_SCORE = new Color(0, 200, 0);
    private final static Color CLR_ORANGE_SCORE = new Color(255, 165, 0);

    private AbalonePaintPanel abaloneBoard = null;
    private Timer timer1 = null;
    private Timer timer2 = null;
    private Timer timer3 = null;
    private Timer timer4 = null;
    private int time1 = 0;
    private int time2 = 0;
    private int time3 = 0;
    private int time4 = 0;

    private String strGame = "";

    private AudioInputStream audioStreamMove, audioStreamCapture, audioStreamWin;

    public AbaloneMainFrame() {
        initComponents();
        audioStreamMove = createStream("abalone_move.wav");
        audioStreamCapture = createStream("abalone_capture.wav");
        audioStreamWin = createStream("abalone_win.wav");
        Board2.CreateBoard();
        ButtonGroup grpPlayers = new ButtonGroup();
        grpPlayers.add(radTwoPlayers);
        grpPlayers.add(radThreePlayers);
        grpPlayers.add(radFourPlayers);
        JFormattedTextField tf = ((JSpinner.DefaultEditor) spnTurns.getEditor()).getTextField();
        tf.setEditable(false);
        int maxturns = Board.getMaxturns();
        tf = ((JSpinner.DefaultEditor) spnTime.getEditor()).getTextField();
        tf.setEditable(false);
        int maxtime = Board.getMaxtime();
        lblScore1.setBackground(CLR_INACTIVE_SCORE);
        lblScore1.setText("");
        lblScore2.setBackground(CLR_INACTIVE_SCORE);
        lblScore2.setText("");
        lblScore3.setBackground(CLR_INACTIVE_SCORE);
        lblScore3.setText("");
        lblScore4.setBackground(CLR_INACTIVE_SCORE);
        lblScore4.setText("");
        spnTurns.setValue(maxturns);
        spnTime.setValue(maxtime);
        tbtnPlayer3.setEnabled(false);
        tbtnPlayer4.setEnabled(false);
        timer1 = new Timer(ONE_SECOND, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                time1++;
                lblScore1.setText(Integer.toString(Board.getInstance().getScore(0))
                        + "/" + Board.getInstance().getTurns(0) + "/" + time1);
                if (Board.getInstance().gameturn == 0 && !Board.getInstance().isHuman(0)) {
                    if (client == null || (client != null && !DlgConfigRemote.getBlueRemote())) {
                        String cmd = Board.getInstance().nextCommand();
                        executeCommand(cmd, null, false, false);
                    }
                }
            }
        });
        timer2 = new Timer(ONE_SECOND, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                time2++;
                lblScore2.setText(Integer.toString(Board.getInstance().getScore(1))
                        + "/" + Board.getInstance().getTurns(1) + "/" + time2);
                if (Board.getInstance().gameturn == 1 && !Board.getInstance().isHuman(1)) {
                    if (client == null || (client != null && !DlgConfigRemote.getRedRemote())) {
                        String cmd = Board.getInstance().nextCommand();
                        executeCommand(cmd, null, false, false);
                    }
                }
            }
        });
        timer3 = new Timer(ONE_SECOND, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                time3++;
                lblScore3.setText(Integer.toString(Board.getInstance().getScore(2))
                        + "/" + Board.getInstance().getTurns(2) + "/" + time3);
                if (Board.getInstance().gameturn == 2 && !Board.getInstance().isHuman(2)) {
                    if (client == null || (client != null && !DlgConfigRemote.getGreenRemote())) {
                        String cmd = Board.getInstance().nextCommand();
                        executeCommand(cmd, null, false, false);
                    }
                }
            }
        });
        timer4 = new Timer(ONE_SECOND, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                time4++;
                lblScore4.setText(Integer.toString(Board.getInstance().getScore(3))
                        + "/" + Board.getInstance().getTurns(3) + "/" + time4);
                if (Board.getInstance().gameturn == 3 && !Board.getInstance().isHuman(3)) {
                    if (client == null || (client != null && !DlgConfigRemote.getOrangeRemote())) {
                        String cmd = Board.getInstance().nextCommand();
                        executeCommand(cmd, null, false, false);
                    }
                }
            }
        });
        pnlPlayer1.setBackground(Board.getInstance().GRAY1);
        pnlPlayer2.setBackground(Board.getInstance().GRAY1);
        pnlPlayer3.setBackground(Board.getInstance().GRAY1);
        pnlPlayer4.setBackground(Board.getInstance().GRAY1);
        setLocationRelativeTo(null);
        abaloneBoard = (AbalonePaintPanel) pnBoard;
        abaloneBoard.repaint();
    }

    private Timer tmClient = null;
    private AbaloneClient client = null;

    private boolean createClient(String msg) {
        client = new AbaloneClient(DlgConfigRemote.getServerIP());
        if (client.execute()) {
            tmClient = new Timer(100, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (!client.isMsgQueueEmpty()) {
                        String msg = client.pollMsgQueue();
                        if (msg != null) {
                            String[] arrs = msg.split(":");
                            String command = arrs[0];
                            if (command.equals("STOP")) {
                                txtMessage.setText("Disconnected from Server!");
                                bStarted = true;
                                btnStart.setEnabled(true);
                                btnStartActionPerformed(null);
                            } else if (command.equals("START")) {
                                btnStart.setEnabled(true);
                                btnStartActionPerformed(null);
                            } else if (command.equals("USER")) {
                                String name = arrs[1];
                                txtMessage.setText("Connected to User: " + name);
                            } else if (command.equals("MOVE")) {
                                String cmd = arrs[1];
                                executeCommand(cmd, null, false, false);
                            } else if (command.equals("MAXTURNS")) {
                                String sMaxTurns = arrs[1];
                                int maxTurns = Integer.parseInt(sMaxTurns);
                                spnTurns.setValue(maxTurns);
                                Board.getInstance().setMaxturns(maxTurns);
                            } else if (command.equals("MAXTIME")) {
                                String sMaxTime = arrs[1];
                                int maxTime = Integer.parseInt(sMaxTime);
                                spnTime.setValue(maxTime);
                                Board.getInstance().setMaxtime(maxTime);
                            }
                        }
                    }
                }
            });

            tmClient.start();
            client.sendMessage(msg);
            btnStart.setEnabled(false);
            return true;
        } else {
            client = null;
            return false;
        }
    }

    private void stopClient() {
        client.stop();
        client = null;
        tmClient.stop();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnStart = new javax.swing.JButton();
        lblScore1 = new javax.swing.JLabel();
        spnTurns = new javax.swing.JSpinner();
        radTwoPlayers = new javax.swing.JRadioButton();
        radThreePlayers = new javax.swing.JRadioButton();
        radFourPlayers = new javax.swing.JRadioButton();
        tbtnPlayer1 = new javax.swing.JToggleButton();
        tbtnPlayer2 = new javax.swing.JToggleButton();
        tbtnPlayer3 = new javax.swing.JToggleButton();
        tbtnPlayer4 = new javax.swing.JToggleButton();
        txtMessage = new javax.swing.JTextField();
        lblScore2 = new javax.swing.JLabel();
        lblScore3 = new javax.swing.JLabel();
        lblScore4 = new javax.swing.JLabel();
        pnBoard = new AbalonePaintPanel();
        lblTurn = new javax.swing.JLabel();
        pnlPlayer1 = new javax.swing.JPanel();
        pnlPlayer2 = new javax.swing.JPanel();
        pnlPlayer3 = new javax.swing.JPanel();
        pnlPlayer4 = new javax.swing.JPanel();
        spnTime = new javax.swing.JSpinner();
        lblTurns = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        mbMenuBar = new javax.swing.JMenuBar();
        mnFile = new javax.swing.JMenu();
        miOpen = new javax.swing.JMenuItem();
        miSaveAs = new javax.swing.JMenuItem();
        miExit = new javax.swing.JMenuItem();
        mnHelp = new javax.swing.JMenu();
        miHelp = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Abalone");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        btnStart.setText("Start");
        btnStart.setToolTipText("Start Playing!!");
        btnStart.setBorder(null);
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        lblScore1.setBackground(new java.awt.Color(0, 0, 255));
        lblScore1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblScore1.setForeground(new java.awt.Color(0, 0, 255));
        lblScore1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblScore1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        spnTurns.setModel(new javax.swing.SpinnerNumberModel(50, 20, 150, 10));
        spnTurns.setToolTipText("Number of Turns");
        spnTurns.setEditor(new javax.swing.JSpinner.NumberEditor(spnTurns, ""));
        spnTurns.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnTurnsStateChanged(evt);
            }
        });

        radTwoPlayers.setSelected(true);
        radTwoPlayers.setText("2 Players");
        radTwoPlayers.setToolTipText("2 Players Mode");
        radTwoPlayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radTwoPlayersActionPerformed(evt);
            }
        });

        radThreePlayers.setText("3 Players");
        radThreePlayers.setToolTipText("3 Players Mode");
        radThreePlayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radThreePlayersActionPerformed(evt);
            }
        });

        radFourPlayers.setText("4 Players");
        radFourPlayers.setToolTipText("4 Players Mode");
        radFourPlayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radFourPlayersActionPerformed(evt);
            }
        });

        tbtnPlayer1.setBackground(new java.awt.Color(0, 0, 255));
        tbtnPlayer1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tbtnPlayer1.setText("Player 1");
        tbtnPlayer1.setToolTipText("Toggle to Computer");
        tbtnPlayer1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbtnPlayer1MouseClicked(evt);
            }
        });
        tbtnPlayer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbtnPlayer1ActionPerformed(evt);
            }
        });

        tbtnPlayer2.setBackground(new java.awt.Color(255, 0, 0));
        tbtnPlayer2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tbtnPlayer2.setText("Player 2");
        tbtnPlayer2.setToolTipText("Toggle to Computer");
        tbtnPlayer2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbtnPlayer2MouseClicked(evt);
            }
        });
        tbtnPlayer2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbtnPlayer2ActionPerformed(evt);
            }
        });

        tbtnPlayer3.setBackground(new java.awt.Color(0, 255, 0));
        tbtnPlayer3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tbtnPlayer3.setText("Player 3");
        tbtnPlayer3.setToolTipText("Toggle to Computer");
        tbtnPlayer3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbtnPlayer3MouseClicked(evt);
            }
        });
        tbtnPlayer3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbtnPlayer3ActionPerformed(evt);
            }
        });

        tbtnPlayer4.setBackground(new java.awt.Color(255, 165, 0));
        tbtnPlayer4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tbtnPlayer4.setText("Player 4");
        tbtnPlayer4.setToolTipText("Toggle to Computer");
        tbtnPlayer4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbtnPlayer4MouseClicked(evt);
            }
        });
        tbtnPlayer4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbtnPlayer4ActionPerformed(evt);
            }
        });

        txtMessage.setEditable(false);
        txtMessage.setToolTipText("Messages");

        lblScore2.setBackground(new java.awt.Color(255, 0, 0));
        lblScore2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblScore2.setForeground(new java.awt.Color(255, 0, 0));
        lblScore2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblScore2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblScore3.setBackground(new java.awt.Color(0, 200, 0));
        lblScore3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblScore3.setForeground(new java.awt.Color(0, 170, 0));
        lblScore3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblScore3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblScore4.setBackground(new java.awt.Color(255, 165, 0));
        lblScore4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblScore4.setForeground(new java.awt.Color(255, 165, 0));
        lblScore4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblScore4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pnBoard.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pnBoard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnBoard.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnBoardMouseDragged(evt);
            }
        });
        pnBoard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnBoardMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnBoardMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout pnBoardLayout = new javax.swing.GroupLayout(pnBoard);
        pnBoard.setLayout(pnBoardLayout);
        pnBoardLayout.setHorizontalGroup(
            pnBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnBoardLayout.setVerticalGroup(
            pnBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 523, Short.MAX_VALUE)
        );

        lblTurn.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTurn.setText("Turn:");

        pnlPlayer1.setBackground(new java.awt.Color(200, 200, 200));

        javax.swing.GroupLayout pnlPlayer1Layout = new javax.swing.GroupLayout(pnlPlayer1);
        pnlPlayer1.setLayout(pnlPlayer1Layout);
        pnlPlayer1Layout.setHorizontalGroup(
            pnlPlayer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );
        pnlPlayer1Layout.setVerticalGroup(
            pnlPlayer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pnlPlayer2.setBackground(new java.awt.Color(200, 200, 200));

        javax.swing.GroupLayout pnlPlayer2Layout = new javax.swing.GroupLayout(pnlPlayer2);
        pnlPlayer2.setLayout(pnlPlayer2Layout);
        pnlPlayer2Layout.setHorizontalGroup(
            pnlPlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );
        pnlPlayer2Layout.setVerticalGroup(
            pnlPlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pnlPlayer3.setBackground(new java.awt.Color(200, 200, 200));

        javax.swing.GroupLayout pnlPlayer3Layout = new javax.swing.GroupLayout(pnlPlayer3);
        pnlPlayer3.setLayout(pnlPlayer3Layout);
        pnlPlayer3Layout.setHorizontalGroup(
            pnlPlayer3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );
        pnlPlayer3Layout.setVerticalGroup(
            pnlPlayer3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pnlPlayer4.setBackground(new java.awt.Color(200, 200, 200));

        javax.swing.GroupLayout pnlPlayer4Layout = new javax.swing.GroupLayout(pnlPlayer4);
        pnlPlayer4.setLayout(pnlPlayer4Layout);
        pnlPlayer4Layout.setHorizontalGroup(
            pnlPlayer4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );
        pnlPlayer4Layout.setVerticalGroup(
            pnlPlayer4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        spnTime.setModel(new javax.swing.SpinnerNumberModel(300, 60, 1800, 60));
        spnTime.setToolTipText("Time Limit");
        spnTime.setEditor(new javax.swing.JSpinner.NumberEditor(spnTime, ""));
        spnTime.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnTimeStateChanged(evt);
            }
        });

        lblTurns.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTurns.setText("Turns:");

        lblTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTime.setText("Time:");

        mnFile.setText("File");

        miOpen.setText("Open...");
        miOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miOpenActionPerformed(evt);
            }
        });
        mnFile.add(miOpen);

        miSaveAs.setText("Save As...");
        miSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSaveAsActionPerformed(evt);
            }
        });
        mnFile.add(miSaveAs);

        miExit.setText("Exit");
        miExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExitActionPerformed(evt);
            }
        });
        mnFile.add(miExit);

        mbMenuBar.add(mnFile);

        mnHelp.setText("Help");

        miHelp.setText("Help");
        miHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miHelpActionPerformed(evt);
            }
        });
        mnHelp.add(miHelp);

        mbMenuBar.add(mnHelp);

        setJMenuBar(mbMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtMessage))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblScore1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblScore2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblScore3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblScore4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(radTwoPlayers)
                                .addGap(18, 18, 18)
                                .addComponent(radThreePlayers)
                                .addGap(18, 18, 18)
                                .addComponent(radFourPlayers))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblTurns, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(spnTurns, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(spnTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblTurn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(tbtnPlayer1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlPlayer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tbtnPlayer2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlPlayer2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tbtnPlayer3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlPlayer3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlPlayer4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tbtnPlayer4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pnBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tbtnPlayer4)
                            .addComponent(tbtnPlayer3)
                            .addComponent(tbtnPlayer2)
                            .addComponent(tbtnPlayer1)
                            .addComponent(radFourPlayers)
                            .addComponent(radThreePlayers)
                            .addComponent(radTwoPlayers))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pnlPlayer2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblTurn)
                                .addComponent(spnTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spnTurns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTurns)
                                .addComponent(lblTime))
                            .addComponent(pnlPlayer1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlPlayer4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlPlayer3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblScore3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblScore2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                            .addComponent(lblScore1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(lblScore4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        txtMessage.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private AudioInputStream createStream(String audioFilePath) {
        AudioInputStream audioStream = null;
        try {
            File audioFile = new File(audioFilePath);
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            audioStream.mark((int) audioFile.length() + 1);
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error getting the audio stream.");
            ex.printStackTrace();
        }
        return audioStream;
    }

    private void playMove() {
        try {
            audioStreamMove.reset();
            AudioFormat format = audioStreamMove.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStreamMove);
            audioClip.start();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
    }

    private void playCapture() {
        try {
            audioStreamCapture.reset();
            AudioFormat format = audioStreamCapture.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStreamCapture);
            audioClip.start();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
    }

    private void playWin() {
        try {
            audioStreamWin.reset();
            AudioFormat format = audioStreamWin.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStreamWin);
            audioClip.start();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
    }

    public void resetAnalyze() {
        time1 = 0;
        time2 = 0;
        time3 = 0;
        time4 = 0;
        lblScore1.setText("");
        lblScore2.setText("");
        lblScore3.setText("");
        lblScore4.setText("");
        pnlPlayer1.setBackground(Board.getInstance().GRAY1);
        pnlPlayer2.setBackground(Board.getInstance().GRAY1);
        pnlPlayer3.setBackground(Board.getInstance().GRAY1);
        pnlPlayer4.setBackground(Board.getInstance().GRAY1);
        txtMessage.setText("");
    }

    private void miHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miHelpActionPerformed
        String url = "https://abaloneonline.wordpress.com/game-rules/";
        OpenBrowser.openURL(url);
    }//GEN-LAST:event_miHelpActionPerformed

    private void miExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure?");
        if (answer == JOptionPane.YES_OPTION) {
            if (client != null) {
                stopClient();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    System.out.println("Error in CheckersMainForm: " + ie.getMessage());
                    ie.printStackTrace();
                }
            }
            System.exit(0);
        }
    }//GEN-LAST:event_miExitActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure?");
        if (answer == JOptionPane.YES_OPTION) {
            if (client != null) {
                stopClient();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    System.out.println("Error in CheckersMainForm: " + ie.getMessage());
                    ie.printStackTrace();
                }
            }
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }//GEN-LAST:event_formWindowClosing

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }//GEN-LAST:event_formWindowOpened

    boolean tbtnPlayer1Old;
    boolean tbtnPlayer2Old;
    boolean tbtnPlayer3Old;
    boolean tbtnPlayer4Old;

    void blockControls() {
        btnStart.setEnabled(false);
        radTwoPlayers.setEnabled(false);
        radThreePlayers.setEnabled(false);
        radFourPlayers.setEnabled(false);
        spnTurns.setEnabled(false);
        spnTime.setEnabled(false);
        tbtnPlayer1Old = tbtnPlayer1.isEnabled();
        tbtnPlayer2Old = tbtnPlayer2.isEnabled();
        tbtnPlayer3Old = tbtnPlayer3.isEnabled();
        tbtnPlayer4Old = tbtnPlayer4.isEnabled();
        tbtnPlayer1.setEnabled(false);
        tbtnPlayer2.setEnabled(false);
        tbtnPlayer3.setEnabled(false);
        tbtnPlayer4.setEnabled(false);
        miOpen.setEnabled(false);
        miSaveAs.setEnabled(false);
    }

    void unblockControls() {
        btnStart.setEnabled(true);
        radTwoPlayers.setEnabled(true);
        radThreePlayers.setEnabled(true);
        radFourPlayers.setEnabled(true);
        spnTurns.setEnabled(true);
        spnTime.setEnabled(true);
        tbtnPlayer1.setEnabled(tbtnPlayer1Old);
        tbtnPlayer2.setEnabled(tbtnPlayer2Old);
        tbtnPlayer3.setEnabled(tbtnPlayer3Old);
        tbtnPlayer4.setEnabled(tbtnPlayer4Old);
        miOpen.setEnabled(true);
        miSaveAs.setEnabled(true);
    }

    private void spnTurnsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnTurnsStateChanged
        String sval = spnTurns.getValue().toString();
        int ival = Integer.parseInt(sval);
        Board.setMaxturns(ival);
    }//GEN-LAST:event_spnTurnsStateChanged

    public void setMode(int mode) {
        if (mode == 2) {
            radTwoPlayers.setSelected(true);
            radTwoPlayersActionPerformed(null);
        } else if (mode == 3) {
            radThreePlayers.setSelected(true);
            radThreePlayersActionPerformed(null);
        } else {
            radFourPlayers.setSelected(true);
            radFourPlayersActionPerformed(null);
        }
    }

    private void radTwoPlayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radTwoPlayersActionPerformed
        Board2.CreateBoard();
        time1 = time2 = time3 = time4 = 0;
        tbtnPlayer3.setEnabled(false);
        tbtnPlayer4.setEnabled(false);
        abaloneBoard.repaint();
    }//GEN-LAST:event_radTwoPlayersActionPerformed

    private void radThreePlayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radThreePlayersActionPerformed
        Board3.CreateBoard();
        time1 = time2 = time3 = time4 = 0;
        tbtnPlayer3.setEnabled(true);
        tbtnPlayer4.setEnabled(false);
        abaloneBoard.repaint();
    }//GEN-LAST:event_radThreePlayersActionPerformed

    private void radFourPlayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radFourPlayersActionPerformed
        Board4.CreateBoard();
        time1 = time2 = time3 = time4 = 0;
        tbtnPlayer3.setEnabled(true);
        tbtnPlayer4.setEnabled(true);
        abaloneBoard.repaint();
    }//GEN-LAST:event_radFourPlayersActionPerformed

    private void tbtnPlayer1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtnPlayer1ActionPerformed
        if (tbtnPlayer1.isSelected()) {
            tbtnPlayer1.setToolTipText("Toggle to Human");
            tbtnPlayer1.setBackground(Board.BLUE1);
        } else {
            tbtnPlayer1.setToolTipText("Toggle to Computer");
            tbtnPlayer1.setBackground(Color.blue);
        }
    }//GEN-LAST:event_tbtnPlayer1ActionPerformed

    private void tbtnPlayer2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtnPlayer2ActionPerformed
        if (tbtnPlayer2.isSelected()) {
            tbtnPlayer2.setToolTipText("Toggle to Human");
            tbtnPlayer2.setBackground(Board.RED1);
        } else {
            tbtnPlayer2.setToolTipText("Toggle to Computer");
            tbtnPlayer2.setBackground(Color.red);
        }
    }//GEN-LAST:event_tbtnPlayer2ActionPerformed

    private void tbtnPlayer3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtnPlayer3ActionPerformed
        if (tbtnPlayer3.isSelected()) {
            tbtnPlayer3.setToolTipText("Toggle to Human");
            tbtnPlayer3.setBackground(Board.GREEN1);
        } else {
            tbtnPlayer3.setToolTipText("Toggle to Computer");
            tbtnPlayer3.setBackground(Color.green);
        }
    }//GEN-LAST:event_tbtnPlayer3ActionPerformed

    private void tbtnPlayer4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbtnPlayer4ActionPerformed
        if (tbtnPlayer4.isSelected()) {
            tbtnPlayer4.setToolTipText("Toggle to Human");
            tbtnPlayer4.setBackground(Board.ORANGE1);
        } else {
            tbtnPlayer4.setToolTipText("Toggle to Computer");
            tbtnPlayer4.setBackground(Color.orange);
        }
    }//GEN-LAST:event_tbtnPlayer4ActionPerformed

    public void repaint1() {
        abaloneBoard.repaint();
    }

    public void setState() {
        int mode = Board.getInstance().getGamemode();
        if (mode >= 2) {
            lblScore1.setText(Integer.toString(Board.getInstance().getScore(0))
                    + "/" + Board.getInstance().getTurns(0) + "/" + time1);
            lblScore2.setText(Integer.toString(Board.getInstance().getScore(1))
                    + "/" + Board.getInstance().getTurns(1) + "/" + time2);
        }
        if (mode >= 3) {
            lblScore3.setText(Integer.toString(Board.getInstance().getScore(2))
                    + "/" + Board.getInstance().getTurns(2) + "/" + time3);
        }
        if (mode == 4) {
            lblScore4.setText(Integer.toString(Board.getInstance().getScore(3))
                    + "/" + Board.getInstance().getTurns(3) + "/" + time4);
        }
        pnlPlayer1.setBackground(Board.getInstance().GRAY1);
        pnlPlayer2.setBackground(Board.getInstance().GRAY1);
        pnlPlayer3.setBackground(Board.getInstance().GRAY1);
        pnlPlayer4.setBackground(Board.getInstance().GRAY1);
        int gameturn = Board.getInstance().getGameturn();
        switch (gameturn) {
            case 0:
                pnlPlayer1.setBackground(Board.getInstance().MOVE_BLUE);
                break;

            case 1:
                pnlPlayer2.setBackground(Board.getInstance().MOVE_RED);
                break;

            case 2:
                pnlPlayer3.setBackground(Board.getInstance().MOVE_GREEN);
                break;

            case 3:
                pnlPlayer4.setBackground(Board.getInstance().MOVE_ORANGE);
                break;
        }
        abaloneBoard.repaint();
    }

    private boolean bStarted = false;
    private boolean bPlayAnalyzer = false;

    public boolean isbPlayAnalyzer() {
        return bPlayAnalyzer;
    }

    public void setbPlayAnalyzer(boolean bPlayAnalyzer) {
        this.bPlayAnalyzer = bPlayAnalyzer;
    }

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        bStarted = !bStarted;
        spnTurns.setEnabled(!bStarted);
        spnTime.setEnabled(!bStarted);
        radTwoPlayers.setEnabled(!bStarted);
        radThreePlayers.setEnabled(!bStarted);
        radFourPlayers.setEnabled(!bStarted);
        miOpen.setEnabled(!bStarted);
        miSaveAs.setEnabled(!bStarted);
        int mode = Board.getInstance().getGamemode();
        switch (mode) {
            case 2:
                tbtnPlayer1.setEnabled(!bStarted);
                tbtnPlayer2.setEnabled(!bStarted);
                break;

            case 3:
                tbtnPlayer1.setEnabled(!bStarted);
                tbtnPlayer2.setEnabled(!bStarted);
                tbtnPlayer3.setEnabled(!bStarted);
                break;

            case 4:
                tbtnPlayer1.setEnabled(!bStarted);
                tbtnPlayer2.setEnabled(!bStarted);
                tbtnPlayer3.setEnabled(!bStarted);
                tbtnPlayer4.setEnabled(!bStarted);
                break;
        }
        if (bStarted) {
            btnStart.setText("Stop");
            if (!DlgConfigRemote.isRemote()) {
                txtMessage.setText("");
            }
            time1 = time2 = time3 = time4 = 0;
            Board.CreateBoard(mode);
            char ch = tbtnPlayer1.isSelected() ? 'c' : 'h';
            Board.getInstance().setStrategy(0, ch);
            ch = tbtnPlayer2.isSelected() ? 'c' : 'h';
            Board.getInstance().setStrategy(1, ch);
            ch = tbtnPlayer3.isSelected() ? 'c' : 'h';
            Board.getInstance().setStrategy(2, ch);
            ch = tbtnPlayer4.isSelected() ? 'c' : 'h';
            Board.getInstance().setStrategy(3, ch);
            lblScore1.setBackground(CLR_BLUE_SCORE);
            lblScore1.setText(Integer.toString(Board.getInstance().getScore(0)) + "/0/" + time1);
            lblScore2.setBackground(CLR_RED_SCORE);
            lblScore2.setText(Integer.toString(Board.getInstance().getScore(1)) + "/0/" + time2);
            strGame = "Mode:";
            switch (Board.getInstance().getGamemode()) {
                case 2:
                    lblScore3.setBackground(CLR_INACTIVE_SCORE);
                    lblScore3.setText("");
                    lblScore4.setBackground(CLR_INACTIVE_SCORE);
                    lblScore4.setText("");
                    strGame += 2 + ";";
                    break;

                case 3:
                    lblScore3.setBackground(CLR_GREEN_SCORE);
                    lblScore3.setText(Integer.toString(Board.getInstance().getScore(2)) + "/0/" + time3);
                    lblScore4.setBackground(CLR_INACTIVE_SCORE);
                    lblScore4.setText("");
                    strGame += 3 + ";";
                    break;

                case 4:
                    lblScore3.setBackground(CLR_GREEN_SCORE);
                    lblScore3.setText(Integer.toString(Board.getInstance().getScore(2)) + "/0/" + time3);
                    lblScore4.setBackground(CLR_ORANGE_SCORE);
                    lblScore4.setText(Integer.toString(Board.getInstance().getScore(3)) + "/0/" + time4);
                    strGame += 4 + ";";
                    break;
            }
            int maxTurns = (int) spnTurns.getValue();
            int maxTime = (int) spnTime.getValue();
            showTurn(true);
            pnBoard.repaint();
        } else {
            if (client != null) {
                stopClient();
            }
            btnStart.setText("Start");
            pnlPlayer1.setBackground(Board.getInstance().GRAY1);
            pnlPlayer2.setBackground(Board.getInstance().GRAY1);
            pnlPlayer3.setBackground(Board.getInstance().GRAY1);
            pnlPlayer4.setBackground(Board.getInstance().GRAY1);
            lblScore1.setBackground(CLR_INACTIVE_SCORE);
            lblScore1.setText("");
            lblScore2.setBackground(CLR_INACTIVE_SCORE);
            lblScore2.setText("");
            lblScore3.setBackground(CLR_INACTIVE_SCORE);
            lblScore3.setText("");
            lblScore4.setBackground(CLR_INACTIVE_SCORE);
            lblScore4.setText("");
            timer1.stop();
            timer2.stop();
            timer3.stop();
            timer4.stop();
        }
    }//GEN-LAST:event_btnStartActionPerformed

    private int irowStart = -1;
    private int icolStart = -1;

    private void pnBoardMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnBoardMousePressed
        if (!bStarted && !bPlayAnalyzer) {
            return;
        } else if (client != null) {
            if ((Board.getInstance().getGameturn() == 0) && DlgConfigRemote.getBlueRemote()
                    || (Board.getInstance().getGameturn() == 1) && DlgConfigRemote.getRedRemote()
                    || (Board.getInstance().getGameturn() == 2) && DlgConfigRemote.getGreenRemote()
                    || (Board.getInstance().getGameturn() == 3) && DlgConfigRemote.getOrangeRemote()) {
                return;
            }
        }
        AbalonePaintPanel app = (AbalonePaintPanel) pnBoard;
        int x = evt.getX();
        int y = evt.getY();
        int arr[] = {-1, -1};
        if (app.isOnMarble(x, y, arr)) {
            Marble mrb = Board.getInstance().arr[arr[0]][arr[1]].mrb;
            if (evt.isControlDown()) {
                irowStart = arr[0];
                icolStart = arr[1];
                return;
            } else {
                boolean bmove = false;
                switch (Board.getInstance().gameturn) {
                    case 0:
                        bmove = (mrb == Marble.BLUE);
                        break;

                    case 1:
                        bmove = (mrb == Marble.RED);
                        break;

                    case 2:
                        bmove = (mrb == Marble.GREEN);
                        break;

                    case 3:
                        bmove = (mrb == Marble.ORANGE);
                        break;
                }
                if (bmove) {
                    irowStart = arr[0];
                    icolStart = arr[1];
                    app.setMarbleStart(irowStart, icolStart, x, y, mrb);
                    pnBoard.repaint();
                }
            }
        }
    }//GEN-LAST:event_pnBoardMousePressed

    public void showTurn(boolean timers) {
        pnlPlayer1.setBackground(Board.getInstance().GRAY1);
        pnlPlayer2.setBackground(Board.getInstance().GRAY1);
        pnlPlayer3.setBackground(Board.getInstance().GRAY1);
        pnlPlayer4.setBackground(Board.getInstance().GRAY1);
        int gameturn = Board.getInstance().getGameturn();
        switch (gameturn) {
            case 0:
                pnlPlayer1.setBackground(Board.getInstance().MOVE_BLUE);
                if (timers) {
                    timer1.start();
                    timer2.stop();
                    timer3.stop();
                    timer4.stop();
                }
                break;

            case 1:
                pnlPlayer2.setBackground(Board.getInstance().MOVE_RED);
                if (timers) {
                    timer1.stop();
                    timer2.start();
                    timer3.stop();
                    timer4.stop();
                }
                break;

            case 2:
                pnlPlayer3.setBackground(Board.getInstance().MOVE_GREEN);
                if (timers) {
                    timer1.stop();
                    timer2.stop();
                    timer3.start();
                    timer4.stop();
                }
                break;

            case 3:
                pnlPlayer4.setBackground(Board.getInstance().MOVE_ORANGE);
                if (timers) {
                    timer1.stop();
                    timer2.stop();
                    timer3.stop();
                    timer4.start();
                }
                break;
        }
    }

    private boolean checkMarble(Marble mrb, int step) {
        int gmode = Board.getInstance().getGamemode();
        int gturn = Board.getInstance().getGameturn();
        switch (gmode) {
            case 2:
                return (gturn == 0) && (mrb == Marble.BLUE)
                        || (gturn == 1) && (mrb == Marble.RED);

            case 3:
                return (gturn == 0) && (mrb == Marble.BLUE)
                        || (gturn == 1) && (mrb == Marble.RED)
                        || (gturn == 2) && (mrb == Marble.GREEN);

            case 4:
                if (step == 1) {
                    return (gturn == 0) && (mrb == Marble.BLUE)
                            || (gturn == 1) && (mrb == Marble.RED)
                            || (gturn == 2) && (mrb == Marble.GREEN)
                            || (gturn == 3) && (mrb == Marble.ORANGE);
                } else {
                    return (gturn == 0) && (mrb == Marble.BLUE || mrb == Marble.GREEN)
                            || (gturn == 1) && (mrb == Marble.RED || mrb == Marble.ORANGE)
                            || (gturn == 2) && (mrb == Marble.BLUE || mrb == Marble.GREEN)
                            || (gturn == 3) && (mrb == Marble.RED || mrb == Marble.ORANGE);
                }
        }
        return false;
    }

    private String getScoreString() {
        int gmode = Board.getInstance().getGamemode();
        switch (gmode) {
            case 2:
                return (Board.getInstance().getScore(0) + "-"
                        + Board.getInstance().getScore(1) + "!");

            case 3:
                return (Board.getInstance().getScore(0) + "-"
                        + Board.getInstance().getScore(1) + "-"
                        + Board.getInstance().getScore(2) + "!");

            case 4:
                return (Board.getInstance().getScore(0) + "-"
                        + Board.getInstance().getScore(1) + "-"
                        + Board.getInstance().getScore(2) + "-"
                        + Board.getInstance().getScore(3) + "!");
        }
        return "";
    }

    private int irowSelect1 = -1;
    private int icolSelect1 = -1;
    private int irowSelect2 = -1;
    private int icolSelect2 = -1;

    public void executeCommand(String cmd, MouseEvent evt, boolean bHuman, boolean bAnalyze) {
        int x = 0;
        int y = 0;
        if (bHuman) {
            x = evt.getX();
            y = evt.getY();
        }
        int arr[] = {-1, -1};
        AbalonePaintPanel app = (AbalonePaintPanel) pnBoard;
        String msg = "";
        boolean bGameOver = false;
        int turn = Board.getInstance().getGameturn();
        int maxTime = (int) spnTime.getValue();
        if (bAnalyze) {
            maxTime = Integer.MAX_VALUE;
        }
        String msg1 = "";
        if (turn == 0 && time1 >= maxTime) { // Blue
            bGameOver = true;
            if (Board.getInstance().getGamemode() == 2) {
                msg1 = "Red wins! Score: " + getScoreString();
            } else if (Board.getInstance().getGamemode() == 3) {
                if (Board.getInstance().getScore(1) > Board.getInstance().getScore(2)) {
                    msg1 = "Red wins! ";
                } else if (Board.getInstance().getScore(1) < Board.getInstance().getScore(2)) {
                    msg1 = "Green wins! ";
                } else {
                    msg1 = "Red and Green draw! ";
                }
                msg1 += "Score: " + getScoreString();
            } else {
                msg1 = "Red and Orange win! ";
                msg1 += "Score: " + getScoreString();
            }
        }
        if (turn == 1 && time2 >= maxTime) { // Red
            bGameOver = true;
            if (Board.getInstance().getGamemode() == 2) {
                msg1 = "Blue wins! Score: " + getScoreString();
            } else if (Board.getInstance().getGamemode() == 3) {
                if (Board.getInstance().getScore(0) > Board.getInstance().getScore(2)) {
                    msg1 = "Blue wins! ";
                } else if (Board.getInstance().getScore(0) < Board.getInstance().getScore(2)) {
                    msg1 = "Green wins! ";
                } else {
                    msg1 = "Blue and Green draw! ";
                }
                msg1 += "Score: " + getScoreString();
            } else {
                msg1 = "Blue and Green win! ";
                msg1 += "Score: " + getScoreString();
            }
        }
        if (turn == 2 && time3 >= maxTime) { // Green
            bGameOver = true;
            if (Board.getInstance().getGamemode() == 3) {
                if (Board.getInstance().getScore(0) > Board.getInstance().getScore(1)) {
                    msg1 = "Blue wins! ";
                } else if (Board.getInstance().getScore(0) < Board.getInstance().getScore(1)) {
                    msg1 = "Red wins! ";
                } else {
                    msg1 = "Blue and Red draw! ";
                }
                msg1 += "Score: " + getScoreString();
            } else {
                msg1 = "Red and Orange win! ";
                msg1 += "Score: " + getScoreString();
            }
        }
        if (turn == 3 && time4 >= maxTime) { // Orange
            bGameOver = true;
            msg1 = "Blue and Green win! ";
            msg1 += "Score: " + getScoreString();
        }
        if (bGameOver) {
            msg = "Time limit attained! Game Interrupted! " + msg1;
            txtMessage.setText(msg);
        }
        if (!bGameOver) {
            if (!bHuman || evt.isControlDown()) {
                if (!bHuman) {
                    if (cmd.charAt(0) == 'M') {
                        irowStart = cmd.charAt(2) - 'A';
                        icolStart = cmd.charAt(3) - '0' - 1;
                    } else {
                        irowStart = irowSelect1 = cmd.charAt(2) - 'A';
                        icolStart = icolSelect1 = cmd.charAt(3) - '0' - 1;
                        app.setSelect1(irowSelect1, icolSelect1);
                        irowSelect2 = cmd.charAt(5) - 'A';
                        icolSelect2 = cmd.charAt(6) - '0' - 1;
                        app.setSelect2(irowSelect2, icolSelect2);
                    }
                } else {
                    if (app.isOnMarble(x, y, arr)) {
                        Marble mrb = Board.getInstance().arr[arr[0]][arr[1]].mrb;
                        if (irowStart == arr[0] && icolStart == arr[1]) {
                            if (irowSelect1 < 0) {
                                if (!checkMarble(mrb, 1)) {
                                    return;
                                }
                                irowSelect1 = irowStart;
                                icolSelect1 = icolStart;
                                app.setSelect1(irowSelect1, icolSelect1);
                            } else {
                                if (!checkMarble(mrb, 2)) {
                                    return;
                                }
                                if (irowStart != irowSelect1 || icolStart != icolSelect1) {
                                    irowSelect2 = irowStart;
                                    icolSelect2 = icolStart;
                                    app.setSelect2(irowSelect2, icolSelect2);
                                }
                            }
                        }
                    }
                }
            }
            if (!bHuman || (!evt.isControlDown() && app.getMouseMove())) {
                int irowEnd1 = 0, icolEnd1 = 0;
                if (bHuman) {
                    app.closestMarble(x, y, arr);
                } else {
                    if (cmd.charAt(0) == 'M') {
                        arr[0] = cmd.charAt(5) - 'A';
                        arr[1] = cmd.charAt(6) - '0' - 1;
                    } else {
                        arr[0] = cmd.charAt(8) - 'A';
                        arr[1] = cmd.charAt(9) - '0' - 1;
                    }
                }
                irowEnd1 = arr[0];
                icolEnd1 = arr[1];
                if (Board.getInstance().positionEnd(irowStart, icolStart, arr)) {
                    int irowEnd = arr[0];
                    int icolEnd = arr[1];
                    char r1 = (char) (65 + irowStart);
                    char r2 = (char) (65 + irowEnd);
                    char r21 = (char) (65 + irowEnd1);
                    char c1 = (char) (48 + icolStart + 1);
                    char c2 = (char) (48 + icolEnd + 1);
                    char c21 = (char) (48 + icolEnd1 + 1);
                    String strMove;
                    if (irowSelect1 >= 0 && irowSelect2 >= 0) {
                        // Side move
                        if (irowStart == irowSelect1 && icolStart == icolSelect1) {
                            char rr = (char) (65 + irowSelect2);
                            char cc = (char) (48 + icolSelect2 + 1);
                            strMove = "S " + r1 + c1 + " " + rr + cc + " " + r2 + c2;
                            if (Board.getInstance().sidemove(r1, c1, rr, cc, r2, c2)) {
                                if (client != null && DlgConfigRemote.isRemote()) {
                                    if (turn == 0 && !DlgConfigRemote.getBlueRemote()
                                            || turn == 1 && !DlgConfigRemote.getRedRemote()
                                            || turn == 2 && !DlgConfigRemote.getGreenRemote()
                                            || turn == 3 && !DlgConfigRemote.getOrangeRemote()) {
                                        client.sendMessage("MOVE:" + strMove);
                                    }
                                }
                                txtMessage.setText(strMove);
                                strGame += strMove + ";";
                                playMove();
                            } else {
                                txtMessage.setText(Board.getInstance().getMessage());
                            }
                        }
                    } else {
                        strMove = "M " + r1 + c1 + " " + r21 + c21;
                        int scoreOld = Board.getInstance().getScore(0)
                                + Board.getInstance().getScore(1)
                                + Board.getInstance().getScore(2);
                        if (Board.getInstance().move(r1, c1, r2, c2)) {
                            if (client != null && DlgConfigRemote.isRemote()) {
                                if (turn == 0 && !DlgConfigRemote.getBlueRemote()
                                        || turn == 1 && !DlgConfigRemote.getRedRemote()
                                        || turn == 2 && !DlgConfigRemote.getGreenRemote()
                                        || turn == 3 && !DlgConfigRemote.getOrangeRemote()) {
                                    client.sendMessage("MOVE:" + strMove);
                                }
                            }
                            txtMessage.setText(strMove);
                            strGame += strMove + ";";
                            int score = Board.getInstance().getScore(0)
                                    + Board.getInstance().getScore(1)
                                    + Board.getInstance().getScore(2);
                            if (score != scoreOld) {
                                playCapture();
                            } else {
                                playMove();
                            }
                        } else {
                            txtMessage.setText(Board.getInstance().getMessage());
                        }
                        if (Board.getInstance().getScore(0) >= 6) {
                            if (Board.getInstance().getGamemode() == 2) {
                                msg = "Blue wins! Score: " + getScoreString();
                            } else if (Board.getInstance().getGamemode() == 3) {
                                msg = "Blue wins! Score: " + getScoreString();
                            } else {
                                msg = "Blue and Green wins! Score: " + getScoreString();
                            }
                            bGameOver = true;
                            txtMessage.setText(msg);
                        } else if (Board.getInstance().getScore(1) >= 6) {
                            if (Board.getInstance().getGamemode() == 2) {
                                msg = "Red wins! Score: " + getScoreString();
                            } else if (Board.getInstance().getGamemode() == 3) {
                                msg = "Red wins! Score: " + getScoreString();
                            } else {
                                msg = "Red and Orange wins! Score: " + getScoreString();
                            }
                            bGameOver = true;
                            txtMessage.setText(msg);
                        } else if (Board.getInstance().getScore(2) >= 6) {
                            msg = "Green wins! Score: " + getScoreString();
                            bGameOver = true;
                            txtMessage.setText(msg);
                        }
                    }
                    int t = Board.getInstance().getTurns(0);
                    lblScore1.setText(Integer.toString(Board.getInstance().getScore(0))
                            + "/" + t + "/" + time1);
                    t = Board.getInstance().getTurns(1);
                    lblScore2.setText(Integer.toString(Board.getInstance().getScore(1))
                            + "/" + t + "/" + time2);
                    if (Board.getInstance().getGamemode() >= 3) {
                        t = Board.getInstance().getTurns(2);
                        lblScore3.setText(Integer.toString(Board.getInstance().getScore(2))
                                + "/" + t + "/" + time3);
                    }
                    if (Board.getInstance().getGamemode() == 4) {
                        t = Board.getInstance().getTurns(3);
                        lblScore4.setText(Integer.toString(Board.getInstance().getScore(3))
                                + "/" + t + "/" + time4);
                    }
                }
                irowSelect1 = -1;
                icolSelect1 = -1;
                irowSelect2 = -1;
                icolSelect2 = -1;
                app.setSelect1(-1, -1);
                app.setSelect2(-1, -1);
            }
        }
        if (!bGameOver) {
            turn = Board.getInstance().getGameturn();
            if (turn == 0) {
                int t = Board.getInstance().getTurns(0);
                int maxTurns = (int) spnTurns.getValue();
                if (bAnalyze) {
                    maxTurns = Integer.MAX_VALUE;
                }
                if (t >= maxTurns) {
                    msg = "Turns limit attained! Game Interrupted! Score: " + getScoreString();
                    bGameOver = true;
                    txtMessage.setText(msg);
                }
            }
        }
        if (bGameOver) {
            if (client != null) {
                stopClient();
            }
            try {
                Thread.sleep(500);
            } catch (Exception ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
            bPlayAnalyzer = false;
            playWin();
        }
        if (bGameOver && !bAnalyze) {
            btnStartActionPerformed(null);
        } else {
            showTurn(!bAnalyze);
        }
        app.setMarbleStop();
        pnBoard.repaint();
    }

    private void pnBoardMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnBoardMouseReleased
        if (!bStarted && !bPlayAnalyzer) {
            return;
        }
        executeCommand("", evt, true, bPlayAnalyzer);
    }//GEN-LAST:event_pnBoardMouseReleased

    private void pnBoardMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnBoardMouseDragged
        if (!bStarted && !bPlayAnalyzer) {
            return;
        }
        AbalonePaintPanel app = (AbalonePaintPanel) pnBoard;
        if (app.getMouseMove()) {
            app.setMarbleMove(evt.getX(), evt.getY());
            pnBoard.repaint();
        }
    }//GEN-LAST:event_pnBoardMouseDragged

    private static String getExtension(String fp) {
        int ix1 = fp.lastIndexOf("\\");
        if (ix1 < 0) {
            return "";
        }
        String file = fp.substring(++ix1);
        ix1 = file.lastIndexOf(".");
        if (ix1 < 0) {
            return "";
        }
        return file.substring(++ix1);
    }

    private static String setExtension(String fp, String ext) {
        int ix1 = fp.lastIndexOf(".");
        if (ix1 < 0) {
            return fp + "." + ext;
        }
        return fp.substring(0, ix1) + "." + ext;
    }

    private void miSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSaveAsActionPerformed
        JFileChooser fc = new JFileChooser();
        String currdir = System.getProperty("user.dir");
        File fdir = new File(currdir);
        fc.setCurrentDirectory(fdir);
        FileNameExtensionFilter dbfilter = new FileNameExtensionFilter("Abalone File", "abl");
        fc.setFileFilter(dbfilter);
        int res = fc.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            String filepath = f.getPath();
            FileFilter ff = fc.getFileFilter();
            String desc = ff.getDescription();
            if (desc.equals("Abalone File")) {
                String ext = getExtension(filepath);
                if (!ext.equals("abl")) {
                    filepath = setExtension(filepath, "abl");
                }
            }
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileWriter(filepath));
                pw.print(strGame);
            } catch (FileNotFoundException fnfe) {
                JOptionPane.showMessageDialog(this, fnfe.toString(), "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.toString(), "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                if (pw != null) {
                    pw.close();
                }
            }
        }
    }//GEN-LAST:event_miSaveAsActionPerformed

    private void spnTimeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnTimeStateChanged
        String sval = spnTime.getValue().toString();
        int ival = Integer.parseInt(sval);
        Board.setMaxtime(ival);
    }//GEN-LAST:event_spnTimeStateChanged

    private void miOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miOpenActionPerformed
        JFileChooser fc = new JFileChooser();
        String currdir = System.getProperty("user.dir");
        File fdir = new File(currdir);
        fc.setCurrentDirectory(fdir);
        FileNameExtensionFilter ablfilter = new FileNameExtensionFilter("Abalone File", "abl");
        fc.setFileFilter(ablfilter);
        int res = fc.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            String filepath = f.getPath();
            FileFilter ff = fc.getFileFilter();
            String desc = ff.getDescription();
            if (desc.equals("Abalone File")) {
                String ext = getExtension(filepath);
                if (!ext.equals("abl")) {
                    filepath = setExtension(filepath, "abl");
                }
            }
            try {
                String fileContent = Files.readString(Path.of(filepath));
                String[] commands = fileContent.split(";");
                String[] arrs = commands[0].split(":");
                int mode = Integer.parseInt(arrs[1].trim());
                if (mode == 2) {
                    radTwoPlayers.setSelected(true);
                } else if (mode == 3) {
                    radThreePlayers.setSelected(true);
                } else if (mode == 4) {
                    radFourPlayers.setSelected(true);
                }
                List<String> alCmds = new ArrayList();
                for (int i = 1; i < commands.length; i++) {
                    alCmds.add(commands[i]);
                }
                DlgAnalyzer dlg = new DlgAnalyzer(this, alCmds, mode, false);
                dlg.setLocationRelativeTo(this);
                dlg.setVisible(true);
            } catch (FileNotFoundException fnfe) {
                JOptionPane.showMessageDialog(this, fnfe.toString(), "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.toString(), "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_miOpenActionPerformed

    private void configRemote() {
        DlgConfigRemote dlg = new DlgConfigRemote(this, Board.getInstance().getGamemode(), true);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
        if (DlgConfigRemote.isOK() && DlgConfigRemote.isRemote()) {
            if (!DlgConfigRemote.getBlueRemote()) {
                if (client == null) {
                    createClient("BLUE:" + DlgConfigRemote.getNameBlue());
                    if (client != null) {
                        client.sendMessage("MAXTURNS:" + (int) spnTurns.getValue());
                        client.sendMessage("MAXTIME:" + (int) spnTime.getValue());
                    } else {
                        JOptionPane.showMessageDialog(this, "Cannot connect to server!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            if (!DlgConfigRemote.getRedRemote()) {
                if (client == null) {
                    createClient("RED:" + DlgConfigRemote.getNameRed());
                    if (client == null) {
                        JOptionPane.showMessageDialog(this, "Cannot connect to server!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    client.sendMessage("RED:" + DlgConfigRemote.getNameRed());
                }
            }
            if (!DlgConfigRemote.getGreenRemote() && Board.getInstance().getGamemode() >= 3) {
                if (client == null) {
                    createClient("GREEN:" + DlgConfigRemote.getNameGreen());
                    if (client == null) {
                        JOptionPane.showMessageDialog(this, "Cannot connect to server!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    client.sendMessage("GREEN:" + DlgConfigRemote.getNameGreen());
                }
            }
            if (!DlgConfigRemote.getOrangeRemote() && Board.getInstance().getGamemode() == 4) {
                if (client == null) {
                    createClient("ORANGE:" + DlgConfigRemote.getNameOrange());
                    if (client == null) {
                        JOptionPane.showMessageDialog(this, "Cannot connect to server!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    client.sendMessage("ORANGE:" + DlgConfigRemote.getNameOrange());
                }
            }
            client.sendMessage("MODE:" + Board.getInstance().getGamemode());
        }
    }

    private void tbtnPlayer1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbtnPlayer1MouseClicked
        if (SwingUtilities.isRightMouseButton(evt)) {
            configRemote();
        }
    }//GEN-LAST:event_tbtnPlayer1MouseClicked

    private void tbtnPlayer2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbtnPlayer2MouseClicked
        if (SwingUtilities.isRightMouseButton(evt)) {
            configRemote();
        }
    }//GEN-LAST:event_tbtnPlayer2MouseClicked

    private void tbtnPlayer3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbtnPlayer3MouseClicked
        if (SwingUtilities.isRightMouseButton(evt)) {
            configRemote();
        }
    }//GEN-LAST:event_tbtnPlayer3MouseClicked

    private void tbtnPlayer4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbtnPlayer4MouseClicked
        if (SwingUtilities.isRightMouseButton(evt)) {
            configRemote();
        }
    }//GEN-LAST:event_tbtnPlayer4MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AbaloneMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AbaloneMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AbaloneMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AbaloneMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AbaloneMainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnStart;
    private javax.swing.JLabel lblScore1;
    private javax.swing.JLabel lblScore2;
    private javax.swing.JLabel lblScore3;
    private javax.swing.JLabel lblScore4;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTurn;
    private javax.swing.JLabel lblTurns;
    private javax.swing.JMenuBar mbMenuBar;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JMenuItem miHelp;
    private javax.swing.JMenuItem miOpen;
    private javax.swing.JMenuItem miSaveAs;
    private javax.swing.JMenu mnFile;
    private javax.swing.JMenu mnHelp;
    private javax.swing.JPanel pnBoard;
    private javax.swing.JPanel pnlPlayer1;
    private javax.swing.JPanel pnlPlayer2;
    private javax.swing.JPanel pnlPlayer3;
    private javax.swing.JPanel pnlPlayer4;
    private javax.swing.JRadioButton radFourPlayers;
    private javax.swing.JRadioButton radThreePlayers;
    private javax.swing.JRadioButton radTwoPlayers;
    private javax.swing.JSpinner spnTime;
    private javax.swing.JSpinner spnTurns;
    private javax.swing.JToggleButton tbtnPlayer1;
    private javax.swing.JToggleButton tbtnPlayer2;
    private javax.swing.JToggleButton tbtnPlayer3;
    private javax.swing.JToggleButton tbtnPlayer4;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}
