-- Settings

-- Toggle Push Notifications
UPDATE Preference SET Gets_Push = Gets_Push ^ 1 WHERE Username = 'atsou3';

-- Toggle SMS
UPDATE Preference SET Gets_SMS = Gets_SMS ^ 1 WHERE Username = 'atsou3';

-- Toggle Email
UPDATE Preference SET Gets_Email = Gets_Email ^ 1 WHERE Username = 'atsou3';
