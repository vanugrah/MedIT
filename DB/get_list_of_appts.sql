-- Get all uncancelled appointments for specified user
SELECT 
    AppointmentID,
    CONCAT(Parent_Fname, ' ', Parent_Lname) AS Parent,
    CONCAT(Patient_Fname, ' ', Patient_Lname) AS Patient,
    Color,
    CONCAT(Doc_Fname, ' ', Doc_Lname) AS Doctor,
    Hos_Name AS Hospital,
    Branch,
    Clinic_Specialization AS Clinic,
    Address, City, State, ZipCode,
    Date_of_appt AS Date,
    Time_of_appt AS Time,
    Cancelled,
    Checked_In,
    Notes
FROM
    Appointment NATURAL JOIN Patient NATURAL JOIN Parent NATURAL JOIN Doctor NATURAL JOIN Clinic NATURAL JOIN Hospital
WHERE
    Username = 'atsou3' AND Cancelled = 0
ORDER BY Date_of_appt , Time_of_appt;

-- Get all uncancelled future appointments for specified user
SELECT 
    AppointmentID,
    CONCAT(Parent_Fname, ' ', Parent_Lname) AS Parent,
    CONCAT(Patient_Fname, ' ', Patient_Lname) AS Patient,
    CONCAT(Doc_Fname, ' ', Doc_Lname) AS Doctor,
    Hos_Name AS Hospital,
    Branch,
    Clinic_Specialization AS Clinic,
    Date_of_appt AS Date,
    Time_of_appt AS Time,
    Notes
FROM
    Appointment NATURAL JOIN Patient NATURAL JOIN Parent NATURAL JOIN Doctor NATURAL JOIN Clinic NATURAL JOIN Hospital
WHERE
    Username = 'atsou3' AND Cancelled = 0 AND Date_of_appt > CURDATE()
ORDER BY Date_of_appt , Time_of_appt;
    
-- Get all uncancelled past appointments for specified user
SELECT 
    AppointmentID,
    CONCAT(Parent_Fname, ' ', Parent_Lname) AS Parent,
    CONCAT(Patient_Fname, ' ', Patient_Lname) AS Patient,
    CONCAT(Doc_Fname, ' ', Doc_Lname) AS Doctor,
    Hos_Name AS Hospital,
    Branch,
    Clinic_Specialization AS Clinic,
    Date_of_appt AS Date,
    Time_of_appt AS Time,
    Notes
FROM
    Appointment NATURAL JOIN Patient NATURAL JOIN Parent NATURAL JOIN Doctor NATURAL JOIN Clinic NATURAL JOIN Hospital
WHERE
    Username = 'atsou3' AND Cancelled = 0 AND Date_of_appt < CURDATE()
ORDER BY Date_of_appt , Time_of_appt;