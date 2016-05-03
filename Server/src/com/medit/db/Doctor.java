package com.medit.db;

import org.json.JSONObject;

/**
 * Represents data associated with an individual doctor.
 *
 * Created by matt on 4/11/2016.
 */
public class Doctor {

    public int doctorID = -1;
    public String firstName = "";
    public String lastName = "";
    public String specialization = "";

    /**
     * Serializes this object to JSON.
     *
     * @return A JSON representation of this object.
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("DoctorID", doctorID);
        json.put("FirstName", firstName);
        json.put("LastName", lastName);
        json.put("Specialization", specialization);

        return json;
    }

}
