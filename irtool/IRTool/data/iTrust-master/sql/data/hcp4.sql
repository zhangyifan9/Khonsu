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
9000000004,
null,
'hcp',
'Medico',
'Antonio',
'4321 My Road St',
'PO BOX 2',
'Vandelay City',
'NY',
'12345-1234',
'12345',
'1234',
'999-888-7777',
'999',
'888',
'7777',
'surgeon',
'amedico@iTrust.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO Users(MID, password, role, sQuestion, sAnswer) VALUES(9000000004, 'pw', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO HCPAssignedHos(HCPID, HosID) VALUES(9000000004,'9191919191'), (9000000004,'8181818181')
ON DUPLICATE KEY UPDATE HCPID = HCPID;
