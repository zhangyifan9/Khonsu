CREATE TABLE Users(
	MID                 BIGINT unsigned,
	Password            VARCHAR(20),
	Role                enum('patient','admin','hcp','uap','er','tester','pha', 'lt') NOT NULL DEFAULT 'admin',
	sQuestion           VARCHAR(100) DEFAULT '', 
	sAnswer             VARCHAR(30) DEFAULT '',

	PRIMARY KEY (MID)
	/* Please use the MyISAM backend with no foreign keys.*/
) ENGINE=MyISAM; 

CREATE TABLE Hospitals(
	HospitalID   varchar(10),
	HospitalName varchar(30) NOT NULL, 
	
	PRIMARY KEY (hospitalID)
) ENGINE=MyISAM;

CREATE TABLE Personnel(
	MID BIGINT unsigned default NULL,
	AMID BIGINT unsigned default NULL,
	role enum('admin','hcp','uap','er','tester','pha', 'lt') NOT NULL default 'admin',
	enabled tinyint(1) unsigned NOT NULL default '0',
	lastName varchar(20) NOT NULL default '',
	firstName varchar(20) NOT NULL default '',
	address1 varchar(20) NOT NULL default '',
	address2 varchar(20) NOT NULL default '',
	city varchar(15) NOT NULL default '',
	state enum('','AK','AL','AR','AZ','CA','CO','CT','DE','DC','FL','GA','HI','IA','ID','IL','IN','KS','KY','LA','MA','MD','ME','MI','MN','MO','MS','MT','NC','ND','NE','NH','NJ','NM','NV','NY','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VA','VT','WA','WI','WV','WY') NOT NULL default '',
	zip varchar(10) NOT NULL default '',
	zip1 varchar(5)  default '',
	zip2 varchar(4)  default '',
	phone varchar(12) NOT NULL default '',
	phone1 varchar(3) default '',
    phone2 varchar(3) default '',
    phone3 varchar(4) default '',
	specialty varchar(40) default NULL,
	email varchar(55)  default '',
	MessageFilter varchar(60) default '',
	PRIMARY KEY  (MID)
) auto_increment=9000000000 ENGINE=MyISAM;

CREATE TABLE Patients(
	MID BIGINT unsigned  auto_increment, 
	lastName varchar(20)  default '', 
	firstName varchar(20)  default '', 
	email varchar(55)  default '', 
	address1 varchar(20)  default '', 
	address2 varchar(20)  default '', 
	city varchar(15)  default '', 
	state enum('AK','AL','AR','AZ','CA','CO','CT','DE','DC','FL','GA','HI','IA','ID','IL','IN','KS','KY','LA','MA','MD','ME','MI','MN','MO','MS','MT','NC','ND','NE','NH','NJ','NM','NV','NY','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VA','VT','WA','WI','WV','WY')  default 'AK', 
	zip1 varchar(5)  default '', 
	zip2 varchar(4)  default '',
	phone1 varchar(3) default '',
    phone2 varchar(3) default '',
    phone3 varchar(4) default '',
	eName varchar(40)  default '', 
	ePhone1 varchar(3)  default '', 
	ePhone2 varchar(3)  default '', 		
	ePhone3 varchar(4)  default '', 	
	iCName varchar(20)  default '', 
	iCAddress1 varchar(20)  default '', 
	iCAddress2 varchar(20)  default '', 
	iCCity varchar(15)  default '', 
	ICState enum('AK','AL','AR','AZ','CA','CO','CT','DE','DC','FL','GA','HI','IA','ID','IL','IN','KS','KY','LA','MA','MD','ME','MI','MN','MO','MS','MT','NC','ND','NE','NH','NJ','NM','NV','NY','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VA','VT','WA','WI','WV','WY')  default 'AK', 
	iCZip1 varchar(5)  default '', 
	iCZip2 varchar(4)  default '',
	iCPhone1 varchar(3)  default '',
	iCPhone2 varchar(3)  default '',
	iCPhone3 varchar(4)  default '',			
	iCID varchar(20)  default '', 
	DateOfBirth DATE,
	DateOfDeath DATE,
	CauseOfDeath VARCHAR(10) default '',
	MotherMID INTEGER(10) default 0,
	FatherMID INTEGER(10) default 0,
	BloodType VARCHAR(3) default '',
	Ethnicity VARCHAR(20) default '',
	Gender VARCHAR(13) default 'Not Specified',
	TopicalNotes VARCHAR(200) default '',
	CreditCardType VARCHAR(20) default '',
	CreditCardNumber VARCHAR(19) default '',
	MessageFilter varchar(60) default '',
	DirectionsToHome varchar(512) default '',
	Religion varchar(64) default '',
	Language varchar(32) default '',
	SpiritualPractices varchar(512) default '',
	AlternateName varchar(32) default '',
	PRIMARY KEY (MID)
) ENGINE=MyISAM;

