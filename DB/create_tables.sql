DROP TABLE IF EXISTS Appointment, Clinic, Hospital, Doctor, Patient, Preference, Parent;

CREATE TABLE Parent (
	Username varchar(30) NOT NULL,
	Password varchar(30) NOT NULL,
	Parent_Fname varchar(255) NOT NULL,
	Parent_Lname varchar(255) NOT NULL,
	Parent_Phone char(12) NOT NULL,
	Email varchar(255) NOT NULL,
    Parent_Photo varchar(255),
	PRIMARY KEY (Username)
) ENGINE = InnoDB;

CREATE TABLE Patient (
	PatientID int NOT NULL AUTO_INCREMENT,
	Patient_Fname varchar(255) NOT NULL,
	Patient_Lname varchar(255) NOT NULL,
    Age int(3) NOT NULL,
    Sex ENUM('M', 'F') NOT NULL,
	Color varchar(255),
    InsuranceID varchar(255) NOT NULL,
    InsuranceProvider varchar(255) NOT NULL,
    Patient_Photo varchar(255),
	Username varchar(255) NOT NULL,
	PRIMARY KEY (PatientID, Username),
	FOREIGN KEY (Username)
		REFERENCES Parent(Username)
		ON UPDATE CASCADE
		ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE Preference (
	Username varchar(255) NOT NULL,
	Gets_Push bit(1) NOT NULL,
	Gets_SMS bit(1) NOT NULL,
	Gets_Email bit(1) NOT NULL,
    Gets_Weather bit(1) NOT NULL,
    Gets_Traffic bit(1) NOT NULL,
	PRIMARY KEY (Username),
	FOREIGN KEY (Username)
		REFERENCES Parent(Username)
		ON UPDATE CASCADE
		ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE Doctor (
	DoctorID int NOT NULL AUTO_INCREMENT,
	Doc_Fname varchar(255) NOT NULL,
	Doc_Lname varchar(255) NOT NULL,
	Doc_Specialization varchar(255) NOT NULL,
	PRIMARY KEY (DoctorID)
) ENGINE = InnoDB;

CREATE TABLE Hospital (
	HospitalID int NOT NULL AUTO_INCREMENT,
	Hos_Name varchar(255) NOT NULL,
    Branch varchar(255) NULL,
	Address varchar(255) NOT NULL,
	City varchar(255) NOT NULL,
	State char(2) NOT NULL,
    ZipCode int(5) NOT NULL,
	PRIMARY KEY (HospitalID)
) ENGINE = InnoDB;

CREATE TABLE Clinic (
	ClinicID int NOT NULL AUTO_INCREMENT,
	Clinic_Specialization varchar(255) NOT NULL,
	HospitalID int NOT NULL,
    Clinic_Phone char(12) NULL,
	PRIMARY KEY (ClinicID),
	FOREIGN KEY (HospitalID)
		REFERENCES Hospital(HospitalID)
		ON UPDATE CASCADE
		ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE Appointment (
	AppointmentID int NOT NULL AUTO_INCREMENT,
	PatientID int NOT NULL,
	Username varchar(255) NOT NULL,
	DoctorID int NOT NULL,
	ClinicID int NOT NULL,
	Date_of_appt datetime NOT NULL,
    Confirmed bit(1) NOT NULL,
    Checked_In bit(1) NOT NULL,
    Cancelled bit(1) NOT NULL,
	Notes text NULL,
  Date_of_last_reminder date,
	PRIMARY KEY (AppointmentID),
	FOREIGN KEY (PatientID)
		REFERENCES Patient(PatientID)
		ON UPDATE CASCADE
		ON DELETE CASCADE,
	FOREIGN KEY (Username)
		REFERENCES Parent(Username)
		ON UPDATE CASCADE
		ON DELETE CASCADE,
	FOREIGN KEY (DoctorID)
		REFERENCES Doctor(DoctorID)
		ON UPDATE CASCADE
		ON DELETE CASCADE,
	FOREIGN KEY (ClinicID)
		REFERENCES Clinic(ClinicID)
		ON UPDATE CASCADE
		ON DELETE CASCADE
) ENGINE = InnoDB;
