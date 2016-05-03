package com.medit.db;

import com.medit.EpicManager;
import org.json.JSONObject;

import java.util.List;



/**
 * Represents data associated with a patient.
 *
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
    public String insuranceID = "";
    public String insuranceProvider = "";
    public String photo = "";

    /**
     * Retrieve a list of all appointments associated with this patient.
     *
     * @return List of patient's appointments.
     */
    public List<Appointment> getAppointments() {
        return EpicManager.getAppointmentsForPatient(this);
    }

    /**
     * Serializes this object to JSON.
     *
     * @return A JSON representation of this object.
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("PatientID", patientID);
        json.put("FirstName", firstName);
        json.put("LastName", lastName);
        json.put("Age", age);
        json.put("Sex", sex);
        json.put("Color", color);
        json.put("InsuranceID", insuranceID);
        json.put("InsuranceProvider", insuranceProvider);
        json.put("Photo", photo);
        if(user != null) json.put("User", user.toJSON());

        return json;
    }
}
