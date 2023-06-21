INSERT INTO Personnel(
MID,
AMID,
role,
lastName, 
firstName, 
address1,
address2,
city,
state,
zip,
zip1,
zip2,
phone,
phone1,
phone2,
phone3,
specialty,
email)
VALUES (
9000000008,
null,
'hcp',
'George',
'Curious',
'Monkey Street',
'',
'Monkey Town',
'CA',
'12345-1234',
'12345',
'1234',
'999-888-7777',
'999',
'888',
'7777',
'Pediatrician',
'meepmeep@meep.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO Users(MID, password, role, sQuestion, sAnswer) VALUES(9000000008, 'pw', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO HCPAssignedHos(HCPID, HosID) VALUES(9000000008,'4'), (9000000008,'4')
ON DUPLICATE KEY UPDATE HCPID = HCPID;
