package com.medit;

import java.util.Date;
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
                List<Appointment> appointments = patient.getAppointments();
                for (Appointment appointment : appointments) {
                    if(appointment.dueForReminder()) {
                        if(appointment.dueForConfirmation()) {
                            sendConfirmation(appointment);
                        } else {
                            sendReminder(appointment);
                        }
                        System.out.println("Sending reminder for appointment " + appointment.appointmentID);
                        updateDateOfLastReminder(appointment);
                    }
                }

            }
        }
    }

    private void sendReminder(Appointment appointment) {
        if(appointment.user.preferences.getsEmail) {
            EmailManager.sendEmailReminder(appointment);
        }
        if(appointment.user.preferences.getsSMS) {
            SMSManager.sendSMSReminder(appointment);
        }
    }

    private void sendConfirmation(Appointment appointment) {
        if(appointment.user.preferences.getsEmail) {
            EmailManager.sendEmailConfirmation(appointment);
        }
        if(appointment.user.preferences.getsSMS) {
            SMSManager.sendSMSConfirmation(appointment);
        }
    }

    private void updateDateOfLastReminder(Appointment appointment) {
        DatabaseManager.saveDateOfLastReminder(appointment, new Date());
    }
}
