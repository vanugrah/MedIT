-- Gets information for selected appointment (replace AppointmentID with the one the user chose)
SELECT 
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
	AppointmentID = 1;