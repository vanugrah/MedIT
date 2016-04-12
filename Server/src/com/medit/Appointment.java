package com.medit;

import java.util.Date;

/**
 * Created by matt on 4/4/2016.
 */
public class Appointment {
    public int patientID;
    public Date appointmentDate;
    public String clinicName;
    public String clinicLocation;
    public String doctorName;

    public void confirmAppointment() {

    }

    public void checkInAppointment() {

    }

    public void cancelAppointment() {

    }

    public boolean dueForReminder() {

        return false;
    }

    public boolean dueForConfirmation() {

        return false;
    }
}
