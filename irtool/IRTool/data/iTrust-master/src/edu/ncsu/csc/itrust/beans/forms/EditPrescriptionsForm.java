package edu.ncsu.csc.itrust.beans.forms;

/**
 * A form to contain data coming from editing a prescription for an office visit.
 * 
 * A form is a bean, kinda. You could say that it's a “form” of a bean :) 
 * Think of a form as a real-life administrative form that you would fill out to get 
 * something done, not necessarily making sense by itself.
 */
public class EditPrescriptionsForm {
	private String medID;
	private String startDate;
	private String endDate;
	private String dosage;
	private String instructions;
	private String overrideCode;
	private String overrideComment;
	
	/**
	 * @return the overrideCode
	 */
	public String getOverrideCode() {
		return overrideCode;
	}
	/**
	 * @param overrideCode the overrideCode to set
	 */
	public void setOverrideCode(String overrideCode) {
		this.overrideCode = overrideCode;
	}
	/**
	 * @return the overrideComment
	 */
	public String getOverrideComment() {
		return overrideComment;
	}
	/**
	 * @param overrideComment the overrideComment to set
	 */
	public void setOverrideComment(String overrideComment) {
		this.overrideComment = overrideComment;
	}
	/**
	 * @return the medID
	 */
	public String getMedID() {
		return medID;
	}
	/**
	 * @param medID the medID to set
	 */
	public void setMedID(String medID) {
		this.medID = medID;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the dosage
	 */
	public String getDosage() {
		return dosage;
	}
	/**
	 * @param dosage the dosage to set
	 */
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}
	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	
	
}
