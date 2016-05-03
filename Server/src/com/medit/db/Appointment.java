package com.medit.db;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;

/**
 * Represents a scheduled appointment including information about the patient, doctor, and clinic.
 *
 * Created by Matthew Barulic on 4/4/2016.
 */
public class Appointment {
    public int appointmentID = -1;
    public Patient patient = new Patient();
    public User user = new User();
    public Doctor doctor = new Doctor();
    public Clinic clinic = new Clinic();
    public Date date = new Date();
    public Date dateOfLastReminder = null;
    public String notes = "";
    public boolean Confirmed = false;
    public boolean CheckedIn = false;
    public boolean Cancelled = false;

    private static final long OneDayInMS = 86400000;

    /**
     * Determines if this appointment needs a reminder sent for it.
     *
     * @return True if a reminder should be sent for this appointment. False otherwise.
     */
    public boolean dueForReminder() {
        Date today = new Date();

        /*
         * Make sure it's been at least 1 day since we last sent a reminder for this appointment.
         * This prevents multiple reminders being sent in the same day.
         */
        if(dateOfLastReminder == null || today.getTime() - dateOfLastReminder.getTime() > OneDayInMS) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            // Day before
            cal.add(Calendar.DATE, -1);
            if ( abs(cal.getTime().getTime() - today.getTime()) < OneDayInMS ) {
                return true;
            }

            // 1 week before
            cal.add(Calendar.DATE, -6);
            if ( abs(cal.getTime().getTime() - today.getTime()) < OneDayInMS ) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if this appointment needs to be confirmed now.
     *
     * @return True if a confirmation request should be sent for this appointment. False otherwise.
     */
    public boolean dueForConfirmation() {
        Date today = new Date();

        /*
         * Make sure it's been at least 1 day since we last sent a reminder for this appointment.
         * This prevents multiple reminders being sent in the same day.
         */
        if(dateOfLastReminder == null || today.getTime() - dateOfLastReminder.getTime() > 8.64e7) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            // Day before
            cal.add(Calendar.DATE, -1);
            if ( abs(cal.getTime().getTime() - today.getTime()) < OneDayInMS ) {
                return true;
            }
        }

        return false;
    }

    /**
     * Serializes this object to JSON.
     *
     * @return A JSON representation of this object.
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("AppointmentID", appointmentID);
        json.put("Date", date.getTime());
        json.put("Notes", notes);
        json.put("Confirmed", Confirmed);
        json.put("Cancelled", Cancelled);
        json.put("CheckedIn", CheckedIn);
        json.put("Patient", patient.toJSON());
        json.put("User", user.toJSON());
        json.put("Clinic", clinic.toJSON());
        json.put("Doctor", doctor.toJSON());

        return json;
    }
}
