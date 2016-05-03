package com.medit.db;

import com.medit.DatabaseManager;
import com.medit.EpicManager;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents data associated with a medIT user.
 *
 * Created by matt on 4/11/2016.
 */
public class User {

    /**
     * Represents settings variables stored for a User.
     */
    public class Preferences {
        public boolean getsPush = false;
        public boolean getsSMS = false;
        public boolean getsEmail = false;

        /**
         * Serializes this object to JSON.
         *
         * @return A JSON representation of this object.
         */
        public JSONObject toJSON() {
            JSONObject json = new JSONObject();
            json.put("GetsPush", getsPush);
            json.put("GetsSMS", getsSMS);
            json.put("GetsEmail", getsEmail);
            return json;
        }
    }

    public String username = "";
    public String firstName = "";
    public String lastName = "";
    public String phoneNumber = "";
    public String emailAddress = "";
    public String photo = "";
    public Preferences preferences = new Preferences();

    /**
     * Retrieves all patients registered with this user.
     *
     * @return List of this user's patients.
     */
    public List<Patient> getPatients() {

        return DatabaseManager.getPatientsForUser(this);

    }

    /**
     * Retrieves all appointments associated with this user.
     *
     * @return List of this user's appointments.
     */
    public List<Appointment> getAppointments() {

        List<Patient> patients = getPatients();

        List<Appointment> appointments = new ArrayList<>();

        for (Patient patient : patients) {
            appointments.addAll(EpicManager.getAppointmentsForPatient(patient));
        }

        return appointments;
    }

    /**
     * Serializes this object to JSON.
     *
     * @return A JSON representation of this object.
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("Username", username);
        json.put("FirstName", firstName);
        json.put("LastName", lastName);
        json.put("PhoneNumber", phoneNumber);
        json.put("EmailAddress", emailAddress);
        json.put("Photo", photo);
        if(preferences != null) json.put("Preferences", preferences.toJSON());
        return json;
    }
}
