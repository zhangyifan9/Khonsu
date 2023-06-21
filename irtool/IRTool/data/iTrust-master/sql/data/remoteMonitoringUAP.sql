DELETE FROM RemoteMonitoringLists where HCPMID = '8000000009';
DELETE FROM RemoteMonitoringData where PatientID = '2';

INSERT INTO RemoteMonitoringLists(PatientMID, HCPMID)
					VALUES (2, 8000000009);



