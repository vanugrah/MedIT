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
import java.util.Date;
import java.util.List;

/**
 * Created by matt on 4/4/2016.
 */
public class DatabaseManager {

    // TODO : Crashes occasionally with null connection objects

    private static final String MySQLIP = "localhost:3306";
    private static final String MySQLDatabaseName = "test";
    private static final String MySQLUsername = "serverUser";
    private static final String MySQLPassword = "gtsecret";

    public static Connection getConnection() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + MySQLIP + "/" + MySQLDatabaseName + "?" +
                    "user=" + MySQLUsername + "&password=" + MySQLPassword + "&useSSL=false");
        } catch (SQLException e) {
            e.printStackTrace();
            if(connection != null) {
                try {
                    connection.close();
                } catch(SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return connection;
    }

    public static void saveSettings(String username, boolean getsPush, boolean getsSMS, boolean getsEmail) {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            connection = DatabaseManager.getConnection();

            statement = connection.createStatement();
            statement.execute("UPDATE Preference SET Gets_Push = b'" + (getsPush ? "1" : "0") + "' WHERE Username = '" + username + "';");
            statement.execute("UPDATE Preference SET Gets_SMS = b'" + (getsSMS ? "1" : "0") + "' WHERE Username = '" + username + "';");
            statement.execute("UPDATE Preference SET Gets_Email = b'" + (getsEmail ? "1" : "0") + "' WHERE Username = '" + username + "';");
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
            connection = DatabaseManager.getConnection();

            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM Parent WHERE Username = '" + username + "';");

            User user = new User();

            if(results.next()) {
                user.username = username;
                user.firstName = results.getString("Parent_Fname");
                user.lastName = results.getString("Parent_Lname");
                user.phoneNumber = results.getString("Parent_Phone");
                user.emailAddress = results.getString("Email");
                user.photo = results.getString("Parent_Photo");
            } else {
                results.close();
                statement.close();
                connection.close();
                return null;
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

    public static List<Patient> getPatientsForUser(User user) {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        List<Patient> patients = new ArrayList<>();

        try {
            connection = DatabaseManager.getConnection();

            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM Patient WHERE Username = '" + user.username + "';");

            while(results.next()) {
                Patient patient = new Patient();

                patient.patientID = results.getInt("PatientID");
                patient.firstName = results.getString("Patient_Fname");
                patient.lastName = results.getString("Patient_Lname");
                patient.age = results.getInt("Age");
                patient.sex = results.getString("Sex").equals("M") ? Patient.Sex.Male : Patient.Sex.Female;
                patient.color = results.getString("Color");
                patient.insuranceID = results.getString("InsuranceID");
                patient.insuranceProvider = results.getString("InsuranceProvider");
                patient.photo = results.getString("Patient_Photo");

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
            connection = DatabaseManager.getConnection();

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
            connection = DatabaseManager.getConnection();

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
            connection = DatabaseManager.getConnection();

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
            connection = DatabaseManager.getConnection();

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
                patient.color = results.getString("Color");

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

    public static void saveDateOfLastReminder(Appointment appointment, Date dateOfLastReminder) {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            connection = DatabaseManager.getConnection();

            java.sql.Date date = new java.sql.Date(dateOfLastReminder.getTime());

            statement = connection.createStatement();
            statement.execute("UPDATE Appointment SET Date_of_last_reminder = \"" + date + "\" WHERE AppointmentID = " +
                    appointment.appointmentID + ";");

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

    public static void saveColorForPatient(int patientID, String color) {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            connection = DatabaseManager.getConnection();

            statement = connection.createStatement();
            statement.execute("UPDATE Patient SET Color = \"" + color + "\" WHERE PatientID = '" + patientID + "';");
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
}
