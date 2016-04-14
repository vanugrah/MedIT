package com.medit;

import com.medit.db.Appointment;
import com.medit.db.Patient;
import com.medit.db.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matt on 4/4/2016.
 */
public class DatabaseManager {

    public static void main(String[] args) {

    }

    public static void saveSettings(String username, boolean getsPush, boolean getsSMS, boolean getsEmail) {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?" +
                    "user=serverUser&password=gtsecret&useSSL=false");

            statement = connection.createStatement();
            if(!statement.execute("UPDATE Preference SET Gets_Push = " + (getsPush ? "1" : "0") + " WHERE Username = '" + username + "';")) {
                System.err.println("Unable to set GetsPush for " + username);
            }
            if(!statement.execute("UPDATE Preference SET Gets_SMS = " + (getsSMS ? "1" : "0") + " WHERE Username = '" + username + "';")) {
                System.err.println("Unable to set GetsSMS for " + username);
            }
            if(!statement.execute("UPDATE Preferences SET Gets_Email = " + (getsEmail ? "1" : "0") + " WHERE Username = '" + username + "';")) {
                System.err.println("Unable to set GetsEmail for " + username);
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
    }

    public static User getUser(String username) {

        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?" +
                    "user=serverUser&password=gtsecret&useSSL=false");

            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM Parent WHERE Username = '" + username + "';");

            User user = new User();

            if(results.next()) {
                user.username = username;
                user.firstName = results.getString("Parent_Fname");
                user.lastName = results.getString("Parent_Lname");
                user.phoneNumber = results.getString("Parent_Phone");
                user.emailAddress = results.getString("Email");
            }
            results.close();

            results = statement.executeQuery("SELECT * FROM Preference WHERE Username = '" + user.username + "';");

            if(results.next()) {
                user.preferences.getsPush = results.getBoolean("Gets_Push");
                user.preferences.getsSMS = results.getBoolean("Gets_SMS");
                user.preferences.getsEmail = results.getBoolean("Gets_Email");
            }
            results.close();
            statement.close();

            return user;

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

    public static Patient getPatient(int patientID) {
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

                patient.user = getUser(results.getString("Username"));

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

    public static List<Patient> getPatientsForUser(User user) {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        List<Patient> patients = new ArrayList<>();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?" +
                    "user=serverUser&password=gtsecret&useSSL=false");

            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM Patient WHERE Username = '" + user.username + "';");

            while(results.next()) {
                Patient patient = new Patient();

                patient.patientID = results.getInt("PatientID");
                patient.firstName = results.getString("Patient_Fname");
                patient.lastName = results.getString("Patient_Lname");
                patient.age = results.getInt("Age");
                patient.sex = results.getString("Sex").equals("M") ? Patient.Sex.Male : Patient.Sex.Female;

                patient.user = getUser(results.getString("Username"));

                patients.add(patient);
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

        return patients;
    }

    public static boolean verifyLoginCredentials(String username, String password) {

        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?" +
                    "user=serverUser&password=gtsecret&useSSL=false");

            statement = connection.createStatement();
            results = statement.executeQuery("SELECT COUNT(*) FROM Parent WHERE Username = '" +
                    username + "' AND Password = '" + password + "';");

            if(results.next()) {
                if(results.getInt(1) == 1) {
                    return true;
                } else {
                    return false;
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

        return false;
    }

    public static Patient createNewPatient(Patient patient) {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?" +
                    "user=serverUser&password=gtsecret&useSSL=false");

            statement = connection.createStatement();

            statement.execute("INSERT INTO patient(Patient_Fname, Patient_Lname, Age, Sex, Username) VALUES ('" +
                    patient.firstName + "', '" +
                    patient.lastName + "', '" +
                    patient.age + "', '" +
                    patient.sex.toString() + "', '" +
                    patient.user.username + "');");

            results = statement.executeQuery("SELECT PatientID FROM Patient " +
                    "WHERE Username = '" + patient.user.username  + "' " +
                    "AND Parent_Fname = '" + patient.firstName + "' " +
                    "AND Parent_Lname = '" + patient.lastName + "';");

            if(results.next()) {
                patient.patientID = results.getInt("PatientID");
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

    public static User createNewUser(User user, String password) {

        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?" +
                    "user=serverUser&password=gtsecret&useSSL=false");

            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM Parent WHERE Username = '" + user.username + "';");

            if(results.next()) {
                // username already taken
                return null;
            } else {
                results.close();

                statement.execute("INSERT INTO parent() VALUES ('" +
                        user.username + "', '" +
                        password + "', '" +
                        user.firstName + "', '" +
                        user.lastName + "', '" +
                        user.phoneNumber + "', '" +
                        user.emailAddress + "');"
                );

                statement.close();
                return user;
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

    public static List<Patient> getAllPatients() {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        List<Patient> ret = new ArrayList<>();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?" +
                    "user=serverUser&password=gtsecret&useSSL=false");

            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM Patient");

            while(results.next()) {
                Patient patient = new Patient();

                patient.patientID = results.getInt("PatientID");
                patient.firstName = results.getString("Patient_Fname");
                patient.lastName = results.getString("Patient_Lname");
                patient.age = results.getInt("Age");
                patient.sex = results.getString("Sex").equals("M") ? Patient.Sex.Male : Patient.Sex.Female;
                patient.user.username = results.getString("Username");

                ret.add(patient);
            }
            results.close();
            statement.close();

            for (Patient patient : ret) {

                patient.user = getUser(patient.user.username);

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
}
