package com.medit;

import com.medit.db.Appointment;
import com.medit.db.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matt on 4/4/2016.
 */
public class EPICManager {

    public static Patient getPatientInformation(int patientID) {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?" +
                    "user=serverUser&password=gtsecret&useSSL=false");

            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM Patient WHERE PatientID = '" + patientID + "';");

            if(results.next()) {
                Patient patient = new Patient();

                patient.patientID = patientID;
                patient.firstName = results.getString("Patient_Fname");
                patient.lastName = results.getString("Patient_Lname");
                patient.age = results.getInt("Age");
                patient.sex = results.getString("Sex").equals("M") ? Patient.Sex.Male : Patient.Sex.Female;

                patient.user = DatabaseManager.getUser(results.getString("Username"));

                return patient;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(results != null) {
                try {
                    results.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }

            if(statement != null) {
                try {
                    statement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static List<Appointment> getAppointmentsForPatient(Patient patient) {
        List<Appointment> ret = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?" +
                    "user=serverUser&password=gtsecret&useSSL=false");

            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM Appointment");

            while(results.next()) {
                Appointment appointment = new Appointment();

                appointment.patient = patient;
                appointment.user.username = results.getString("Username");
                appointment.doctor.doctorID = results.getInt("DoctorID");
                appointment.clinic.clinicID = results.getInt("ClinicID");
                java.sql.Date date = results.getDate("Date_of_appt");
                java.sql.Time time = results.getTime("Time_of_appt");
                appointment.date = new java.util.Date(date.getTime() + time.getTime());
                appointment.Confirmed = results.getBoolean("Confirmed");
                appointment.CheckedIn = results.getBoolean("Checked_In");
                appointment.Cancelled = results.getBoolean("Cancelled");

                ret.add(appointment);
            }
            results.close();
            statement.close();

            for(Appointment appointment : ret) {
                statement = connection.createStatement();
                results = statement.executeQuery("SELECT * FROM Parent WHERE Username='" + appointment.user.username + "';");
                if(results.next()) {
                    appointment.user.emailAddress = results.getString("Email");
                    appointment.user.firstName = results.getString("Parent_Fname");
                    appointment.user.lastName = results.getString("Parent_Lname");
                    appointment.user.phoneNumber = results.getString("Parent_Phone");
                }
                results.close();
                statement.close();

                statement = connection.createStatement();
                results = statement.executeQuery("SELECT * FROM Doctor WHERE DoctorID='" + appointment.doctor.doctorID + "';");
                if(results.next()) {
                    appointment.doctor.firstName = results.getString("Doc_Fname");
                    appointment.doctor.lastName = results.getString("Doc_Lname");
                    appointment.doctor.specialization = results.getString("Doc_Specialization");
                }
                results.close();
                statement.close();

                statement = connection.createStatement();
                results = statement.executeQuery("SELECT * FROM Clinic WHERE ClinicID='" + appointment.clinic.clinicID + "';");
                if(results.next()) {
                    appointment.clinic.hospital.hospitalID = results.getInt("HospitalID");
                    appointment.clinic.specialization = results.getString("Clinic_Specialization");
                    appointment.clinic.phoneNumber = results.getString("Clinic_Phone");
                }
                results.close();
                statement.close();

                statement = connection.createStatement();
                results = statement.executeQuery("SELECT * FROM Hospital WHERE HospitalID='" + appointment.clinic.clinicID + "';");
                if(results.next()) {
                    appointment.clinic.hospital.name = results.getString("Hos_Name");
                    appointment.clinic.hospital.branch = results.getString("Branch");
                    appointment.clinic.hospital.address = results.getString("Address");
                    appointment.clinic.hospital.city = results.getString("City");
                    appointment.clinic.hospital.state = results.getString("State");
                    appointment.clinic.hospital.zipCode = results.getString("ZipCode");
                }
            }

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(results != null) {
                try {
                    results.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }

            if(statement != null) {
                try {
                    statement.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    public static void confirmAppointment(int appointmentID) {

    }

    public static void cancelAppointment(int appointmentID) {

    }

    public static void checkInForAppointment(int appointmentID) {

    }



}
