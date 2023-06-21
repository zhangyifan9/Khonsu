/**
 * 
 */
package edu.ncsu.csc.itrust.exception;

import java.util.List;

/**
 * @author student
 *@author student
 */

public class PrescriptionWarningException extends iTrustException {
	private static final long serialVersionUID = -5892322662538526776L;
	
	private List<String> warnings;
	
	/**
	 * Warning if providing wrong prescription
	 * @param warnings
	 */
	public PrescriptionWarningException(List<String> warnings) {
		super("");
		this.warnings = warnings;
	}
	
	/**
	 * Display warning message
	 * @return r
	 */
	public String getDisplayMessage() {
		StringBuffer buf = new StringBuffer();
		for (String warning : warnings) {
			buf.append(warning + "\n");
		}
		return buf.toString();
	}
}
