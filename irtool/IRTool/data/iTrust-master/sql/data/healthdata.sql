DELETE FROM PersonalHealthInformation WHERE PatientID = 2;
INSERT INTO PersonalHealthInformation 
	   (PatientID,Height,Weight,Smoker,BloodPressureN,BloodPressureD,CholesterolHDL,CholesterolLDL,CholesterolTri,HCPID, AsOfDate)
VALUES ( 2,  62.2,   205.5,   1,      165,          220,           40,             255,         230,          9000000000, CONCAT(YEAR(NOW())-2, '-08-12 08:34:58')),
	   ( 2,  62.0,   209.1,   1,      170,          200,           70,             200,         290,          9000000000, CONCAT(YEAR(NOW())-2, '-10-30 10:54:22')),
	   ( 2,  62.0,   220.5,   1,      190,          300,           35,             219,         160,          9000000000, CONCAT(YEAR(NOW())-2, '-11-30 14:14:22')),
	   ( 2,  62.0,   218.5,   0,      165,          220,           30,             255,         220,          9000000000, CONCAT(YEAR(NOW())-1, '-01-04 12:54:48')),
	   ( 2,  62.4,    0.0,     0,      165,          220,           30,             255,         220,          9000000000, CONCAT(YEAR(NOW())-1, '-11-19 12:32:48')),
	   ( 2,  62.2,   205.5,   1,      165,          220,           40,             255,         230,          9000000000, '2007-08-12 08:34:58'),
	   ( 2,  62.0,   209.1,   1,      170,          200,           70,             200,         290,          9000000000, '2007-10-30 10:54:22');