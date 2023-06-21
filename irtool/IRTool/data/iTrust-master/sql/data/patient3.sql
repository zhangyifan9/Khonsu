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
(3,
'Needs',
'Care',
'fake@email.com',
'1247 Noname Dr',
'Suite 106',
'Raleigh', 
'NC',
'27606',
'1234',
'919',
'971',
'0000',
'Mum',
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
1,
0,
'AB+',
'African American',
'Male',
'')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO Users(MID, password, role, sQuestion, sAnswer) 
			VALUES (3, 'pw', 'patient', 'opposite of yin', 'yang')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO PersonalHealthInformation
(PatientID,Height,Weight,Smoker,SmokingStatus,BloodPressureN,BloodPressureD,CholesterolHDL,CholesterolLDL,CholesterolTri,HCPID, AsOfDate)
VALUES ( 3,  72,   180,   0, 9,     100,          100,           40,             100,         100,          9000000000, '2005-06-07 20:33:58')
 ON DUPLICATE KEY UPDATE PatientID = PatientID;

INSERT INTO OfficeVisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (111,'2005-10-10',9000000000,'Old office visit.','',3)
 ON DUPLICATE KEY UPDATE id = id;

INSERT INTO OVDiagnosis(ICDCode, VisitID) VALUES 
(487.00, 111)
 ON DUPLICATE KEY UPDATE id = id;

INSERT INTO DeclaredHCP(PatientID,HCPID) VALUE(3, 9000000003)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;

INSERT INTO Representatives(RepresenterMID, RepresenteeMID) VALUES(2,3)
 ON DUPLICATE KEY UPDATE RepresenterMID = RepresenterMID;
