package com.medit;

import java.util.Date;
import java.util.List;

import com.medit.db.Appointment;
import com.medit.db.Patient;

/**
 * Primary class for reminder daemon.
 *
 * Created by matt on 4/2/2016.
 */
public class ReminderManager implements Runnable {

    @Override
    /**
     * Entry point for this thread. Loops through all appointments for medIT patients. If the appointment is due for a
     * reminder or confirmation, one is sent.
     */
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

    /**
     * Sends a reminder for the given appointment. The reminder method (SMS, Email, or both) is determined based on
     * user preferences.
     *
     * @param appointment
     */
    private void sendReminder(Appointment appointment) {
        if(appointment.user.preferences.getsEmail) {
            EmailManager.sendEmailReminder(appointment);
        }
        if(appointment.user.preferences.getsSMS) {
            SMSManager.sendSMSReminder(appointment);
        }
    }

    /**
     * Sends a confirmation request for the given appointment. The communication method (SMS, Email, or both) is
     * determined based on user preferences.
     *
     * @param appointment
     */
    private void sendConfirmation(Appointment appointment) {
        if(appointment.user.preferences.getsEmail) {
            EmailManager.sendEmailConfirmation(appointment);
        }
        if(appointment.user.preferences.getsSMS) {
            SMSManager.sendSMSConfirmation(appointment);
        }
    }

    /**
     * Sets the date of last reminder for the given appointment to the current date and time.
     *
     * @param appointment
     */
    private void updateDateOfLastReminder(Appointment appointment) {
        DatabaseManager.saveDateOfLastReminder(appointment, new Date());
    }
}
