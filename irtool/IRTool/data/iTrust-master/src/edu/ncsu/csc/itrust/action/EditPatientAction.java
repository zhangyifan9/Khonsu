package edu.ncsu.csc.itrust.action;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.action.base.PatientBaseAction;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.PatientHistoryBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.PatientValidator;
import edu.ncsu.csc.itrust.EmailUtil;


/**
 * Edits a patient Used by editPatient.jsp
 * 
 * @author laurenhayward
 * 
 */
public class EditPatientAction extends PatientBaseAction {
	private PatientValidator validator = new PatientValidator();
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;
	private long loggedInMID;
	private EmailUtil emailutil;

	/**
	 * The super class validates the patient id
	 * 
	 * @param factory The DAOFactory used to create the DAOs for this action.
	 * @param loggedInMID The MID of the user who is authorizing this action.
	 * @param pidString The MID of the patient being edited.
	 * @throws iTrustException
	 */
	public EditPatientAction(DAOFactory factory, long loggedInMID, String pidString) throws iTrustException {
		super(factory, pidString);
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.loggedInMID = loggedInMID;
		emailutil = new EmailUtil(factory);
	}

	/**
	 * Takes the information out of the PatientBean param and updates the patient's information
	 * 
	 * @param p
	 *            the new patient information
	 * @throws iTrustException
	 * @throws FormValidationException
	 */
	public void updateInformation(PatientBean p) throws iTrustException, FormValidationException {
		p.setMID(pid); // for security reasons
		validator.validate(p);
		patientDAO.editPatient(p, loggedInMID);
		emailutil.sendEmail(makeEmail());
	}

	/**
	 * Returns a PatientBean for the patient
	 * 
	 * @return the PatientBean
	 * @throws DBException
	 */
	public PatientBean getPatient() throws DBException {
		return patientDAO.getPatient(this.getPid());
	}

	/**
	 *  Creates and e-mail to inform the patient that their information has been updated.
	 *  
	 * @return the email with the notice
	 * @throws DBException
	 */
	private Email makeEmail() throws DBException{

		Email email = new Email();
		List<PatientBean> reps = patientDAO.getRepresenting(pid);
		PatientBean pb = patientDAO.getPatient(pid);
		
		List<String> toAddrs = new ArrayList<String>();
		toAddrs.add(pb.getEmail());
		for (PatientBean r: reps) {
			toAddrs.add(r.getEmail());
		}
		
		email.setFrom("no-reply@itrust.com");
    	email.setToList(toAddrs); // patient and personal representative
    	email.setSubject(String.format("Patient Information Updated"));
    	email.setBody("Dear " + pb.getFullName() + ",\n\tYour patient record information has been updated. " + 
    			"Please login to iTrust to see who has viewed your records.");
		return email;
	}
	
	public void editMessageFilter(String filter) throws iTrustException, FormValidationException {
		PatientBean b = this.getPatient();
		b.setMessageFilter(filter);
		this.updateInformation(b);
	}
	
	public boolean hasHistory() throws DBException {
		return patientDAO.hasHistory(this.getPid());
	}
	
	public List<PatientHistoryBean> getHistory() throws DBException {
		return patientDAO.getPatientHistory(this.getPid());
	}
	
	public String getEmployeeName(long mid) throws DBException, iTrustException {
		return personnelDAO.getName(mid);
	}
}
