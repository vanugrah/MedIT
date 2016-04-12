-- Get Hospital / Clinic information for a selected clinic (Replace ClinicID with the one the user chose)
SELECT HospitalID, Hos_Name, Branch, Address, City, State, ZipCode, ClinicID, Clinic_Specialization, Clinic_Phone 
	FROM Clinic NATURAL JOIN Hospital
		WHERE ClinicID = 5;