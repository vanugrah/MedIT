package com.medit;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Thread serverThread = new Thread(new Server(), "Server");
        Thread daemonThread = new Thread(new ReminderManager(), "ReminderManager");
        serverThread.start();
        daemonThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Server running. Type q[enter] to quit.");
        while(serverThread.isAlive() || daemonThread.isAlive()) {
            try {
                if(System.in.read() == 'q') {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Shutting down server...");
        if(serverThread.isAlive())
            serverThread.interrupt();
        if(daemonThread.isAlive())
            daemonThread.interrupt();
        System.out.println("Server shutdown complete.");
    }
}
