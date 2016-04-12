-- Get logged-in user info
SELECT * FROM Parent WHERE Username = 'atsou3';

-- Get list of children of logged-in user (and their info)
SELECT * FROM Patient WHERE Username = 'atsou3';

-- Get info of a selected child
SELECT * FROM Patient WHERE PatientID = 1;