CREATE TABLE HistoryPatients(
	ID BIGINT unsigned  auto_increment,
	changeDate DATE NOT NULL,
	changeMID BIGINT unsigned NOT NULL,
	MID BIGINT unsigned NOT NULL, 
	lastName varchar(20)  default '', 
	firstName varchar(20)  default '', 
	email varchar(55)  default '', 
	address1 varchar(20)  default '', 
	address2 varchar(20)  default '', 
	city varchar(15)  default '', 
	state enum('AK','AL','AR','AZ','CA','CO','CT','DE','DC','FL','GA','HI','IA','ID','IL','IN','KS','KY','LA','MA','MD','ME','MI','MN','MO','MS','MT','NC','ND','NE','NH','NJ','NM','NV','NY','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VA','VT','WA','WI','WV','WY')  default 'AK', 
	zip1 varchar(5)  default '', 
	zip2 varchar(4)  default '',
	phone1 varchar(3) default '',
    phone2 varchar(3) default '',
    phone3 varchar(4) default '',
	eName varchar(40)  default '', 
	ePhone1 varchar(3)  default '', 
	ePhone2 varchar(3)  default '', 		
	ePhone3 varchar(4)  default '', 	
	iCName varchar(20)  default '', 
	iCAddress1 varchar(20)  default '', 
	iCAddress2 varchar(20)  default '', 
	iCCity varchar(15)  default '', 
	ICState enum('AK','AL','AR','AZ','CA','CO','CT','DE','DC','FL','GA','HI','IA','ID','IL','IN','KS','KY','LA','MA','MD','ME','MI','MN','MO','MS','MT','NC','ND','NE','NH','NJ','NM','NV','NY','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VA','VT','WA','WI','WV','WY')  default 'AK', 
	iCZip1 varchar(5)  default '', 
	iCZip2 varchar(4)  default '',
	iCPhone1 varchar(3)  default '',
	iCPhone2 varchar(3)  default '',
	iCPhone3 varchar(4)  default '',			
	iCID varchar(20)  default '', 
	DateOfBirth DATE,
	DateOfDeath DATE,
	CauseOfDeath VARCHAR(10) default '',
	MotherMID INTEGER(10) default 0,
	FatherMID INTEGER(10) default 0,
	BloodType VARCHAR(3) default '',
	Ethnicity VARCHAR(20) default '',
	Gender VARCHAR(13) default 'Not Specified',
	TopicalNotes VARCHAR(200) default '',
	CreditCardType VARCHAR(20) default '',
	CreditCardNumber VARCHAR(19) default '',
	MessageFilter varchar(60) default '',
	DirectionsToHome varchar(512) default '',
	Religion varchar(64) default '',
	Language varchar(32) default '',
	SpiritualPractices varchar(512) default '',
	AlternateName varchar(32) default '',
	PRIMARY KEY (ID)
) ENGINE=MyISAM;

CREATE TABLE LoginFailures(
	ipaddress varchar(128) NOT NULL, 
	failureCount int NOT NULL default 0, 
	lastFailure TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (ipaddress)
) ENGINE=MyISAM;

CREATE TABLE ResetPasswordFailures(
	ipaddress varchar(128) NOT NULL, 
	failureCount int NOT NULL default 0, 
	lastFailure TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (ipaddress)
) ENGINE=MyISAM;

CREATE TABLE icdcodes (
  Code decimal(5,2) NOT NULL,
  Description TEXT NOT NULL,
  Chronic enum('no','yes') NOT NULL default 'no',
  PRIMARY KEY (Code)
) ENGINE=MyISAM;

CREATE TABLE CPTCodes(
	Code varchar(5) NOT NULL COMMENT 'Actual CPT Code', 
	Description varchar(30) NOT NULL COMMENT 'Description of the CPT Codes', 
	Attribute varchar(30),
	PRIMARY KEY (Code)
) ENGINE=MyISAM;

