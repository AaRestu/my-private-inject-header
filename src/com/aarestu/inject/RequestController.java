package com.aarestu.inject;

import java.io.*;
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

            BufferedReader reader = null;
            DataOutputStream out = null;
            try {
                out = new DataOutputStream(requesterSocket.getOutputStream());
                reader = new BufferedReader(new InputStreamReader(proxySocket.getInputStream()));
                for (String line; (line = reader.readLine()) != null;) {

                    //kirim ke client
                    out.write(line.getBytes());
                    System.out.println(line);
                }
                out.flush();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                try { requesterSocket.getOutputStream().close(); } catch (IOException logOrIgnore) {}
            } finally {
                if (out != null) try { out.close(); } catch (IOException logOrIgnore) {}
                if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
                if (requesterSocket != null ) try { requesterSocket.close(); } catch (IOException logOrIgnore) {}
                if (proxySocket != null ) try { proxySocket.close(); } catch (IOException logOrIgnore) {}
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
            os.flush();

            return socket;
        } catch (IOException e) {
            return null;
        }
    }

    public void setOriginRequest(String originRequest) {
        this.originRequest = originRequest;
    }
}
