package com.medit.db;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by matt on 4/4/2016.
 */
public class Appointment {
    public int appointmentID = -1;
    public Patient patient = new Patient();
    public User user = new User();
    public Doctor doctor = new Doctor();
    public Clinic clinic = new Clinic();
    public Date date = new Date();
    public String notes = "";
    public boolean Confirmed = false;
    public boolean CheckedIn = false;
    public boolean Cancelled = false;

    public boolean dueForReminder() {
        return false;
    }

    public boolean dueForConfirmation() {
        return false;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("AppointmentID", appointmentID);
        json.put("Date", date);
        json.put("Notes", notes);
        json.put("Confirmed", Confirmed);
        json.put("Cancelled", Cancelled);
        json.put("CheckedIn", CheckedIn);

        return json;
    }
}
