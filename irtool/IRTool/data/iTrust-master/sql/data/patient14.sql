DELETE FROM Users WHERE MID = 14;
DELETE FROM OfficeVisits WHERE PatientID = 14;
DELETE FROM Patients WHERE MID = 14;
DELETE FROM DeclaredHCP WHERE PatientID = 14;
DELETE FROM OVDiagnosis WHERE VisitID = 1066;
DELETE FROM OVMedication WHERE VisitID = 1066;


INSERT INTO Users(MID, password, role, sQuestion, sAnswer) 
			VALUES (14, 'pw', 'patient', 'how you doin?', 'good');

INSERT INTO Patients (MID, firstName,lastName, email, phone1, phone2, phone3) 
VALUES (14, 'Zack', 'Arthur', 'k@l.com', '919', '555', '1234');


INSERT INTO OfficeVisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (1066,'2007-6-09',9900000000,'Yet another office visit.','1',14);



INSERT INTO OVMedication(NDCode, VisitID, StartDate,EndDate,Dosage,Instructions)
	VALUES ('647641512', 1066, '2006-10-10', CURDATE(), 5, 'Take twice daily');

INSERT INTO ICDCodes (Code, Description) VALUES (459.99, '');

INSERT INTO OVDiagnosis(ICDCode, VisitID) VALUES 
			(459.99, 1066);

INSERT INTO DeclaredHCP(PatientID, HCPID) VALUES (14, 9900000000);