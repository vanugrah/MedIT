package com.medit;

import com.medit.db.Appointment;
import com.medit.db.Patient;
import com.medit.db.User;
import com.mysql.fabric.xmlrpc.base.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by matt on 4/2/2016.
 */
public class Server implements Runnable {

    private JSONObject getJSONFromPOSTReader(BufferedReader reader) {
        try {
            String currentLine = null;
            int contentLength = 0;

            while (reader.ready()) {
                currentLine = reader.readLine();
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
        switch (messageType) {
            case "AuthRequest": {
                System.out.println("Authentication Request received.");
                String username = root.getString("Username");
                String password = root.getString("Password");
                boolean success = DatabaseManager.verifyLoginCredentials(username, password);
                response.put("MessageType", "AuthResponse");
                response.put("Success", success);
                if(success) {
                    response.put("User", DatabaseManager.getUser(username).toJSON());
                } else {
                    response.put("User", (User)null);
                }
                break;
            }
            case "PatientQuery": {
                System.out.println("Patient Query received.");
                int id = root.getInt("PatientID");
                Patient p = EPICManager.getPatientInformation(id);
                response.put("MessageType", "PatientQueryResults");
                if(p != null) {
                    response.put("Found", true);
                    response.put("Patient", p.toJSON());
                } else {
                    response.put("Found", false);
                    response.put("Patient", (Patient)null);
                }
                break;
            }
            case "UserPatientsQuery": {
                System.out.println("User patients query received.");
                String username = root.getString("Username");
                User user = DatabaseManager.getUser(username);
                if(user == null) {
                    response.put("MessageType", "Error");
                    response.put("Message", "No user found with username " + username);
                    break;
                }
                List<Patient> patients = DatabaseManager.getPatientsForUser(user);
                response.put("MessageType", "UserPatientsQueryResults");
                response.put("Patients", new JSONArray());

                for(Patient patient : patients) {
                    response.append("Patients", patient.toJSON());
                }
                break;
            }
            case "AppointmentsQuery": {
                System.out.println("Appointments Query received.");
                String username = root.getString("Username");
                User user = DatabaseManager.getUser(username);
                if(user == null) {
                    response.put("MessageType", "Error");
                    response.put("Message", "Could not find user with username " + username);
                    break;
                }
                List<Patient> patients = DatabaseManager.getPatientsForUser(user);

                response.put("MessageType", "AppointmentsQueryResults");
                response.put("Appointments", new JSONArray());

                for (Patient patient : patients) {
                    List<Appointment> appointments = patient.getAppointments();
                    for(Appointment appointment : appointments) {
                        response.append("Appointments", appointment.toJSON());
                    }
                }
                break;
            }
            case "UserSettingsQuery" : {
                System.out.println("User settings query received.");
                String username = root.getString("Username");
                User user = DatabaseManager.getUser(username);
                response.put("MessageType", "UserSettingsQueryResults");
                response.put("Username", username);
                response.put("GetsPush", user.preferences.getsPush);
                response.put("GetsSMS", user.preferences.getsSMS);
                response.put("GetsEmail", user.preferences.getsEmail);
                break;
            }
            case "UserSettingsChange": {
                System.out.println("User settings change request received.");
                String username = root.getString("Username");
                boolean getsPush = root.getBoolean("GetsPush");
                boolean getsSMS = root.getBoolean("GetsSMS");
                boolean getsEmail = root.getBoolean("GetsEmail");
                DatabaseManager.saveSettings(username, getsPush, getsSMS, getsEmail);
                response.put("MessageType", "UserSettingsChangeConfirmation");
                break;
            }
            case "ConfirmAppointment" : {
                System.out.println("Appointment confirmation received.");
                int appointmentID = root.getInt("AppointmentID");
                EPICManager.confirmAppointment(appointmentID);
                response.put("MessageType", "AppointmentConfirmed");
                response.put("AppointmentID", appointmentID);
                break;
            }
            case "CancelAppointment" : {
                System.out.println("Appointment cancellation received.");
                int appointmentID = root.getInt("AppointmentID");
                EPICManager.cancelAppointment(appointmentID);
                response.put("MessageType", "AppointmentCancelled");
                response.put("AppointmentID", appointmentID);
                break;
            }
            case "CheckInForAppointment" : {
                System.out.println("Appointment check-in received.");
                int appointmentID = root.getInt("AppointmentID");
                EPICManager.checkInForAppointment(appointmentID);
                response.put("MessageType", "AppointmentCheckedIn");
                response.put("AppointmentID", appointmentID);
                break;
            }
            case "SaveColorForPatient" : {
                System.out.println("Color chance request received.");
                int patientID = root.getInt("PatientID");
                String color = root.getString("Color");
                DatabaseManager.saveColorForPatient(patientID, color);
                response.put("MessageType", "ColorSaved");
                response.put("PatientID", patientID);
                break;
            }
            default : {
                String message = "Unrecognized message type: " + messageType;
                System.out.println(message);
                response.put("MessageType", "Error");
                response.put("Message", message);
                break;
            }
        }
        return response;
    }

    private String handleGETRequest(String path) {
        if(path.substring(1,8).equals("Confirm")) {
            int appointmentID = Integer.parseInt(path.substring(12));
            EPICManager.confirmAppointment(appointmentID);
            return "<html>Thank you for confirming!</html>";
        }
        return "";
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(80);
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

                String currentLine = bufferedReader.readLine();
                String headerLine = currentLine;
                System.out.println(headerLine);
                StringTokenizer tokenizer = new StringTokenizer(headerLine);
                String httpMethod = tokenizer.nextToken();

                switch(httpMethod) {
                    case "POST": {
                        JSONObject obj = getJSONFromPOSTReader(bufferedReader);
                        JSONObject response = executeJSONRequest(obj);

                        SimpleDateFormat dateFormater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zz");
                        OutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
                        final String HEADERS = "HTTP/1.1 200 OK\r\n" +
                                "Date: " + dateFormater.format(new java.util.Date()) + "\r\n" +
                                "Access-Control-Allow-Origin:*\r\n" +
                                "Content-Type: application/json\r\n" +
                                "Content-Length: ";
                        String responseString = response.toString();
                        out.write((HEADERS + responseString.length() + "\r\n\r\n" + responseString).getBytes());
                        out.flush();
                        out.close();

                        System.out.println((HEADERS + responseString.length() + "\r\n\r\n" + responseString));
                        System.out.println();
                        System.out.println("Response sent.");
                        break;
                    }
                    case "GET" : {
                        OutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
                        final String HEADERS = "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: text/html\r\n" +
                                "Content-Length: ";

                        String responseString = handleGETRequest(tokenizer.nextToken());
                        out.write((HEADERS + responseString.length() + "\r\n\r\n" + responseString).getBytes());
                        out.flush();
                        out.close();
                        System.out.println("Response sent.");
                        break;
                    }
                    case "OPTIONS" : {
                        OutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
                        SimpleDateFormat dateFormater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zz");
                        final String HEADERS = "HTTP/1.1 200 OK\r\n" +
                                "Date: " + dateFormater.format(new java.util.Date()) + "\r\n" +
                                "Access-Control-Allow-Origin:*\r\n" +
                                "Access-Control-Allow-Methods: POST, GET, OPTIONS\r\n" +
                                "Access-Control-Allow-Headers: X-PINGOTHER, Content-Type\r\n" +
                                "Access-Control-Max-Age: 86400\r\n" +
                                "Vary: Accept-Encoding, Origin\r\n" +
                                "Content-Encoding: gzip\r\n" +
                                "Content-Length: 0\r\n" +
                                "Keep-Alive: timeout=2, max=100\r\n" +
                                "Connection: Keep-Alive\r\n" +
                                "Content-Type: text/plain\r\n\r\n";
                        out.write(HEADERS.getBytes());
                        out.flush();
                        out.close();
                        System.out.println("Response sent.");
                        System.out.println(HEADERS);
                        break;
                    }
                    default : {
                        System.out.println("Unsupported HTTP method : " + httpMethod);
                        OutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
                        final String HEADERS = "HTTP/1.1 400 BAD REQUEST\r\n" +
                                "Content-Type: text/text\r\n" +
                                "Content-Length: ";

                        String responseString = "Unsupported HTTP method : " + httpMethod;
                        out.write((HEADERS + responseString.length() + "\r\n\r\n" + responseString).getBytes());
                        out.flush();
                        out.close();
                        clientSocket.close();
                        break;
                    }
                }
            }
        } catch(IOException e) {
            System.err.println("Server thread encountered an error.");
            System.err.println(e.getMessage());
        }
    }
}
