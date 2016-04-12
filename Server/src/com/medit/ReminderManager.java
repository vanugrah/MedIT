package com.medit;

import java.io.*;
import java.net.*;
import java.util.List;

import org.json.*;

/**
 * Created by matt on 4/2/2016.
 */
public class ReminderManager implements Runnable {

    private DatabaseManager db;

    @Override
    public void run() {
        db = new DatabaseManager();

        while(!Thread.interrupted()) {
            List<Patient> patients = db.getAllPatients();
            for (Patient patient : patients) {
                List<Appointment> appointments = patient.getAppointments();
                for (Appointment appointment : appointments) {
                    if(appointment.dueForReminder()) {
                        if(appointment.dueForConfirmation()) {
                            sendConfirmation(patient, appointment);
                        } else {
                            sendConfirmation(patient, appointment);
                        }
                    }
                }

            }
        }
    }

    public void sendReminder(Patient patient, Appointment appointment) {
        if(patient.receiveEmail) {
            EmailManager.sendEmailReminder(patient, appointment);
        }
    }

    public void sendConfirmation(Patient patient, Appointment appointment) {
        if(patient.receiveEmail) {
            EmailManager.sendEmailConfirmation(patient, appointment);
        }
    }
}
