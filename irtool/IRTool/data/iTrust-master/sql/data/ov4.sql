INSERT INTO OfficeVisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (375,'2009-9-15',9000000000,'Test office visit','1',2);

DELETE FROM Allergies WHERE PatientID = 2;
INSERT INTO Allergies(PatientID,Description, FirstFound) 
	VALUES (2, '081096', '2008-12-10 20:33:58'),	/*aspirin*/
	       (2, '664662530', '2007-06-04 20:33:58');	/*penicillin*/
