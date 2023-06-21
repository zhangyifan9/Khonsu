package edu.ncsu.csc.itrust.enums;


/**
 * All of the possible transaction types, in no particular order, used in producing the operational profile.
 * TransactionCode -- identification code
 * Description -- basic description of the event
 * ActionPhrase -- a patient-centered English sentence describing the action (used in activity feeds)
 * PatientViewable -- boolean for if the action will be displayed in the patient activity feed
 */
public enum TransactionType {
	LOGIN_FAILURE(1, "Failed login", "tried to authenticate unsuccessfully", true),
	HOME_VIEW(10, "User views homepage", "has viewed the iTrust user homepage", false),
	UNCAUGHT_EXCEPTION(20, "Exception occured and was not caught", "caused an unhandled exception", false),
	GLOBAL_PREFERENCES_VIEW(30,"View global preferences","viewed global preferences for iTrust", false),
	GLOBAL_PREFERENCES_EDIT(31,"Edit global preferences", "edited global preferences for iTrust", false),
	USER_PREFERENCES_VIEW(32, "View preferences", "viewed user preferences", false),
	USER_PREFERENCES_EDIT(33, "Edit peferences", "edited user preferences", false),
    PATIENT_CREATE(100, "Create a patient", "created your account", true),
    PATIENT_DISABLE(101, "Disable a patient", "disabled your account", true),
    LHCP_CREATE(200, "Create a LHCP", "created an LHCP", false),
    LHCP_EDIT(201, "Edit LHCP information", "edited LHCP information", false),
    LHCP_DISABLE(202, "Disable a LHCP", "disbaled an LHCP", false),
    ER_CREATE(210, "Create an ER", "created an ER", false),
    ER_EDIT(211, "Edit ER information", "edited ER information", false),
    ER_DISABLE(212, "Disable an ER", "disabled an ER", false),
    PHA_CREATE(220, "Create a PHA", "created a PHA", false),
    PHA_EDIT(221, "Edit PHA information", "edited PHA information", false),
    PHA_DISABLE(222, "Disable a PHA", "disabled a PHA", false),
    LHCP_ASSIGN_HOSPITAL(230, "Assign LHCP to a hospital", "assigned an LHCP to a hospital", false),
    LHCP_REMOVE_HOSPITAL(231, "Unassign LHCP from a hospital", "unassigned an LHCP from a hospital", false),
    LT_ASSIGN_HOSPITAL(232, "Assign LT to a hospital", "assigned an LT to a hospital", false),
    LT_REMOVE_HOSPITAL(233, "Unassign LT from a hospital", "unassigned an LT from a hospital", false),
    UAP_CREATE(240, "Create a UAP", "created a UAP", false),
    UAP_EDIT(241, "Edit a UAP", "edited a UAP", false),
    UAP_DISABLE(242, "Disable a UAP", "disabled a UAP", false),
    PERSONNEL_VIEW(250, "View a personnel's information", "viewed a personnel member's information", false),
    LT_CREATE(260, "Create a LT", "created an LT", false),
    LT_EDIT(261, "Edit LT information", "edited LT information", false),
    LT_DISABLE(262, "Disable a LT", "disabled an LT", false),
    LOGIN_SUCCESS(300, "Login Succeded", "successfully authenticated", true),
    LOGOUT(301, "Logged out", "logged out of iTrust", true),
    LOGOUT_INACTIVE(302, "Logged out from inactivity", "logged out due to inactivity",true),
    PASSWORD_RESET(310, "Password changed", "changed your iTrust password", true),
    DEMOGRAPHICS_VIEW(400, "View patient demographics", "viewed your demographics", true),
    DEMOGRAPHICS_EDIT(410, "Edit patient demographics", "edited your demographics", true),
    PATIENT_PHOTO_UPLOAD(411, "Upload patient photo", "uploaded a photo of you to your record", false),
    PATIENT_PHOTO_REMOVE(412, "Remove patient photo", "removed your photograph from your record", false),
    LHCP_VIEW(600, "View LHCP information", "viewed LHCP information", false),
    LHCP_DECLARE_DLHCP(601, "Declare LHCP as DLHCP", "declared a LHCP", true),
    LHCP_UNDECLARE_DLHCP(602, "Undeclare LHCP as DLHCP", "undeclared an LHCP", true),
    ACCESS_LOG_VIEW(800, "View access log", "viewed your access log", false),
    MEDICAL_RECORD_VIEW(900, "View medical records", "viewed your medical records", true),
    PATIENT_HEALTH_INFORMATION_VIEW(1000, "View personal health information", "viewed your basic health information", true),
    PATIENT_HEALTH_INFORMATION_EDIT(1001, "Enter/edit personal health information", "edited your basic health information", true),
    BASIC_HEALTH_CHARTS_VIEW(1002, "View Basic Health Charts", "viewed you basic health information charts", true),
    OFFICE_VISIT_CREATE(1100, "Create Office Visits", "created an office visit", true),
    OFFICE_VISIT_VIEW(1101, "View Office Visits", "viewed your office visit", true),
    OFFICE_VISIT_EDIT(1102, "Edit Office Visits", "edited your office visit", true),
    
