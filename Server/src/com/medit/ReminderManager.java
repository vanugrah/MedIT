package com.medit;

import java.util.List;

import com.medit.db.Appointment;
import com.medit.db.Patient;

/**
 * Created by matt on 4/2/2016.
 */
public class ReminderManager implements Runnable {

    @Override
    public void run() {
        DatabaseManager db = new DatabaseManager();

        while(!Thread.interrupted()) {
            List<Patient> patients = db.getAllPatients();
            for (Patient patient : patients) {
//                System.out.println(patient.firstName);
                List<Appointment> appointments = patient.getAppointments();
                for (Appointment appointment : appointments) {
//                    System.out.println("\t" + appointment.date);
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

    public void sendReminder(Appointment appointment) {
        if(appointment.user.preferences.getsEmail) {
            EmailManager.sendEmailReminder(appointment);
        }
        if(appointment.user.preferences.getsSMS) {
            SMSManager.sendSMSReminder(appointment);
        }
    }

    public void sendConfirmation(Patient patient, Appointment appointment) {
        if(appointment.user.preferences.getsSMS) {
            EmailManager.sendEmailConfirmation(appointment);
        }
        if(appointment.user.preferences.getsSMS) {
            SMSManager.sendSMSConfirmation(appointment);
        }
    }
}
