package com.medit.db;

import org.json.JSONObject;

/**
 * Created by matt on 4/11/2016.
 */
public class Doctor {

    public int doctorID = -1;
    public String firstName = "";
    public String lastName = "";
    public String specialization = "";

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("DoctorID", doctorID);
        json.put("FirstName", firstName);
        json.put("LastName", lastName);
        json.put("Specialization", specialization);

        return json;
    }

}
