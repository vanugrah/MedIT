package com.medit;

import java.io.IOException;

/**
 * Entry point for Server code. Initializes a thread for the application-facing JSON interface and a daemon for sending
 * appointment reminders.
 */
public class Main {

    public static void main(String[] args) {
        // Register MySQL Driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Unable to register MySQL Driver class:");
            e.printStackTrace();
        }

        Thread serverThread = new Thread(new Server(), "Server");
        ReminderManager reminderManager = new ReminderManager();
        Thread daemonThread = new Thread(reminderManager, "ReminderManager");

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
