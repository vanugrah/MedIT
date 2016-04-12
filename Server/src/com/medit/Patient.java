package com.medit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matt on 4/4/2016.
 */
public class Patient {

    public int patientID;
    public boolean receiveEmail;
    public boolean receiveSMS;
    public String emailAddress;
    public String firstName;
    public String lastName;
    public String phoneNumber;


    public void populatePatientInformation() {

    }

    public void updateInformation() {

    }

    public List<Appointment> getAppointments() {

        return new ArrayList<Appointment>();
    }
}
