DELETE FROM RemoteMonitoringLists where HCPMID = '9000000000';
DELETE FROM RemoteMonitoringLists where PatientMID = '2';
DELETE FROM RemoteMonitoringData where PatientID = '2';

INSERT INTO RemoteMonitoringLists(PatientMID, HCPMID, systolicBloodPressure, diastolicBloodPressure)
					VALUES (2, 9000000000, 1, 1);