package com.medit;

import com.medit.db.Appointment;
import com.medit.db.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides an interface for other parts of the Server to interact with the Epic system.
 *
 * NOTE: Currently, due to a lack of access to a testable Epic instance, this implementation simply connects to the
 * medIT database, which holds a mock version of data needed from Epic. Some method signatures may need to change to
 * port this to an actual Epic connection.
 *
 * Created by matt on 4/4/2016.
 */
public class EpicManager {

    /**
     * Retrieves information for the patient with the given patient ID.
     *
     * @param patientID
     * @return A populated Patient instance, or a null instance if the given patient ID is not found.
     */
    public static Patient getPatientInformation(int patientID) {
        synchronized (DatabaseManager.databaseLock) {
            Connection connection = null;
            Statement statement = null;
            ResultSet results = null;

            try {
                connection = DatabaseManager.getConnection();

                statement = connection.createStatement();
                results = statement.executeQuery("SELECT * FROM Patient WHERE PatientID = '" + patientID + "';");

                if (results.next()) {
                    Patient patient = new Patient();

                    patient.patientID = patientID;
                    patient.firstName = results.getString("Patient_Fname");
                    patient.lastName = results.getString("Patient_Lname");
                    patient.age = results.getInt("Age");
                    patient.sex = results.getString("Sex").equals("M") ? Patient.Sex.Male : Patient.Sex.Female;

                    patient.user = DatabaseManager.getUser(results.getString("Username"));

                    return patient;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (results != null) {
                    try {
                        results.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }
    }

    /**
     * Retrieves all appointments for the given patient.
     *
     * @param patient
     * @return List of appointments for patient, or empty list if patient is not found in Epic system.
     */
    public static List<Appointment> getAppointmentsForPatient(Patient patient) {
        synchronized (DatabaseManager.databaseLock) {
            List<Appointment> ret = new ArrayList<>();
            Connection connection = null;
            Statement statement = null;
            ResultSet results = null;

            try {
                connection = DatabaseManager.getConnection();

                statement = connection.createStatement();
                results = statement.executeQuery("SELECT * FROM Appointment WHERE PatientID = " + patient.patientID);

                while (results.next()) {
                    Appointment appointment = new Appointment();

                    appointment.appointmentID = results.getInt("AppointmentID");
                    appointment.patient = patient;
                    appointment.user.username = results.getString("Username");
                    appointment.doctor.doctorID = results.getInt("DoctorID");
                    appointment.clinic.clinicID = results.getInt("ClinicID");
                    java.sql.Timestamp date = results.getTimestamp("Date_of_appt");
                    appointment.date = new java.util.Date(date.getTime());
                    appointment.Confirmed = results.getBoolean("Confirmed");
                    appointment.CheckedIn = results.getBoolean("Checked_In");
                    appointment.Cancelled = results.getBoolean("Cancelled");
                    appointment.notes = results.getString("Notes");
                    java.sql.Date dateOfLastReminder = results.getDate("Date_of_last_reminder");
                    if (dateOfLastReminder == null) {
                        appointment.dateOfLastReminder = null;
                    } else {
                        appointment.dateOfLastReminder = new java.util.Date(dateOfLastReminder.getTime());
                    }

                    ret.add(appointment);
                }
                results.close();
                statement.close();

                for (Appointment appointment : ret) {
                    statement = connection.createStatement();
                    results = statement.executeQuery("SELECT * FROM Parent WHERE Username='" + appointment.user.username + "';");
                    if (results.next()) {
                        appointment.user.emailAddress = results.getString("Email");
                        appointment.user.firstName = results.getString("Parent_Fname");
                        appointment.user.lastName = results.getString("Parent_Lname");
                        appointment.user.phoneNumber = results.getString("Parent_Phone");
                    }
                    results.close();
                    statement.close();

                    statement = connection.createStatement();
                    results = statement.executeQuery("SELECT * FROM Preference WHERE Username='" + appointment.user.username + "';");
                    if (results.next()) {
                        appointment.user.preferences.getsEmail = results.getBoolean("Gets_Email");
                        appointment.user.preferences.getsSMS = results.getBoolean("Gets_SMS");
                        appointment.user.preferences.getsPush = results.getBoolean("Gets_Push");
                    }
                    results.close();
                    statement.close();

                    statement = connection.createStatement();
                    results = statement.executeQuery("SELECT * FROM Doctor WHERE DoctorID='" + appointment.doctor.doctorID + "';");
                    if (results.next()) {
                        appointment.doctor.firstName = results.getString("Doc_Fname");
                        appointment.doctor.lastName = results.getString("Doc_Lname");
                        appointment.doctor.specialization = results.getString("Doc_Specialization");
                    }
                    results.close();
                    statement.close();

                    statement = connection.createStatement();
                    results = statement.executeQuery("SELECT * FROM Clinic WHERE ClinicID='" + appointment.clinic.clinicID + "';");
                    if (results.next()) {
                        appointment.clinic.hospital.hospitalID = results.getInt("HospitalID");
                        appointment.clinic.specialization = results.getString("Clinic_Specialization");
                        appointment.clinic.phoneNumber = results.getString("Clinic_Phone");
                    }
                    results.close();
                    statement.close();

                    statement = connection.createStatement();
                    results = statement.executeQuery("SELECT * FROM Hospital WHERE HospitalID='" + appointment.clinic.hospital.hospitalID + "';");
                    if (results.next()) {
                        appointment.clinic.hospital.name = results.getString("Hos_Name");
                        appointment.clinic.hospital.branch = results.getString("Branch");
                        appointment.clinic.hospital.address = results.getString("Address");
                        appointment.clinic.hospital.city = results.getString("City");
                        appointment.clinic.hospital.state = results.getString("State");
                        appointment.clinic.hospital.zipCode = results.getString("ZipCode");
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (results != null) {
                    try {
                        results.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            return ret;
        }
    }

    /**
     * Marks the appointment for the given appointment ID as confirmed.
     *
     * Right now, this raises a flag in the medIT database, but a final implementation should raise a similar flag in
     * the Epic system, or add a note to the appointment visible to the clinic's scheduler.
     *
     * @param appointmentID
     */
    public static void confirmAppointment(int appointmentID) {
        synchronized (DatabaseManager.databaseLock) {
            Connection connection = null;
            Statement statement = null;
            ResultSet results = null;

            try {
                connection = DatabaseManager.getConnection();

                statement = connection.createStatement();
                statement.execute("UPDATE Appointment SET Confirmed = b'1' WHERE AppointmentID = " + appointmentID + ";");

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (results != null) {
                    try {
                        results.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Marks the appointment for the given appointment ID as cancelled.
     *
     * Right now, this raises a flag in the medIT database, but a final implementation should raise a similar flag in
     * the Epic system, or add a note to the appointment visible to the clinic's scheduler.
     *
     * @param appointmentID
     */
    public static void cancelAppointment(int appointmentID) {
        synchronized (DatabaseManager.databaseLock) {
            Connection connection = null;
            Statement statement = null;
            ResultSet results = null;

            try {
                connection = DatabaseManager.getConnection();

                statement = connection.createStatement();
                statement.execute("UPDATE Appointment SET Cancelled = b'1' WHERE AppointmentID = " + appointmentID + ";");

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (results != null) {
                    try {
                        results.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Marks the appointment for the given appointment ID to reflect that the patient has checked-in at the clinic.
     *
     * Right now, this raises a flag in the medIT database, but a final implementation should raise a similar flag in
     * the Epic system, or add a note to the appointment visible to the clinic's scheduler.
     *
     * @param appointmentID
     */
    public static void checkInForAppointment(int appointmentID) {
        synchronized (DatabaseManager.databaseLock) {
            Connection connection = null;
            Statement statement = null;
            ResultSet results = null;

            try {
                connection = DatabaseManager.getConnection();

                statement = connection.createStatement();
                statement.execute("UPDATE Appointment SET Checked_In = b'1' WHERE AppointmentID = " + appointmentID + ";");

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (results != null) {
                    try {
                        results.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
