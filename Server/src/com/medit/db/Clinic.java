package com.medit.db;

import org.json.JSONObject;

/**
 * Created by matt on 4/11/2016.
 */
public class Clinic {

    public int clinicID = -1;
    public String specialization = "";
    public String phoneNumber = "";
    public Hospital hospital = new Hospital();

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("ClinicID", clinicID);
        json.put("Specialization", specialization);
        json.put("PhoneNumber", phoneNumber);

        return json;
    }
}
