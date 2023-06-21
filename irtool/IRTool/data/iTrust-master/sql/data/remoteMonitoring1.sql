DELETE FROM RemoteMonitoringLists where HCPMID = '9000000000';
DELETE FROM RemoteMonitoringData where PatientID = '2';

INSERT INTO RemoteMonitoringLists(PatientMID, HCPMID)
					VALUES (2, 9000000000);


