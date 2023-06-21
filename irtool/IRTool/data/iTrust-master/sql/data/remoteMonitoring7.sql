DELETE FROM RemoteMonitoringLists where HCPMID = '9000000000';
DELETE FROM RemoteMonitoringData where PatientID = '1';

INSERT INTO RemoteMonitoringLists(PatientMID, HCPMID, height, weight, pedometerReading)
					VALUES (1, 9000000000, 1, 1, 1);