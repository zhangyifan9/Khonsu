package edu.ncsu.csc.itrust.enums;

/**
 * The iTrust user roles: Patient, ER, HCP, UAP, LT, PHA, Administrator and Tester.
 * Consult the requirements for the contextual meanings of these individual choices.
 */
public enum Role {
	PATIENT("patient", "Patients",0L), 
	ER("er", "Personnel", 9L), 
	HCP("hcp", "Personnel", 9L), 
	UAP("uap", "Personnel", 8L),
	LT("lt", "Personnel", 5L),
	ADMIN("admin", "Personnel", 0L),
	PHA("pha", "Personnel", 7L),
	TESTER("tester", "", 0L);
	
	private String userRolesString;
	private String dbTable;
	private long midFirstDigit;

	Role(String userRolesString, String dbTable, long midFirstDigit) {
		this.userRolesString = userRolesString;
		this.dbTable = dbTable;
		this.midFirstDigit = midFirstDigit;
	}

	public String getDBTable() {
		return dbTable;
	}

	public String getUserRolesString() {
		return userRolesString;
	}

	public long getMidFirstDigit() {
		return midFirstDigit;
	}

	public static Role parse(String str) {
		for (Role role : values()) {
			if (role.userRolesString.toLowerCase().equals(str.toLowerCase()))
				return role;
		}
		throw new IllegalArgumentException("Role " + str + " does not exist");
	}
}
