package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.ApptBeanValidator;

public class EditApptAction {
	private ApptDAO apptDAO;
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;
	private ApptBeanValidator validator = new ApptBeanValidator();
	
	public EditApptAction(DAOFactory factory, long loggedInMID) {
		this.apptDAO = factory.getApptDAO();
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
	}
	
	/**
	 * Retrieves an appointment from the database, given its ID.
	 * Returns null if there is no match, or multiple matches.
	 * 
	 * @param apptID
	 * @return ApptBean with matching ID
	 */
	public ApptBean getAppt(int apptID) {
		try {
			List<ApptBean> apptBeans = apptDAO.getAppt(apptID);
			if (apptBeans.size() == 1)
				return apptBeans.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Updates an existing appointment
	 * 
	 * @param appt Appointment Bean containing the updated information
	 * @return Message to be displayed
	 * @throws FormValidationException
	 */
	public String editAppt(ApptBean appt) throws FormValidationException {
		validator.validate(appt);
		if(appt.getDate().before(new Timestamp(System.currentTimeMillis())))
			return "The scheduled date of this appointment ("+appt.getDate()+") has already passed.";
		try {
			apptDAO.editAppt(appt);
			return "Success: Appointment changed";
		} catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		} 
	}
	
	/**
	 * Removes an existing appointment
	 * 
	 * @param appt Appointment Bean containing the ID of the appointment to be removed.
	 * @return Message to be displayed
	 */
	public String removeAppt(ApptBean appt) {
		try {
			apptDAO.removeAppt(appt);
			return "Success: Appointment removed";
		} catch (SQLException e) {
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