    PRESCRIPTION_ADD(1110, "Add Prescription", "Added a prescription to your office visit", true),
    PRESCRIPTION_EDIT(1111, "Edit Prescription", "Edited a prescription in your office visit", true),
    PRESCRIPTION_REMOVE(1112, "Remove Prescription", "Removed a prescription from your office visit", true),
    LAB_PROCEDURE_ADD(1120, "Add Lab Procedure", "Added a lab procedure to your office visit", true),
    LAB_PROCEDURE_EDIT(1121, "Edit Lab Procedure", "Edited a lab procedure in your office visit", true),
    LAB_PROCEDURE_REMOVE(1122, "Remove Lab Procedure", "Removed a lab procedure from your office visit", true),
    DIAGNOSIS_ADD(1130, "Add Diagnosis", "Added a diagnosis to your office visit", true),
    DIAGNOSIS_REMOVE(1132, "Remove Diagnosis", "Removed a diagnosis from your office visit", true),
    PROCEDURE_ADD(1140, "Add Procedure", "Added a procedure to your office visit", true),
    PROCEDURE_EDIT(1141, "Edit Procedure", "Edited a procedure in your office visit", true),
    PROCEDURE_REMOVE(1142, "Remove Procedure", "Removed a procedure from your office visit", true),
    IMMUNIZATION_ADD(1150, "Add Immunization", "Added an immunization to your office visit", true),
    IMMUNIZATION_REMOVE(1152, "Remove Immunization", "Removed an immunization from your office visit", true),
    
    OPERATIONAL_PROFILE_VIEW(1200, "View Operational Profile", "viewed the operational profile", false),
    HEALTH_REPRESENTATIVE_DECLARE(1300, "Declare personal health representative", "declared a personal health representative", true),
    HEALTH_REPRESENTATIVE_UNDECLARE(1301, "Undeclare personal health representative", "undeclared a personal health representative", true),
    MEDICAL_PROCEDURE_CODE_ADD(1500, "Add Medical procedure code", "added a medical procedure code", false),
    MEDICAL_PROCEDURE_CODE_VIEW(1501, "View Medical procedure code", "viewed a medical procedure code", false),
    MEDICAL_PROCEDURE_CODE_EDIT(1502, "Edit Medical procedure code", "edited a medical procedure code", false),
    IMMUNIZATION_CODE_ADD(1510, "Add Immunization code", "added an immunization code", false),
    IMMUNIZATION_CODE_VIEW(1511, "View Immunization code", "viewed an immunization code", false),
    IMMUNIZATION_CODE_EDIT(1512, "Edit Immunization code", "edited an immunization code", false),
    DIAGNOSIS_CODE_ADD(1520, "Add Diagnosis code", "added a diagnosis code", false),
    DIAGNOSIS_CODE_VIEW(1521, "View Diagnosis code", "viewed a diagnosis code", false),
    DIAGNOSIS_CODE_EDIT(1522, "Edit Diagnosis code", "edited a diagnosis code", false),
    DRUG_CODE_ADD(1530, "Add Drug code", "added a drug code", false),
    DRUG_CODE_VIEW(1531, "View Drug code", "viewed a drug code", false),
    DRUG_CODE_EDIT(1532, "Edit Drug code", "edited a drug code", false),
    DRUG_CODE_REMOVE(1533, "Remove Drug code", "removed a drug code", false),
    LOINC_CODE_ADD(1540, "Add Physical Services code", "added a physical service code", false),
    LOINC_CODE_VIEW(1541, "View Physical Services code", "viewed a physical service code", false),
    LOINC_CODE_EDIT(1542, "Edit Physical Services code", "edited a physical service code", false),
    RISK_FACTOR_VIEW(1600, "View risk factors", "viewed your risk factors", true),
    PATIENT_REMINDERS_VIEW(1700, "Proactively determine necessary patient care", "proactively determined your necessary patient care", true),
    HOSPITAL_LISTING_ADD(1800, "Add a hospital listing", "added a hospital listing", false),
    HOSPITAL_LISTING_VIEW(1801, "View a hospital listing", "viewed a hospital listing", false),
    HOSPITAL_LISTING_EDIT(1802, "Edit a hospital listing", "edited a hospital listing", false),
    PRESCRIPTION_REPORT_VIEW(1900, "View prescription report", "viewed your prescription report", true),
    DEATH_TRENDS_VIEW(2000, "View Cause of Death Trends", "viewed cause of death trends", false),
    EMERGENCY_REPORT_CREATE(2100, "Create emergency report", "created an emergency report for you", true),
    EMERGENCY_REPORT_VIEW(2101, "View emergency report", "viewed your emergency report",true),
    APPOINTMENT_TYPE_ADD(2200, "Add appointment type listing", "added an appointment type", false),
    APPOINTMENT_TYPE_VIEW(2201, "View appointment type listing", "viewed an appointment type", false),
    APPOINTMENT_TYPE_EDIT(2202, "Edit appointment type listing", "edited an appointment type", false),
    APPOINTMENT_ADD(2210, "Schedule Appointments", "scheduled an appointment with you", true),
    APPOINTMENT_VIEW(2211, "View Scheduled Appointment", "viewed your scheduled appointment", true),
    APPOINTMENT_EDIT(2212, "Edit Scheduled Appointment", "edited your scheduled appointment", true),
    APPOINTMENT_REMOVE(2213, "Delete Scheduled Appointment", "canceled your scheduled appointment", true),
    APPOINTMENT_ALL_VIEW(2220, "View All Scheduled Appointments", "viewed all scheduled appointments", true),
    COMPREHENSIVE_REPORT_VIEW(2300, "Comprehensive Report", "viewed your comprehensive report", true),
    COMPREHENSIVE_REPORT_ADD(2301, "Request Comprehensive Report", "requested a comprehensive report for you", true),
    SATISFACTION_SURVEY_TAKE(2400, "Take Satisfaction Survey", "completed a satisfaction survey", true),
    SATISFACTION_SURVEY_VIEW(2500, "View physician satisfaction results", "viewed physician satisfaction survey results", false),

