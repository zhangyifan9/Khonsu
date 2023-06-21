DELETE FROM Users WHERE MID = 10;
DELETE FROM OfficeVisits WHERE PatientID = 10;
DELETE FROM Patients WHERE MID = 10;
DELETE FROM DeclaredHCP WHERE PatientID = 10;
DELETE FROM OVDiagnosis WHERE VisitID = 1062;
DELETE FROM OVMedication WHERE VisitID = 1062;


INSERT INTO Users(MID, password, role, sQuestion, sAnswer) 
			VALUES (10, 'pw', 'patient', 'how you doin?', 'good');

INSERT INTO Patients (MID, firstName,lastName, email, phone1, phone2, phone3) 
VALUES (10, 'Zappic', 'Clith', 'c@d.com', '919', '555', '9213');


INSERT INTO OfficeVisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (1062,'2007-6-09',9900000000,'Yet another office visit.','1',10);



INSERT INTO OVMedication(NDCode, VisitID, StartDate,EndDate,Dosage,Instructions)
	VALUES ('647641512', 1062, '2006-10-10', DATE_ADD(CURDATE(), INTERVAL 10 DAY), 5, 'Take twice daily');


INSERT INTO OVDiagnosis(ICDCode, VisitID) VALUES 
			(493.00, 1062);

INSERT INTO DeclaredHCP(PatientID, HCPID) VALUES (10, 9900000000);