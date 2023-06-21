
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
9900000000,
null,
'hcp',
'Arehart',
'Tester',
'2110 Thanem Circle',
'Apt. 302',
'Raleigh',
'NC',
'12345-1234',
'12345',
'1234',
'999-888-7777',
'999',
'888',
'7777',
'Neurologist',
'tarehart@iTrust.org'
) ON DUPLICATE KEY UPDATE mid = mid;

INSERT INTO Users(MID, password, role, sQuestion, sAnswer) VALUES(9900000000, 'pw', 'hcp', 'second letter?', 'b')
ON DUPLICATE KEY UPDATE mid = mid;

INSERT INTO HCPAssignedHos(HCPID, HosID) VALUES(9900000000,'9191919191'), (9900000000,'8181818181')
ON DUPLICATE KEY UPDATE HCPID = HCPID;
