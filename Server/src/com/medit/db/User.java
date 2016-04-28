package com.medit.db;

import com.medit.DatabaseManager;
import com.medit.EPICManager;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matt on 4/11/2016.
 */
public class User {

    public class Preferences {
        public boolean getsPush = false;
        public boolean getsSMS = false;
        public boolean getsEmail = false;

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
    public Preferences preferences = new Preferences();

    public List<Patient> getPatients() {

        return DatabaseManager.getPatientsForUser(this);

    }

    public List<Appointment> getAppointments() {

        List<Patient> patients = getPatients();

        List<Appointment> appointments = new ArrayList<>();

        for (Patient patient : patients) {
            appointments.addAll(EPICManager.getAppointmentsForPatient(patient));
        }

        return appointments;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("Username", username);
        json.put("FirstName", firstName);
        json.put("LastName", lastName);
        json.put("PhoneNumber", phoneNumber);
        json.put("EmailAddress", emailAddress);
        if(preferences != null) json.put("Preferences", preferences.toJSON());
        return json;
    }
}
