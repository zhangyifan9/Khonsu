package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.sql.Timestamp;
import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.ApptBeanValidator;

public class AddApptAction {
	private ApptDAO apptDAO;
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;
	private ApptBeanValidator validator = new ApptBeanValidator();
	
	public AddApptAction(DAOFactory factory, long loggedInMID) {
		this.apptDAO = factory.getApptDAO();
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
	}
	
	public String addAppt(ApptBean appt) throws FormValidationException, SQLException {
		validator.validate(appt);
		if(appt.getDate().before(new Timestamp(System.currentTimeMillis()))) {
			return "The scheduled date of this Appointment ("+appt.getDate()+") has already passed.";
		}
		
		try {
			apptDAO.scheduleAppt(appt);
			return "Success: " + appt.getApptType() + " for " + appt.getDate() + " added";
		}
		catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		} 
	}
	
	/**
	 * Gets a users's name from their MID
	 * 
	 * @param mid the MID of the user
	 * @return the user's name
	 * @throws iTrustException
	 */
	public String getName(long mid) throws iTrustException {
		if(mid < 7000000000L)
			return patientDAO.getName(mid);
		else
			return personnelDAO.getName(mid);
	}
}
