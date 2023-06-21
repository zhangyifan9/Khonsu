DELETE FROM Representatives where representerMID = '2';
DELETE FROM RemoteMonitoringData where PatientID = '1';
DELETE FROM RemoteMonitoringLists where PatientMID = '1';

INSERT INTO Representatives(representerMID, representeeMID)
					VALUES (2, 1);
					
INSERT INTO RemoteMonitoringLists(PatientMID, HCPMID, GlucoseLevel)
					VALUES (1, 9000000000, 1);