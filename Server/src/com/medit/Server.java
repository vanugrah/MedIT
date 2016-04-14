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
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by matt on 4/2/2016.
 */
public class Server implements Runnable {

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
                Patient p = DatabaseManager.getPatient(id);
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
            case "AppointmentsQuery": {
                System.out.println("Appointments Query received.");
                int patientID = root.getInt("PatientID");
                Patient p = DatabaseManager.getPatient(patientID);
                if(p == null) {
                    response.put("MessageType", "Error");
                    response.put("Message", "Could not find patient with ID " + patientID);
                } else {
                    List<Appointment> appointments = p.getAppointments();
                    response.put("MessageType", "AppointmentsQueryResults");
                    response.put("Appointments", new JSONArray());
                    for (Appointment app : appointments) {
                        response.append("Appointments", app.toJSON());
                    }
                }
                break;
            }
            case "UserCreate" : {
                System.out.println("User creation request received.");
                User user = new User();
                user.username = root.getString("Username");
                String password = root.getString("Password");
                user.phoneNumber = root.getString("PhoneNumber");
                user.emailAddress = root.getString("EmailAddress");
                user.firstName = root.getString("FirstName");
                user.lastName = root.getString("LastName");
                DatabaseManager.createNewUser(user, password);
                response.put("MessageType", "UserCreateConfirmation");
                response.put("User", user.toJSON());
                break;
            }
            case "PatientCreate": {
                System.out.println("Patient creation request received.");
                String sex = root.getString("Sex");
                Patient patient = new Patient();
                patient.user.username = root.getString("Username");
                patient.firstName = root.getString("FirstName");
                patient.lastName = root.getString("LastName");
                patient.age = root.getInt("Age");
                patient.sex = (root.getString("Sex") == "M" ? Patient.Sex.Male : Patient.Sex.Female);
                DatabaseManager.createNewPatient(patient);
                response.put("MessageType", "PatientCreateConfirmation");
                response.put("Patient", patient.toJSON());
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
                // TODO : Appointments should have IDs
                break;
            }
            case "CancelAppointment" : {
                System.out.println("Appointment cancellation received.");
                break;
            }
            case "CheckInForAppointment" : {
                System.out.println("Appointment check-in received.");
                break;
            }
        }
        return response;
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
            System.err.println(e.getMessage());
        }
    }
}
