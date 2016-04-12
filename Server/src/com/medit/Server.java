package com.medit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.Buffer;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by matt on 4/2/2016.
 */
public class Server implements Runnable {

    private static ServerSocket serverSocket;

    private JSONObject getJSONFromPOSTReader(BufferedReader reader) {
        try {
            String currentLine = reader.readLine();
            String headerLine = currentLine;
            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            String httpMethod = tokenizer.nextToken();
            int contentLength = 0;

            if (httpMethod.equals("POST")) {
                while (reader.ready()) {
                    currentLine = reader.readLine();
//                    System.out.println(currentLine);
                    if (currentLine.contains("Content-type:")) {
                        if (!currentLine.contains("application/json"))
                            System.out.println("Unsupported content type in POST.");
                    }
                    if (currentLine.contains("Content-Length:")) {
                        contentLength = Integer.parseInt(currentLine.split(" ")[1]);
                    }
                    if (currentLine.equals("")) {
                        char[] bytes = new char[contentLength];
                        reader.read(bytes);
                        return new JSONObject(new String(bytes));
                    }
                }
            } else {
                System.out.println("Unsupported request type.");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private JSONObject executeJSONRequest(JSONObject root) {
        JSONObject response = new JSONObject();
        if(!root.has("MessageType")) {
            System.err.println("No MessageType found in post data.");
            response.put("MessageType", "Error");
            response.put("Message", "Root object must have attribute 'MessageType'");
            return response;
        }
        String messageType = root.getString("MessageType");
        if(messageType.equals("AuthRequest")) {
            System.out.println("Authentication Request received.");
            String username = root.getString("Username");
            String password = root.getString("Password");
            // check if credentials are correct
            response.put("MessageType", "AuthResponse");
            response.put("PatientID", 0);
        } else if(messageType.equals("PatientQuery")) {
            System.out.println("Patient Query received.");
            int id = root.getInt("PatientID");
            // get data for patient with ID id
            Patient p = new Patient();
            response.put("FirstName", p.firstName = "fname");
            response.put("LastName", p.lastName = "lname");
            response.put("EmailAddress", p.emailAddress = "email");
        } else if(messageType.equals("AppointmentsQuery")) {
            System.out.println("Appointments Query received.");
            int patientID = root.getInt("PatientID");
            // get patient
            Patient p = new Patient();
            List<Appointment> appointments = p.getAppointments();
            response.put("Appointments", new JSONArray());
            for (Appointment app : appointments) {
                JSONObject appObj = new JSONObject();
                appObj.put("ClinicName", app.clinicName);
                appObj.put("ClinicLocation", app.clinicLocation);
                appObj.put("DoctorName", app.doctorName);
                appObj.put("Date", app.appointmentDate);
                appObj.put("PatientID", app.patientID);
                response.append("Appointments", appObj);
            }
        }
        return response;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(80);
            serverSocket.setSoTimeout(500);
            while(!Thread.interrupted()) {
                Socket clientSocket;
                try {
                    clientSocket = serverSocket.accept();
                } catch(SocketTimeoutException e) {
                    continue;
                }
                System.out.println("The client " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " is connected.");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                JSONObject obj = getJSONFromPOSTReader(bufferedReader);
                JSONObject response = executeJSONRequest(obj);

                OutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
                final String HEADERS = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: ";
                String reponseString = response.toString();
                out.write( (HEADERS + reponseString.length() + "\r\n\r\n" + reponseString).getBytes());
                out.flush();
                out.close();
                System.out.println("Response sent.");
            }
        } catch(IOException e) {
            System.err.println("Server thread encountered an error.");
            System.err.println(e);
        }
    }
}
