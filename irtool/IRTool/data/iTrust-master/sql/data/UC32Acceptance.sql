DELETE FROM Users WHERE MID IN (16, 17, 18);
DELETE FROM OfficeVisits WHERE PatientID IN (16, 17, 18);
DELETE FROM Patients WHERE MID IN (16, 17, 18);
DELETE FROM DeclaredHCP WHERE PatientID IN (16, 17, 18);
DELETE FROM OVDiagnosis WHERE VisitID BETWEEN 1068 AND 1070;
DELETE FROM OVMedication WHERE VisitID BETWEEN 1068 AND 1070;



INSERT INTO Patients (MID, firstName,lastName, email, phone1, phone2, phone3) 
VALUES (16, 'Andy', 'Koopa', 'ak@gmail.com', '919', '224', '3343'),
		(17, 'David', 'Prince', 'prince@gmail.com', '919', '212', '3433'),
		(18, 'Mark', 'Jackson', 'mj@gmail.com', '919', '349', '3432');


INSERT INTO OfficeVisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (1068,'2007-6-09',9000000003,'Yet another office visit.','1',16),
		(1069,'2007-6-09',9000000003,'Yet another office visit.','1',17),
		(1070,'2007-6-09',9000000003,'Yet another office visit.','1',18);



INSERT INTO OVMedication(NDCode, VisitID, StartDate,EndDate,Dosage,Instructions)
	VALUES ('647641512', 1068, '2006-10-10', DATE_ADD(CURDATE(), INTERVAL 5 DAY), 5, 'Take twice daily'),
			('647641512', 1069, '2006-10-10', DATE_ADD(CURDATE(), INTERVAL 5 DAY), 5, 'Take twice daily'),
			('647641512', 1070, '2006-10-10', DATE_ADD(CURDATE(), INTERVAL 20 DAY), 5, 'Take twice daily');


INSERT INTO OVDiagnosis(ICDCode, VisitID) VALUES 
			(250.00, 1068),
			(250.00, 1069),
			(250.00, 1070);

INSERT INTO DeclaredHCP(PatientID, HCPID) 
VALUES (16, 9000000003),
		(17, 9000000003),
		(18, 9000000000);