CREATE TABLE DrugReactionOverrideCodes(
	Code varchar(5) NOT NULL COMMENT 'Identifier for override reason',
	Description varchar(80) NOT NULL COMMENT 'Description of override reason',
	PRIMARY KEY (Code)
) ENGINE=MyISAM;
	
CREATE TABLE NDCodes(
	Code varchar(10) NOT NULL, 
	Description varchar(100) NOT NULL, 
	PRIMARY KEY  (Code)
) ENGINE=MyISAM;

CREATE TABLE DrugInteractions(
	FirstDrug varchar(9) NOT NULL,
	SecondDrug varchar(9) NOT NULL,
	Description varchar(100) NOT NULL,
	PRIMARY KEY  (FirstDrug,SecondDrug)
) ENGINE=MyISAM;

CREATE TABLE TransactionLog(
	transactionID int(10) unsigned NOT NULL auto_increment, 
	loggedInMID BIGINT unsigned NOT NULL DEFAULT '0', 
	secondaryMID BIGINT unsigned NOT NULL DEFAULT '0', 
	transactionCode int(10) UNSIGNED NOT NULL default '0', 
	timeLogged timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP, 
	addedInfo VARCHAR(255) default '',
	PRIMARY KEY (transactionID)
) ENGINE=MyISAM;

CREATE TABLE HCPRelations(
	HCP BIGINT unsigned NOT NULL default '0', 
	UAP BIGINT unsigned NOT NULL default '0',
	PRIMARY KEY (HCP, UAP)
) ENGINE=MyISAM;

CREATE TABLE PersonalRelations(
	PatientID BIGINT unsigned NOT NULL COMMENT 'MID of the patient',
	RelativeID BIGINT unsigned NOT NULL COMMENT 'MID of the Relative',
	RelativeType VARCHAR( 35 ) NOT NULL COMMENT 'Relation Type'
) ENGINE=MyISAM;

CREATE TABLE Representatives(
	representerMID BIGINT unsigned default 0, 
	representeeMID BIGINT unsigned default 0,
	PRIMARY KEY  (representerMID,representeeMID)
) ENGINE=MyISAM;

CREATE TABLE HCPAssignedHos(
	hosID VARCHAR(10) NOT NULL, 
	HCPID BIGINT unsigned NOT NULL, 
	PRIMARY KEY (hosID,HCPID)
) ENGINE=MyISAM;

CREATE TABLE DeclaredHCP(
	PatientID BIGINT unsigned NOT NULL default '0', 
	HCPID BIGINT unsigned NOT NULL default '0', 
	PRIMARY KEY  (PatientID,HCPID)
) ENGINE=MyISAM;

CREATE TABLE OfficeVisits(
	ID int(10) unsigned auto_increment,
	visitDate date default '0000-00-00',  
	HCPID BIGINT unsigned default '0', 
	notes mediumtext, 
	PatientID BIGINT unsigned default '0', 
	HospitalID VARCHAR(10) default '',
	PRIMARY KEY  (ID)
) ENGINE=MyISAM;

CREATE TABLE PersonalHealthInformation (
	PatientID BIGINT unsigned NOT NULL default '0',
	Height float default '0',  
	Weight float default '0',  
	Smoker tinyint(1) NOT NULL default '0' COMMENT 'Is the person a smoker',
	SmokingStatus int(1) NOT NULL default '9' COMMENT 'Smoking Status Code',
	BloodPressureN int(11) default '0',  
	BloodPressureD int(11) default '0',  
	CholesterolHDL int(11) default '0' COMMENT 'HDL Cholesterol',  
	CholesterolLDL int(11) default '0' COMMENT 'LDL Ccholesterol',  
	CholesterolTri int(11) default '0' COMMENT 'Cholesterol Triglyceride',  
	HCPID BIGINT unsigned default NULL,  
	AsOfDate timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
) ENGINE=MyISAM;

CREATE TABLE PersonalAllergies(
	PatientID BIGINT unsigned NOT NULL COMMENT 'MID of the Patient',
	Allergy VARCHAR( 50 ) NOT NULL COMMENT 'Description of the allergy'
) ENGINE=MyISAM;

