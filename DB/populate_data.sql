-- Parent
INSERT INTO parent() 
	VALUES ('atsou3', 'password', 'Anthony', 'Tsou', '678-777-8888', 'atsou3@gatech.edu');
INSERT INTO parent() 
	VALUES ('avijay3', 'password', 'Anugrah', 'Vijay', '404-333-4444', 'avijay3@gatech.edu');
INSERT INTO parent() 
	VALUES ('skim3', 'password', 'Sado', 'Kim', '770-111-5555', 'skim3@gatech.edu');
INSERT INTO parent() 
	VALUES ('pbothra3', 'password', 'Pratik', 'Bothra', '404-222-9999', 'pbothra3@gatech.edu');
INSERT INTO parent() 
	VALUES ('mbarulic3', 'password', 'Matthew', 'Barulic', '678-123-4567', 'mbarulic3@gatech.edu');
    
-- Patient
INSERT INTO patient(Patient_Fname, Patient_Lname, Age, Sex, Username) 
	VALUES ('Junior', 'Tsou', 12, 'M', 'atsou3');
INSERT INTO patient(Patient_Fname, Patient_Lname, Age, Sex, Username) 
	VALUES ('Antoinette', 'Tsou', 10, 'F', 'atsou3');
INSERT INTO patient(Patient_Fname, Patient_Lname, Age, Sex, Username) 
	VALUES ('Junior', 'Vijay', 8, 'M', 'avijay3');
INSERT INTO patient(Patient_Fname, Patient_Lname, Age, Sex, Username) 
	VALUES ('Faith', 'Kim', 13, 'F', 'skim3');
INSERT INTO patient(Patient_Fname, Patient_Lname, Age, Sex, Username) 
	VALUES ('Hope', 'Kim', 9, 'F', 'skim3');
INSERT INTO patient(Patient_Fname, Patient_Lname, Age, Sex, Username) 
	VALUES ('Love', 'Kim', 7, 'F', 'skim3');
INSERT INTO patient(Patient_Fname, Patient_Lname, Age, Sex, Username) 
	VALUES ('Junior', 'Bothra', 7, 'M', 'pbothra3');
INSERT INTO patient(Patient_Fname, Patient_Lname, Age, Sex, Username) 
	VALUES ('Matilda', 'Barulic', 5, 'F', 'mbarulic3');
    
-- Doctor
INSERT INTO doctor(Doc_Fname, Doc_Lname, Doc_Specialization) 
	VALUES ('Donald', 'Batisky', 'Pediatrics');
INSERT INTO doctor(Doc_Fname, Doc_Lname, Doc_Specialization) 
	VALUES ('Olufisayo', 'Omojokun', 'Radiology');
INSERT INTO doctor(Doc_Fname, Doc_Lname, Doc_Specialization) 
	VALUES ('Olga', 'Menagarishvili', 'Cardiology');
    
-- Hospital
INSERT INTO hospital(Hos_Name, Branch, Address, City, State, ZipCode)
	VALUES ('Children\'s Healthcare of Atlanta', 'Egelston', '1405 Clifton Rd', 'Atlanta', 'GA', 30329);
INSERT INTO hospital(Hos_Name, Branch, Address, City, State, ZipCode)
	VALUES ('Children\'s Healthcare of Atlanta', 'Hughes Spalding', '35 Jesse Hill Jr Dr SE', 'Atlanta', 'GA', 30303);
INSERT INTO hospital(Hos_Name, Branch, Address, City, State, ZipCode)
	VALUES ('Emory University Hospital', 'Main', '1364 Clifton Rd', 'Atlanta', 'GA', 30322);
INSERT INTO hospital(Hos_Name, Branch, Address, City, State, ZipCode)
	VALUES ('Children\'s Healthcare of Atlanta', 'North Point', '3795 Mansell Road', 'Alpharetta', 'GA', 30022);
    
-- Clinic
INSERT INTO clinic(Clinic_Specialization, HospitalID, Clinic_Phone)
	VALUES ('Cardiology', 1, '404-256-2593');
