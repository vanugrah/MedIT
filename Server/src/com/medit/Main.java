package com.medit;

import com.medit.demoPanel.DemoPanel;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Thread serverThread = new Thread(new Server(), "Server");
        ReminderManager reminderManager = new ReminderManager();
        Thread daemonThread = new Thread(reminderManager, "ReminderManager");

        JFrame frame = new JFrame("DemoPanel");
        frame.setContentPane(new DemoPanel(reminderManager).getContentPane());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

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
        if(frame.isVisible())
            frame.dispose();
        if(serverThread.isAlive())
            serverThread.interrupt();
        if(daemonThread.isAlive())
            daemonThread.interrupt();
        System.out.println("Server shutdown complete.");
    }
}
