INSERT INTO OfficeVisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (355,'2009-9-17',9000000000,'Test office visit','1',1);

DELETE FROM OVMedication WHERE VisitID = 333;
INSERT INTO OVMedication(NDCode, VisitID, StartDate,EndDate,Dosage,Instructions)
	VALUES ('081096', 355, '2009-9-18', '2009-10-18', 10, 'Take three times daily with water');
	
