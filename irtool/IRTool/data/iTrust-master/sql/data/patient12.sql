DELETE FROM Users WHERE MID = 12;
DELETE FROM OfficeVisits WHERE PatientID = 12;
DELETE FROM Patients WHERE MID = 12;
DELETE FROM DeclaredHCP WHERE PatientID = 12;
DELETE FROM OVDiagnosis WHERE VisitID = 1064;
DELETE FROM OVMedication WHERE VisitID = 1064;


INSERT INTO Users(MID, password, role, sQuestion, sAnswer) 
			VALUES (12, 'pw', 'patient', 'how you doin?', 'good');

INSERT INTO Patients (MID, firstName,lastName, email, phone1, phone2, phone3) 
VALUES (12, 'Volcano', 'Blammo', 'g@h.com', '919', '555', '9213');


INSERT INTO OfficeVisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (1064,'2007-6-09',9900000000,'Yet another office visit.','1',12);



INSERT INTO OVMedication(NDCode, VisitID, StartDate,EndDate,Dosage,Instructions)
	VALUES ('647641512', 1064, '2006-10-10', DATE_ADD(CURDATE(), INTERVAL 8 DAY), 5, 'Take twice daily');


INSERT INTO OVDiagnosis(ICDCode, VisitID) VALUES 
			(493.00, 1064);

INSERT INTO DeclaredHCP(PatientID, HCPID) VALUES (12, 9900000000);