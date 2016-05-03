package com.medit.db;

import org.json.JSONObject;

/**
 * Represents data associated with a given clinic.
 *
 * Created by matt on 4/11/2016.
 */
public class Clinic {

    public int clinicID = -1;
    public String specialization = "";
    public String phoneNumber = "";
    public Hospital hospital = new Hospital();

    /**
     * Serializes this object to JSON.
     *
     * @return A JSON representation of this object.
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("ClinicID", clinicID);
        json.put("Specialization", specialization);
        json.put("PhoneNumber", phoneNumber);
        json.put("Hospital", hospital.toJSON());

        return json;
    }
}
