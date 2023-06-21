

INSERT INTO Personnel(
MID,
AMID,
role,
lastName, 
firstName, 
address1,
address2)
VALUES (
9990000000,
null,
'hcp',
'Incomplete',
'Jimmy',
'567 Nowhere St.',
'PO Box 4')
ON DUPLICATE KEY UPDATE mid = mid;

INSERT INTO Users(MID, password, role, sQuestion, sAnswer) VALUES(9990000000, 'pw', 'hcp', 'second letter?', 'b')
ON DUPLICATE KEY UPDATE mid = mid;

INSERT INTO HCPAssignedHos(HCPID, HosID) VALUES(9990000000,'9191919191'), (9990000000,'8181818181')
ON DUPLICATE KEY UPDATE HCPID = HCPID;
