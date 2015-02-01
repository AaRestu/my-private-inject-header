package com.aarestu.inject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class RequestController extends Thread {
    static String proxyIP = "110.4.12.173";
    static int proxyPort = 80;
    private Socket proxySocket;
    private Socket requesterSocket;
    private String originRequest;

    public RequestController(Socket requesterSocket) {
        this.requesterSocket = requesterSocket;
    }

    @Override
    public void run() {
        proxySocket = generateProxySocket();
        if(proxySocket != null) {

            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(proxySocket.getInputStream()));
                for (String line; (line = reader.readLine()) != null;) {

                    //kirim ke client
                    requesterSocket.getOutputStream().write(line.getBytes());
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                try { requesterSocket.getOutputStream().close(); } catch (IOException logOrIgnore) {}
            }

        } else {
            try { requesterSocket.getOutputStream().close(); } catch (IOException logOrIgnore) {}
        }
    }

    private Socket generateProxySocket() {
        try {
            Socket socket = new Socket(proxyIP, proxyPort);
            OutputStream os = socket.getOutputStream();

            os.write(originRequest.getBytes());

            return socket;
        } catch (IOException e) {
            return null;
        }
    }

    public void setOriginRequest(String originRequest) {
        this.originRequest = originRequest;
    }
}
