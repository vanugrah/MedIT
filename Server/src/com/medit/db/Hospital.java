package com.medit.db;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matt on 4/11/2016.
 */
public class Hospital {

    public int hospitalID = -1;
    public String name = "";
    public String branch = "";
    public String address = "";
    public String city = "";
    public String state = "";
    public String zipCode = "";

    public List<Clinic> getClinics() {
        return new ArrayList<>();
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("HospitalID", hospitalID);
        json.put("Name", name);
        json.put("Branch", branch);
        json.put("Address", address);
        json.put("City", city);
        json.put("State", state);
        json.put("ZipCode", zipCode);

        return json;
    }
}