CREATE TABLE Allergies(
	ID INT(10) unsigned auto_increment primary key,
	PatientID BIGINT unsigned NOT NULL COMMENT 'MID of the Patient',
	Description VARCHAR( 50 ) NOT NULL COMMENT 'Description of the allergy',
	FirstFound TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM;

CREATE TABLE OVProcedure(
	ID INT(10) auto_increment primary key,
	VisitID INT( 10 ) unsigned NOT NULL COMMENT 'ID of the Office Visit',
	CPTCode VARCHAR( 5 ) NOT NULL COMMENT 'CPTCode of the procedure',
	HCPID VARCHAR( 10 ) NOT NULL DEFAULT ''
) ENGINE=MyISAM;

CREATE TABLE OVMedication (
    ID INT(10)  auto_increment primary key,
	VisitID INT( 10 ) unsigned NOT NULL COMMENT 'ID of the Office Visit',
	NDCode VARCHAR( 9 ) NOT NULL COMMENT 'NDCode for the medication',
	StartDate DATE,
	EndDate DATE,
	Dosage INT DEFAULT 0 COMMENT 'Always in mg - this could certainly be changed later',
	Instructions VARCHAR(500) DEFAULT ''
) ENGINE=MyISAM;

CREATE TABLE OVReactionOverride (
	ID INT(10)  auto_increment primary key,
	OVMedicationID INT(10) NOT NULL COMMENT 'Must correspond to an ID in OVMedication table',
	OverrideCode VARCHAR(5) COMMENT 'Code identifier of the override reason',
	OverrideComment VARCHAR(255) DEFAULT '' COMMENT 'Optional reason for override',
	FOREIGN KEY (OVMedicationID) REFERENCES OVMedication (ID)
) ENGINE=MyISAM;

CREATE TABLE OVDiagnosis (
    ID INT(10) auto_increment primary key,
	VisitID INT( 10 ) unsigned NOT NULL COMMENT 'ID of the Office Visit',
	ICDCode DECIMAL( 5, 2 ) NOT NULL COMMENT 'Code for the Diagnosis'
) ENGINE=MyISAM;

CREATE TABLE GlobalVariables (
	Name VARCHAR(20) primary key,
	Value VARCHAR(20)
) ENGINE=MyISAM;

INSERT INTO GlobalVariables(Name,Value) VALUES ('Timeout', '20');

CREATE TABLE FakeEmail(
	ID INT(10) auto_increment primary key,
	ToAddr VARCHAR(100),
	FromAddr VARCHAR(100),
	Subject VARCHAR(500),
	Body VARCHAR(2000),
	AddedDate timestamp NOT NULL default CURRENT_TIMESTAMP
) ENGINE=MyISAM;

CREATE TABLE ReportRequests (
	ID INT(10) auto_increment primary key,
    RequesterMID BIGINT unsigned,
    PatientMID BIGINT unsigned,
    ApproverMID BIGINT unsigned,
    RequestedDate datetime,
    ApprovedDate datetime,
    ViewedDate datetime,
    Status varchar(30),
	Comment TEXT
) ENGINE=MyISAM;

CREATE TABLE OVSurvey (
	VisitID int(10) unsigned primary key COMMENT 'ID of the Office Visit',
	SurveyDate datetime not null COMMENT 'Date the survey was completed',
	WaitingRoomMinutes int(3) COMMENT 'How many minutes did you wait in the waiting room?',
	ExamRoomMinutes int(3) COMMENT 'How many minutes did you wait in the examination room before seeing your physician?',
    VisitSatisfaction int(1) COMMENT 'How satisfied were you with your office visit?',
	TreatmentSatisfaction int(1) COMMENT 'How satisfied were you with the treatment or information you received?'
) ENGINE=MyISAM;

CREATE TABLE LOINC (
	LaboratoryProcedureCode VARCHAR (7), 
	Component VARCHAR(100),
	KindOfProperty VARCHAR(100),
	TimeAspect VARCHAR(100),
	System VARCHAR(100),
	ScaleType VARCHAR(100),
	MethodType VARCHAR(100)
) ENGINE=MyISAM;

CREATE TABLE LabProcedure (
	LaboratoryProcedureID BIGINT(10) auto_increment primary key,
	PatientMID BIGINT unsigned, 
	LaboratoryProcedureCode VARCHAR (7), 
	Rights VARCHAR(10),
	Status VARCHAR(20),
	Commentary MEDIUMTEXT,
	Results MEDIUMTEXT,
	NumericalResults VARCHAR(20),
	NumericalResultsUnit VARCHAR(20),
	UpperBound VARCHAR(20),
	LowerBound VARCHAR(20),	
	OfficeVisitID INT unsigned,
	LabTechID LONG,
	PriorityCode INT unsigned,
	ViewedByPatient BOOLEAN NOT NULL default FALSE,
	UpdatedDate timestamp NOT NULL default CURRENT_TIMESTAMP
) ENGINE=MyISAM;

CREATE TABLE message (
	message_id          INT UNSIGNED AUTO_INCREMENT,
	parent_msg_id       INT UNSIGNED,
	from_id             BIGINT UNSIGNED NOT NULL,
	to_id               BIGINT UNSIGNED NOT NULL,
	sent_date           DATETIME NOT NULL,
	message             TEXT,
	subject				TEXT,
	been_read			INT UNSIGNED,
	PRIMARY KEY (message_id)
) ENGINE=MyISAM;

CREATE TABLE Appointment (
	appt_id				INT UNSIGNED AUTO_INCREMENT primary key,
	doctor_id           BIGINT UNSIGNED NOT NULL,
	patient_id          BIGINT UNSIGNED NOT NULL,
	sched_date          DATETIME NOT NULL,
	appt_type           VARCHAR(30) NOT NULL,
	comment				TEXT
) ENGINE=MyISAM;

CREATE TABLE AppointmentType (
	apptType_id			INT UNSIGNED AUTO_INCREMENT primary key,
	appt_type           VARCHAR(30) NOT NULL,
	duration			INT UNSIGNED NOT NULL
) ENGINE=MyISAM;

CREATE TABLE referrals (
	id          INT UNSIGNED AUTO_INCREMENT,
	PatientID          BIGINT UNSIGNED NOT NULL,
	SenderID               BIGINT UNSIGNED NOT NULL,
	ReceiverID           BIGINT UNSIGNED NOT NULL,
	ReferralDetails             TEXT,
	OVID		BIGINT UNSIGNED NOT NULL,
	viewed_by_patient 	boolean NOT NULL,
	viewed_by_HCP 	boolean NOT NULL,
	TimeStamp DATETIME NOT NULL,
	PriorityCode INT unsigned,
	PRIMARY KEY (id)
) AUTO_INCREMENT=1 ENGINE=MyISAM;

CREATE TABLE RemoteMonitoringData (
	id          INT UNSIGNED AUTO_INCREMENT,
	PatientID          BIGINT UNSIGNED NOT NULL,
	systolicBloodPressure int(10) SIGNED default -1,
	diastolicBloodPressure int(10) SIGNED default -1,
	glucoseLevel int(10) SIGNED default -1,
	height float default -1,
	weight float default -1,
	pedometerReading int(10) SIGNED default -1,
	timeLogged timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
	ReporterRole		TEXT,
	ReporterID          BIGINT UNSIGNED NOT NULL,
	PRIMARY KEY (id)
) AUTO_INCREMENT=1 ENGINE=MyISAM;

CREATE TABLE RemoteMonitoringLists (
	PatientMID BIGINT unsigned default 0, 
	HCPMID BIGINT unsigned default 0,
	SystolicBloodPressure BOOLEAN default true,
	DiastolicBloodPressure BOOLEAN default true,
	GlucoseLevel BOOLEAN default true,
	Height BOOLEAN default true,
	Weight BOOLEAN default true,
	PedometerReading BOOLEAN default true,
	PRIMARY KEY  (PatientMID,HCPMID)
) ENGINE=MyISAM;

CREATE TABLE AdverseEvents (
	id INT UNSIGNED AUTO_INCREMENT primary key,
	Status VARCHAR(10) default "Active",
	PatientMID BIGINT unsigned default 0,
	PresImmu VARCHAR(50),
	Code VARCHAR(20),
	Comment VARCHAR(2000),
	Prescriber BIGINT unsigned default 0,
	TimeLogged timestamp NOT NULL default CURRENT_TIMESTAMP
) ENGINE=MyISAM;

CREATE TABLE ProfilePhotos (
	MID BIGINT (10) primary key,
	Photo LONGBLOB,
	UpdatedDate timestamp NOT NULL default CURRENT_TIMESTAMP
) ENGINE=MyISAM;

CREATE TABLE PatientSpecificInstructions (
    id BIGINT unsigned AUTO_INCREMENT primary key,
    VisitID BIGINT unsigned,
    Modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Name VARCHAR(100),
    URL VARCHAR(250),
    Comment VARCHAR(500)
) ENGINE=MyISAM;

CREATE TABLE ReferralMessage(
	messageID  INT unsigned NOT NULL, 
	referralID INT unsigned NOT NULL, 
	PRIMARY KEY (messageID,referralID)
) ENGINE=MyISAM;



