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
 * Provides an interface for other parts of the Server to access the medIT database.
 *
 * Created by matt on 4/4/2016.
 */
public class DatabaseManager {

    private static final String MySQLIP = "localhost:3306";
    private static final String MySQLDatabaseName = "medit";
    private static final String MySQLUsername = "serverUser";
    private static final String MySQLPassword = "gtsecret";

    private static Object connectionLock = new Object();
    public static Object databaseLock = new Object();

    /**
     * Establishes a connection to the medIT MySQL database with appropriate authentication.
     *
     * @return A connection object used to communicated with the MySQL database.
     */
    public static Connection getConnection() {
        synchronized (connectionLock) {
            Connection connection = null;

            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + MySQLIP + "/" + MySQLDatabaseName + "?" +
                        "user=" + MySQLUsername + "&password=" + MySQLPassword + "&useSSL=false");
            } catch (SQLException e) {
                e.printStackTrace();

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            return connection;
        }
    }

    /**
     * Saves the given settings values for the given username
     *
     * @param username
     * @param getsPush
     * @param getsSMS
     * @param getsEmail
     */
    public static void saveSettings(String username, boolean getsPush, boolean getsSMS, boolean getsEmail) {
        synchronized (databaseLock) {
            Connection connection = null;
            Statement statement = null;
            ResultSet results = null;

            try {
                connection = DatabaseManager.getConnection();

                statement = connection.createStatement();
                statement.execute("UPDATE Preference SET Gets_Push = b'" + (getsPush ? "1" : "0") + "' WHERE Username = '" + username + "';");
                statement.execute("UPDATE Preference SET Gets_SMS = b'" + (getsSMS ? "1" : "0") + "' WHERE Username = '" + username + "';");
                statement.execute("UPDATE Preference SET Gets_Email = b'" + (getsEmail ? "1" : "0") + "' WHERE Username = '" + username + "';");
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
     * Pulls user information from the database for the given username.
     *
     * @param username
     * @return A populated User instance if the given username is found. A null object if the username is not found.
     */
    public static User getUser(String username) {
        synchronized (databaseLock) {
            Connection connection = null;
            Statement statement = null;
            ResultSet results = null;

            try {
                connection = DatabaseManager.getConnection();

                statement = connection.createStatement();
                results = statement.executeQuery("SELECT * FROM Parent WHERE Username = '" + username + "';");

                User user = new User();

                if (results.next()) {
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

                if (results.next()) {
                    user.preferences.getsPush = results.getBoolean("Gets_Push");
                    user.preferences.getsSMS = results.getBoolean("Gets_SMS");
                    user.preferences.getsEmail = results.getBoolean("Gets_Email");
                }
                results.close();
                statement.close();

                return user;

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
     * Pulls a list of all patients registered for the given user from the database.
     * @param user
     * @return List of patients registered for the given user. If the user is not found in the database, the list will
     * be empty.
     */
    public static List<Patient> getPatientsForUser(User user) {
        synchronized (databaseLock) {
            Connection connection = null;
            Statement statement = null;
            ResultSet results = null;

            List<Patient> patients = new ArrayList<>();

            try {
                connection = DatabaseManager.getConnection();

                statement = connection.createStatement();
                results = statement.executeQuery("SELECT * FROM Patient WHERE Username = '" + user.username + "';");

                while (results.next()) {
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

            return patients;
        }
    }

    /**
     * Checks provided login credentials against those stored in the database.
     *
     * @param username
     * @param password
     * @return True if the given username and password are valid. False if the username is not found or if the given
     * password is incorrect.
     */
    public static boolean verifyLoginCredentials(String username, String password) {
        synchronized (databaseLock) {
            Connection connection = null;
            Statement statement = null;
            ResultSet results = null;

            try {
                connection = DatabaseManager.getConnection();

                statement = connection.createStatement();
                results = statement.executeQuery("SELECT COUNT(*) FROM Parent WHERE Username = '" +
                        username + "' AND Password = '" + password + "';");

                if (results.next()) {
                    if (results.getInt(1) == 1) {
                        return true;
                    } else {
                        return false;
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

            return false;
        }
    }

    /**
     * Retrieves list of all patients associated with medIT users.
     *
     * @return List of patients in database
     */
    public static List<Patient> getAllPatients() {
        synchronized (databaseLock) {
            Connection connection = null;
            Statement statement = null;
            ResultSet results = null;

            List<Patient> ret = new ArrayList<>();

            try {
                connection = DatabaseManager.getConnection();

                statement = connection.createStatement();
                results = statement.executeQuery("SELECT * FROM Patient");

                while (results.next()) {
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
     * Writes the given date of last reminder to the database storage for the given appointment.
     * @param appointment
     * @param dateOfLastReminder
     */
    public static void saveDateOfLastReminder(Appointment appointment, Date dateOfLastReminder) {
        synchronized (databaseLock) {
            Connection connection = null;
            Statement statement = null;
            ResultSet results = null;

            try {
                connection = DatabaseManager.getConnection();

                java.sql.Date date = new java.sql.Date(dateOfLastReminder.getTime());

                statement = connection.createStatement();
                statement.execute("UPDATE Appointment SET Date_of_last_reminder = \"" + date + "\" WHERE AppointmentID = " +
                        appointment.appointmentID + ";");

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
     * Writes the given color preference to the database storage for the patient with the given ID.
     * @param patientID
     * @param color
     */
    public static void saveColorForPatient(int patientID, String color) {
        synchronized (databaseLock) {
            Connection connection = null;
            Statement statement = null;
            ResultSet results = null;

            try {
                connection = DatabaseManager.getConnection();

                statement = connection.createStatement();
                statement.execute("UPDATE Patient SET Color = \"" + color + "\" WHERE PatientID = '" + patientID + "';");
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