    LAB_RESULTS_UNASSIGNED(2600, "Unassigned lab results log value", "", false),
    LAB_RESULTS_CREATE(2601, "Create laboratory procedure", "created your lab procedure", true),
    LAB_RESULTS_VIEW(2602, "View laboratory procedure results", "viewed your lab procedure results", true),
    LAB_RESULTS_REASSIGN(2603, "Reassign laboratory test to a new lab technician", "assigned your laboratory test to a new lab tech", false),
    LAB_RESULTS_REMOVE(2604, "Remove a laboratory procedure", "removed your lab procedure", true),
    LAB_RESULTS_ADD_COMMENTARY(2605, "Add commentary to a laboratory procedure", "marked a lab procedure as completed", true),
    LAB_RESULTS_VIEW_QUEUE(2606, "View laboratory procedure queue", "viewed your lab procedure queue", false),
    LAB_RESULTS_RECORD(2607, "Record laboratory test results", "recorded your lab procedure results", false),
    LAB_RESULTS_RECEIVED(2608, "Laboratory procedure received by lab technician", "the lab tech received your lab procedure", false),
    
    EMAIL_SEND(2700, "Send an email", "sent an email", false),
    EMAIL_HISTORY_VIEW(2710, "View email history", "viewed email history", false),
    PATIENT_LIST_VIEW(2800, "View patient list", "viewed his/her patient list", false),
    EXPERIENCED_LHCP_FIND(2900, "Find LHCPs with experience with a diagnosis", "found LHCPs with experience with a diagnosis", false),
    MESSAGE_SEND(3000, "Send messages", "sent a message", false),
    MESSAGE_VIEW(3001, "View messages", "viewed a message", false),
    INBOX_VIEW(3010, "View inbox", "viewed message inbox", false),
    OUTBOX_VIEW(3011, "View outbox", "viewed message outbox", false),
    PATIENT_FIND_LHCP_FOR_RENEWAL(3100, "Find LHCPs for prescription renewal", "found LHCPs for prescription renewal", true),
    EXPIRED_PRESCRIPTION_VIEW(3110, "View expired prescriptions", "viewed your expired prescriptions", true),
    PRECONFIRM_PRESCRIPTION_RENEWAL(3200, "Proactively confirm prescription-renewal needs", "proactively confirmed your prescription renewal needs", true),
    DIAGNOSES_LIST_VIEW(3210, "View list of diagnoses", "viewed a list of diagnosis", false),
    CONSULTATION_REFERRAL_CREATE(3300, "Refer a patient for consultations", "referred you to another HCP", true),
    CONSULTATION_REFERRAL_VIEW(3310, "View referral for consultation", "viewed your consultation referral", true),
    CONSULTATION_REFERRAL_EDIT(3311, "Modify referral for consultation", "modified your consultation referral", true),
    CONSULTATION_REFERRAL_CANCEL(3312, "Cancel referral for consultation", "canceled your consultation referral", true),
    PATIENT_LIST_ADD(3400, "Patient added to monitoring list", "added you to the telemedicine monitoring list", true),
    PATIENT_LIST_EDIT(3401, "Patient telemedicine permissions changed", "changed the data you report for telemedicine monitoring", true),
    PATIENT_LIST_REMOVE(3402, "Patient removed from monitoring list", "removed you from the telemedicine monitoring list", true),
    TELEMEDICINE_DATA_REPORT(3410, "Remote monitoring levels are reported", "reported your telemedicine data", true),
    TELEMEDICINE_DATA_VIEW(3420, "Remote monitoring levels are viewed", "viewed your telemedicine data reports", true),
    ADVERSE_EVENT_REPORT(3500, "Adverse event reporting", "reported an adverse event", true),
    ADVERSE_EVENT_VIEW(3600, "Adverse event monitoring", "reviewed an adverse event report", false),
    ADVERSE_EVENT_REMOVE(3601, "Remove adverse event", "removed an adverse event report", false),
    ADVERSE_EVENT_REQUEST_MORE(3602, "Request More Adverse Event Details", "requested more adverse event details", false),
    ADVERSE_EVENT_CHART_VIEW(3603, "View Adverse Event Chart", "viewed adverse event chart", false),
    OVERRIDE_INTERACTION_WARNING(3700, "Override interaction warning", "Overrode an interaction warning", true),
    OVERRIDE_CODE_ADD(3710, "Add overriding reason listing", "added a medication override reason",false),
    OVERRIDE_CODE_EDIT(3711, "Edit overriding reason listing", "edited a medication override reason", false),
    DRUG_INTERACTION_ADD(3800, "Add Drug Interactions Code", "added a drug interaction code", false),
    DRUG_INTERACTION_EDIT(3801, "Edit Drug Interactions Code", "edited a drug interaction code", false),
    DRUG_INTERACTION_DELETE(3802, "Delete Drug Interactions Code", "deleted a drug interaction code", false),
    CALENDAR_VIEW(4000, "View calendar", "viewed your calendar", false),
    UPCOMING_APPOINTMENTS_VIEW(4010, "View Upcoming Appointments", "viewed upcoming appointments", false),
    NOTIFICATIONS_VIEW(4200, "View Notifications", "viewed your notification center", false),
    ACTIVITY_FEED_VIEW(4300, "View activity feed", "viewed your activity feed", false),
	PATIENT_INSTRUCTIONS_ADD(4400, "Add Patient Specific Instructions for Office Visit", "added a patient-specific instruction", true),
	PATIENT_INSTRUCTIONS_EDIT(4401, "Edit Patient Specific Instructions for Office Visit", "edited a patient-specific instruction", true),
	PATIENT_INSTRUCTIONS_DELETE(4402, "	Delete Patient Specific Instructions for Office Visit", "deleted a patient-specific instruction", true),
	PATIENT_INSTRUCTIONS_VIEW(4403, "	View Patient Specific Instructions", "viewed your patient-specific instructions", true),
	
