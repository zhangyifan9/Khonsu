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
9000000005,
null,
'hcp',
'Soulcrusher',
'Sarah',
'4321 My Road St',
'PO BOX 2',
'Woodbridge',
'NY',
'12345-1234',
'12345',
'1234',
'999-888-7777',
'999',
'888',
'7777',
'surgeon',
'ssoulcrusher@iTrust.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO Users(MID, password, role, sQuestion, sAnswer) VALUES(9000000005, 'pw', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO HCPAssignedHos(HCPID, HosID) VALUES(9000000005,'9191919191'), (9000000005,'8181818181')
ON DUPLICATE KEY UPDATE HCPID = HCPID;
