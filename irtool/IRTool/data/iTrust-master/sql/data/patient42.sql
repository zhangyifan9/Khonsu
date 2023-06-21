INSERT INTO Patients
(MID, 
lastName, 
firstName,
email,
address1,
address2,
city,
state,
zip1,
zip2,
phone1,
phone2,
phone3,
eName,
ePhone1,
ePhone2,
ePhone3,
iCName,
iCAddress1,
iCAddress2,
iCCity, 
ICState,
iCZip1,
iCZip2,
iCPhone1,
iCPhone2,
iCPhone3,
iCID,
dateofbirth,
mothermid,
fathermid,
bloodtype,
ethnicity,
gender, 
topicalnotes)
VALUES
(42,
'Horse', 
'Bad', 
'badHorse@ele.org', 
'1247 Noname Dr', 
'Suite 106', 
'Raleigh', 
'NC', 
'27606',
'1234', 
'919',
'123',
'4567', 
'Mommy Person', 
'704',
'123',
'4567', 
'Aetna', 
'1234 Aetna Blvd', 
'Suite 602', 
'Charlotte',
'NC', 
'28215',
'', 
'704',
'555',
'1234', 
'ChetumNHowe', 
'1950-05-10',
0,
0,
'AB+',
'African American',
'Male',
'Beware his terrible Death Whinny!')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO Users(MID, password, role, sQuestion, sAnswer) 
			VALUES (42, 'pw', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO DeclaredHCP(PatientID,HCPID) VALUE(1, 9000000000)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;
 
INSERT INTO OfficeVisits(ID, visitDate, HCPID, notes, PatientID, HospitalID)
	VALUES (5001, '2010-11-28', 9000000000, 'Voice is a little hoarse.', 42, 1)
	ON DUPLICATE KEY UPDATE ID = ID;
	
DELETE FROM RemoteMonitoringLists where PatientMID = '42';
DELETE FROM RemoteMonitoringData where PatientID = '42';

INSERT INTO RemoteMonitoringLists(PatientMID, HCPMID, systolicBloodPressure, diastolicBloodPressure)
					VALUES (42, 9000000000, 1, 1);

DELETE FROM PersonalHealthInformation WHERE PatientID = 42;
INSERT INTO PersonalHealthInformation(PatientID, Weight, Height, AsOfDate, Smoker, HCPID)
	VALUES	(42, 280, 70, '2010-11-25 05:30:00', 1, 9000000000),
			(42, 282, 70, '2010-8-13 05:30:00', 1, 9000000000),
			(42, 281.5, 70, '2010-5-19 05:30:00', 1, 9000000000),
			(42, 285, 70, '2010-2-22 05:30:00', 1, 9000000000),
			(42, 279.7, 70, '2009-12-29 05:30:00', 1, 9000000000),
			(42, 280, 70, '2009-7-22 05:30:00', 1, 9000000000),
			(42, 280, 70, '2009-5-23 05:30:00', 1, 9000000000),
			(42, 280, 70, '2009-3-15 05:30:00', 1, 9000000000),
			(42, 290.3, 70, '2008-12-19 05:30:00', 1, 9000000000),
			(42, 293.1, 70, '2008-8-18 05:30:00', 1, 9000000000),
			(42, 296, 70, '2008-4-10 05:30:00', 1, 9000000000),
			(42, 294.2, 70, '2008-2-10 05:30:00', 1, 9000000000);
	
INSERT INTO Appointment(doctor_id, patient_id, sched_date, appt_type)
	VALUE	(9000000000, 42, CONCAT(SUBDATE(CURDATE(),1), ' 15:00:00.0'), 'Consultation');
