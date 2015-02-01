package com.aarestu.inject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpProxy extends Thread {

    private ServerSocket serverSocket;
    private int listenPort = 6789;
    private Socket socket;

    @Override
    public void run() {
        runServerSoceket();
    }

    private void runServerSoceket() {
        try {
            serverSocket = new ServerSocket(listenPort);
            System.out.println("Inject service berjalan di port " + listenPort);
        } catch (IOException e) {
            System.err.println("Tidak bisa menjalankan inject di port " + listenPort);
        }

        handleRequest();
    }

    public void stopListen() {
        if (isRunning()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Tidak bisa menghentikan inject di port " + listenPort);
            } finally {
                serverSocket = null;
                System.out.println("inject di port " + listenPort + " telah di hentikan");
            }
        }
    }

    public Boolean isRunning() {
        return serverSocket != null;
    }

    private void handleRequest() {

        try {
            while (isRunning()) {
                socket = serverSocket.accept();

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (isRunning() && socket.getInputStream().available() < 1) {
                    // tunggu hingga ada request input
                }

                //pastikan user masih menjalankan applikasi
                if(!isRunning()) break;

                int available = socket.getInputStream().available();
                byte[] buffer = new byte[available];
                socket.getInputStream().read(buffer);

                String request = new String(buffer);

                System.out.println("ORIGIN REQUEST : ");
                System.out.println(request);

                socket.getOutputStream().close();
            }
        } catch (Exception e) {
            System.err.println("Gagal meng-handle request");
        } finally {
            stopListen();
        }

    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }
}
