DELETE FROM Users WHERE MID = 11;
DELETE FROM OfficeVisits WHERE PatientID = 11;
DELETE FROM Patients WHERE MID = 11;
DELETE FROM DeclaredHCP WHERE PatientID = 11;
DELETE FROM OVDiagnosis WHERE VisitID = 1063;
DELETE FROM OVMedication WHERE VisitID = 1063;


INSERT INTO Users(MID, password, role, sQuestion, sAnswer) 
			VALUES (11, 'pw', 'patient', 'how you doin?', 'good');

INSERT INTO Patients (MID, firstName,lastName, email, phone1, phone2, phone3) 
VALUES (11, 'Marie', 'Thompson', 'e@f.com', '919', '555', '9213');


INSERT INTO OfficeVisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (1063,'2007-6-09',9900000000,'Yet another office visit.','1',11);



INSERT INTO OVMedication(NDCode, VisitID, StartDate,EndDate,Dosage,Instructions)
	VALUES ('647641512', 1063, '2006-10-10', DATE_ADD(CURDATE(), INTERVAL 7 DAY), 5, 'Take twice daily');

INSERT INTO ICDCodes (Code, Description) VALUES (493.99, '');

INSERT INTO OVDiagnosis(ICDCode, VisitID) VALUES 
			(493.99, 1063);

INSERT INTO DeclaredHCP(PatientID, HCPID) VALUES (11, 9900000000);