	DIAGNOSIS_TRENDS_VIEW(4500, "View diagnosis statistics", "viewed your diagnosis count", false),
	DIAGNOSIS_EPIDEMICS_VIEW(4600, "View Epidemic Detection", "viewed epidemic detection", false);
	
	/**
	 * This string is used in the SQL statement associated with pulling events for
	 * display in a patient's Access Log
	 */
	public static String patientViewableStr;
	
	static {
		for(TransactionType t : TransactionType.values())
		{
			if(t.isPatientViewable())
			{
				patientViewableStr += "," + t.code;
			}
		}
	}
	/**
	 * This string is used in the SQL statement associated with excluding events as specified in UC43
	 */
	public static String dlhcpHiddenStr;
	
	static {
		dlhcpHiddenStr = "400, 900, 1000, 1001, 1100, 1101, 1102, 1110, 1111, 1112, 1120, 1121, 1122, 1130, 1131, 1132, 1140, 1141, 1142, 1150, 1152, 1600, 1700, 1900, 2101, 2300, 2601, 2602, 3200";
	}
	
	private TransactionType(int code, String description, String actionPhrase, boolean patientViewable) {
		this.code = code;
		this.description = description;
		this.actionPhrase = actionPhrase;
		this.patientView = patientViewable;
	}

	private int code;
	private String description;
	private String actionPhrase;
	private boolean patientView;

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
	
	public String getActionPhrase() {
		return actionPhrase;
	}
	
	public boolean isPatientViewable(){
		return patientView;
	}
	
	public static TransactionType parse(int code) {
		for (TransactionType type : TransactionType.values()) {
			if (type.code == code)
				return type;
		}
		throw new IllegalArgumentException("No transaction type exists for code " + code);
	}
	
}
