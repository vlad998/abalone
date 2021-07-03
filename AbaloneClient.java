package abalone;

import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class AbaloneClient {

    private static final int PORT = 8989;
    private String hostname;
    private ReadThread rth;
    private PrintWriter writer;

    private Queue<String> qmsg = new LinkedList();

    public void addMsgQueue(String msg) {
        qmsg.add(msg);
    }

    public boolean isMsgQueueEmpty() {
        return qmsg.isEmpty();
    }

    public String peekMsgQueue() {
        return qmsg.peek();
    }

    public String pollMsgQueue() {
        return qmsg.poll();
    }

    public AbaloneClient(String hostname) {
        this.hostname = hostname;
    }

    public void sendMessage(String msg) {
        writer.println(msg);
    }

    public void stop() {
        sendMessage("STOP");
        rth.setStop(true);
        
        
    }

    public boolean execute() {
        try {
            Socket socket = new Socket(hostname, PORT);
            System.out.println("Connected to the abalone server");
            rth = new ReadThread(socket, this);
            rth.start();
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            return true;
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
            return false;
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
            return false;
        }
    }
}
