package com.medit.db;

import com.medit.DatabaseManager;
import com.medit.EPICManager;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by matt on 4/4/2016.
 */
public class Patient {

    public enum Sex {
        Male,
        Female;

        public String toString() {
            switch(this) {
                case Male:
                    return "Male";
                case Female:
                    return "Female";
                default:
                    return "";
            }
        }
    }

    public int patientID = -1;
    public String firstName = "";
    public String lastName = "";
    public int age = 0;
    public Sex sex = Sex.Male;
    public User user = new User();
    public String color = "";

    public List<Appointment> getAppointments() {
        return EPICManager.getAppointmentsForPatient(this);
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("PatientID", patientID);
        json.put("FirstName", firstName);
        json.put("LastName", lastName);
        json.put("Age", age);
        json.put("Sex", sex);
        json.put("Color", color);
        if(user != null) json.put("User", user.toJSON());

        return json;
    }
}
