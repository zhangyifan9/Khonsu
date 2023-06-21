DELETE FROM Users WHERE MID = 13;
DELETE FROM OfficeVisits WHERE PatientID = 13;
DELETE FROM Patients WHERE MID = 13;
DELETE FROM DeclaredHCP WHERE PatientID = 13;
DELETE FROM OVDiagnosis WHERE VisitID = 1065;
DELETE FROM OVMedication WHERE VisitID = 1065;


INSERT INTO Users(MID, password, role, sQuestion, sAnswer) 
			VALUES (13, 'pw', 'patient', 'how you doin?', 'good');

INSERT INTO Patients (MID, firstName,lastName, email, phone1, phone2, phone3) 
VALUES (13, 'Blim', 'Cildron', 'i@j.com', '919', '555', '9213');


INSERT INTO OfficeVisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (1065,'2007-6-09',9900000000,'Yet another office visit.','1',13);



INSERT INTO OVMedication(NDCode, VisitID, StartDate,EndDate,Dosage,Instructions)
	VALUES ('647641512', 1065, '2006-10-10', DATE_ADD(CURDATE(), INTERVAL 5 DAY), 5, 'Take twice daily');

INSERT INTO ICDCodes(Code, Description) VALUES (3.00, '');
INSERT INTO OVDiagnosis(ICDCode, VisitID) VALUES 
			(3.00, 1065);

INSERT INTO DeclaredHCP(PatientID, HCPID) VALUES (13, 9900000000);