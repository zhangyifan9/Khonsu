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
(1,
'Person', 
'Random', 
'nobody@gmail.com', 
'1247 Noname Dr', 
'Suite 106', 
'Raleigh', 
'NC', 
'27606',
'1234', 
'919',
'971',
'0000', 
'Mommy Person', 
'704',
'532',
'2117', 
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
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO Users(MID, password, role, sQuestion, sAnswer) 
			VALUES (1, 'pw', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;

DELETE FROM PersonalHealthInformation WHERE PatientID = 1;
INSERT INTO PersonalHealthInformation
(PatientID,Height,Weight,Smoker,SmokingStatus,BloodPressureN,BloodPressureD,CholesterolHDL,CholesterolLDL,CholesterolTri,HCPID, AsOfDate)
VALUES ( 1,  72,   180,   0, 9,      100,          100,           40,             100,         100,          9000000000, '2007-06-07 20:33:58');

INSERT INTO OfficeVisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (11,'2005-10-10',9000000000,'Yet another office visit.','',1)
 ON DUPLICATE KEY UPDATE id = id;

INSERT INTO OVDiagnosis(ICDCode, VisitID) VALUES 
(350.0, 11),
(250.0, 11)
 ON DUPLICATE KEY UPDATE ICDCode = VALUES(ICDCode), VisitID = VALUES(VisitID);

INSERT INTO DeclaredHCP(PatientID,HCPID) VALUE(1, 9000000003)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;
