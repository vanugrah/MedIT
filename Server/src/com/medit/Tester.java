package com.medit;

import com.medit.db.Appointment;
import com.mysql.fabric.xmlrpc.base.Data;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by matt on 4/4/2016.
 */
public class Tester {

    public static void main(String[] args) {
        System.out.println("Tester running.");

        HttpURLConnection connection = null;
        try {
            JSONObject obj = new JSONObject();
            obj.put("MessageType", "AppointmentsQuery");

            obj.put("Username", "mbarulic3");

            URL url = new URL("http://127.0.0.1/");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type","application/json");
            connection.setFixedLengthStreamingMode(obj.toString().getBytes().length);
            OutputStream out = new BufferedOutputStream(connection.getOutputStream());

            out.write(obj.toString().getBytes());
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            String inputLine;
            while((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection != null)
                connection.disconnect();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Tester finished.");
    }

}
