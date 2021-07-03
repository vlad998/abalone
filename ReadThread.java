package abalone;

import java.io.*;
import java.net.*;

public class ReadThread extends Thread {

    private BufferedReader reader;
    private Socket socket;
    private AbaloneClient client;
    private boolean stop;

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public ReadThread(Socket socket, AbaloneClient client) {
        this.socket = socket;
        this.client = client;
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                Thread.sleep(200);
                String response = reader.readLine();
                if (response != null) {
                    response = response.trim();
                    if (response.length() > 0) {
                        System.out.println(response);
                        client.addMsgQueue(response);
                    }
                }
            } catch (IOException ioe) {
                stop = true;
            } catch (InterruptedException ie) {
            }
        }
    }
}
