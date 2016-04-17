-- Settings

-- Toggle Push Notifications
UPDATE Preference SET Gets_Push = Gets_Push ^ 1 WHERE Username = 'atsou3';

-- Toggle SMS
UPDATE Preference SET Gets_SMS = Gets_SMS ^ 1 WHERE Username = 'atsou3';

-- Toggle Email
UPDATE Preference SET Gets_Email = Gets_Email ^ 1 WHERE Username = 'atsou3';

-- Turn all Reminders off
UPDATE Preference 
	SET Gets_Push = 0, Gets_SMS = 0, Gets_Email = 0 
	WHERE Username = 'atsou3';

-- Toggle Weather
UPDATE Preference SET Gets_Weather = Gets_Weatherpreference ^ 1 WHERE Username = 'atsou3';

-- Toggle Traffic
UPDATE Preference SET Gets_Traffic = Gets_Traffic ^ 1 WHERE Username = 'atsou3';