INSERT INTO clinic(Clinic_Specialization, HospitalID, Clinic_Phone)
	VALUES ('Diabetes', 2, '404-785-1724');
INSERT INTO clinic(Clinic_Specialization, HospitalID, Clinic_Phone)
	VALUES ('Primary Care', 2, '404-785-9850');
INSERT INTO clinic(Clinic_Specialization, HospitalID, Clinic_Phone)
	VALUES ('Radiology', 2, '404-785-9988');
INSERT INTO clinic(Clinic_Specialization, HospitalID, Clinic_Phone)
	VALUES ('Pediatrics', 4, '404-785-5437');
INSERT INTO clinic(Clinic_Specialization, HospitalID, Clinic_Phone)
	VALUES ('Transplantation', 3, '404-778-7777');
    
-- Preference
INSERT INTO preference()
	VALUES ('atsou3', 1, 1, 1);
INSERT INTO preference()
	VALUES ('avijay3', 1, 0, 1);
INSERT INTO preference()
	VALUES ('skim3', 0, 1, 1);
INSERT INTO preference()
	VALUES ('pbothra3', 0, 0, 0);
INSERT INTO preference()
	VALUES ('mbarulic3', 0, 0, 1);
    
-- Appointment
INSERT INTO appointment(PatientID, Username, DoctorID, ClinicID, Date_of_appt, Time_of_appt, Confirmed, Checked_In, Cancelled, Notes)
	VALUE(1, 'atsou3', 1, 5, '2016-05-02', '12:30:00', 0, 0, 0, 'Check-up');
INSERT INTO appointment(PatientID, Username, DoctorID, ClinicID, Date_of_appt, Time_of_appt, Confirmed, Checked_In, Cancelled, Notes)
	VALUE(3, 'avijay3', 2, 4, '2016-06-05', '12:45:00', 0, 0, 0, NULL);
INSERT INTO appointment(PatientID, Username, DoctorID, ClinicID, Date_of_appt, Time_of_appt, Confirmed, Checked_In, Cancelled, Notes)
	VALUE(4, 'skim3', 2, 4, '2016-04-17', '13:15:00', 0, 0, 1, 'No comment');
INSERT INTO appointment(PatientID, Username, DoctorID, ClinicID, Date_of_appt, Time_of_appt, Confirmed, Checked_In, Cancelled, Notes)
	VALUE(7, 'pbothra3', 3, 1, '2016-03-15', '14:30:00', 1, 1, 0,'Condition seems normal but check-up in 2 weeks just in case');
INSERT INTO appointment(PatientID, Username, DoctorID, ClinicID, Date_of_appt, Time_of_appt, Confirmed, Checked_In, Cancelled, Notes)
	VALUE(8, 'mbarulic3', 1, 5, '2016-07-19', '08:45:00', 0, 0, 0, 'N/A');
INSERT INTO appointment(PatientID, Username, DoctorID, ClinicID, Date_of_appt, Time_of_appt, Confirmed, Checked_In, Cancelled, Notes)
	VALUE(3, 'avijay3', 1, 5, '2016-02-22', '09:00:00', 1, 1, 1, 'Prescribed medicine');
INSERT INTO appointment(PatientID, Username, DoctorID, ClinicID, Date_of_appt, Time_of_appt, Confirmed, Checked_In, Cancelled, Notes)
	VALUE(2, 'atsou3', 2, 4, '2016-04-20', '10:30:00', 1, 0, 0, 'Notes notes notes notes');
INSERT INTO appointment(PatientID, Username, DoctorID, ClinicID, Date_of_appt, Time_of_appt, Confirmed, Checked_In, Cancelled, Notes)
	VALUE(5, 'skim3', 3, 1, '2016-05-30', '11:45:00', 0, 0, 0, NULL);
INSERT INTO appointment(PatientID, Username, DoctorID, ClinicID, Date_of_appt, Time_of_appt, Confirmed, Checked_In, Cancelled, Notes)
	VALUE(4, 'skim3', 1, 5, '2016-06-14', '10:45:00', 0, 0, 0, NULL);