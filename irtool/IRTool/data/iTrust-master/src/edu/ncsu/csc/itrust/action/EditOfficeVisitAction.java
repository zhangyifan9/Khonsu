package edu.ncsu.csc.itrust.action;

import java.util.Arrays;
import java.util.List;
import edu.ncsu.csc.itrust.action.base.EditOfficeVisitBaseAction;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.beans.HospitalBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.beans.forms.EditOfficeVisitForm;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.HospitalsDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.EditOfficeVisitValidator;

/**
 * Edits the office visits of a patient Used by editOfficeVisit.jsp.  This 
 * exists in two states: saved and unsaved.  If unsaved, data cannot be saved 
 * to sub actions (if this is attempted, exceptions will be raised).  Once it 
 * is saved, however, the sub actions can be saved.  
 * 
 * @author laurenhayward
 * @author student
 * 
 */
public class EditOfficeVisitAction extends EditOfficeVisitBaseAction {
	private EditOfficeVisitValidator validator = new EditOfficeVisitValidator();
	private PersonnelDAO personnelDAO;
	private HospitalsDAO hospitalDAO;
	private OfficeVisitDAO ovDAO;
	private PatientDAO patDAO;
	
	private EditPrescriptionsAction prescriptionsAction;
	private EditProceduresAction proceduresAction;
	private EditImmunizationsAction immunizationsAction;
	private EditDiagnosesAction diagnosesAction;
	private EditLabProceduresAction labProceduresAction;
	private EditPatientInstructionsAction patientInstructionsAction;
	private EditReferralsAction referralsAction;
	
	private EventLoggingAction loggingAction;
	
	private long loggedInMID;


	/**
	 * Patient id and office visit id validated by super class
	 * 
	 * @param factory The DAOFactory to be used in creating the DAOs for this action.
	 * @param loggedInMID The MID of the user who is authorizing this action.
	 * @param pidString The patient who this action is performed on.
	 * @param ovIDString The ID of the office visit in play.
	 * @throws iTrustException
	 */
	public EditOfficeVisitAction(DAOFactory factory, long loggedInMID, String pidString, String ovIDString)
			throws iTrustException {
		super(factory, loggedInMID, pidString, ovIDString);
		pid = Long.parseLong(pidString);
		ovDAO = factory.getOfficeVisitDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.hospitalDAO = factory.getHospitalsDAO();
		this.patDAO = factory.getPatientDAO();

		this.prescriptionsAction = new EditPrescriptionsAction(factory, loggedInMID, pidString, ovIDString);
		this.proceduresAction = new EditProceduresAction(factory, loggedInMID, pidString, ovIDString);
		this.immunizationsAction = new EditImmunizationsAction(factory, loggedInMID, pidString, ovIDString);
		this.diagnosesAction = new EditDiagnosesAction(factory, loggedInMID, pidString, ovIDString);
		this.labProceduresAction = new EditLabProceduresAction(factory, loggedInMID, pidString, ovIDString);
		this.patientInstructionsAction = new EditPatientInstructionsAction(factory, loggedInMID, pidString, ovIDString);
		this.referralsAction = new EditReferralsAction(factory, loggedInMID, pidString, ovIDString);
		
		this.loggingAction = new EventLoggingAction(factory);
		
		this.loggedInMID = loggedInMID;
	}
	
	/**
	 * Create an OfficeVisitAction that is not yet associated with an actual 
	 * office visit.  When update() is called, it will be saved.  Until then, 
	 * any attempt to save prescriptions, procedures, lab procedures, 
	 * immunizations, or diagnoses will raise an exception.
	 * @param factory
	 * @param loggedInMID
	 * @param pidString
	 * @throws iTrustException
	 */
	public EditOfficeVisitAction(DAOFactory factory, long loggedInMID, String pidString)
			throws iTrustException {
		super(factory, loggedInMID, pidString);
		pid = Long.parseLong(pidString);
		ovDAO = factory.getOfficeVisitDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.hospitalDAO = factory.getHospitalsDAO();
		this.patDAO = factory.getPatientDAO();
		
		this.prescriptionsAction = new EditPrescriptionsAction(factory, loggedInMID, pidString); 
		this.proceduresAction = new EditProceduresAction(factory, loggedInMID, pidString);
		this.immunizationsAction = new EditImmunizationsAction(factory, loggedInMID, pidString);
		this.diagnosesAction = new EditDiagnosesAction(factory, loggedInMID, pidString);
		this.labProceduresAction = new EditLabProceduresAction(factory, loggedInMID, pidString);
		this.patientInstructionsAction = new EditPatientInstructionsAction(factory, loggedInMID, pidString);
		this.referralsAction = new EditReferralsAction(factory, loggedInMID, pidString);

		this.loggingAction = new EventLoggingAction(factory);
		
		this.loggedInMID = loggedInMID;
	}
	
	/**
	 * Used to update the sub actions once a office visit is saved.  Until this 
	 * is called, attempting to save sub actions will raise an exception.
	 * @throws iTrustException
	 */
	private void reinitializeSubActions() throws iTrustException {
		if (isUnsaved()) {
			throw new iTrustException("Cannot initalize EditOfficeVisit sub actions.  No ovID is present.");
		}
		DAOFactory factory = getFactory();
		String pidString = ""+getPid();
		String ovIDString = ""+getOvID();
		
		prescriptionsAction = new EditPrescriptionsAction(factory, loggedInMID, pidString, ovIDString);
		proceduresAction = new EditProceduresAction(factory, loggedInMID, pidString, ovIDString);
		immunizationsAction = new EditImmunizationsAction(factory, loggedInMID, pidString, ovIDString);
		diagnosesAction = new EditDiagnosesAction(factory, loggedInMID, pidString, ovIDString);
		labProceduresAction = new EditLabProceduresAction(factory, loggedInMID, pidString, ovIDString);
		patientInstructionsAction = new EditPatientInstructionsAction(factory, loggedInMID, pidString, ovIDString);
		referralsAction = new EditReferralsAction(factory, loggedInMID, pidString, ovIDString);
	}

	/**
	 * Returns the office visit bean for the office visit
	 * 
	 * @return the OfficeVisitBean of the office visit
	 * @throws iTrustException
	 */
	public OfficeVisitBean getOfficeVisit() throws iTrustException {
		return getBean();
	}
	
	
	/**
	 * @return The EditPrescriptionsAction sub action associated with this office visit.
	 * @throws iTrustException
	 */
	public EditPrescriptionsAction prescriptions() throws iTrustException {
		return prescriptionsAction;
	}
	/**
	 * @return The EditProceduresAction sub action associated with this office visit.
	 * @throws iTrustException
	 */
	public EditProceduresAction procedures() throws iTrustException {
		return proceduresAction;
	}
	/**
	 * @return The EditImmunizationsAction sub action associated with this office visit.
	 * @throws iTrustException
	 */
	public EditImmunizationsAction immunizations() throws iTrustException {
		return immunizationsAction;
	}
	/**
	 * @return The EditDiagnosesAction sub action associated with this office visit.
	 * @throws iTrustException
	 */
	public EditDiagnosesAction diagnoses() throws iTrustException {
		return diagnosesAction;
	}
	/**
	 * @return The EditLabProceduresAction sub action associated with this office visit.
	 * @throws iTrustException
	 */
	public EditLabProceduresAction labProcedures() throws iTrustException {
		return labProceduresAction;
	}
	
	public EditPatientInstructionsAction patientInstructions() throws iTrustException {
		return patientInstructionsAction;
	}
	
	public EditReferralsAction referrals() throws iTrustException {
		return referralsAction;
	}
	

	/**
	 * This is a list of all hospitals, ordered by the office visit's hcp FIRST
	 * 
	 * @param hcpID
	 * @return
	 * @throws iTrustException
	 */
	public List<HospitalBean> getHospitals() throws iTrustException {
		List<HospitalBean> hcpsHospitals = personnelDAO.getHospitals(getHcpid());
		List<HospitalBean> allHospitals = hospitalDAO.getAllHospitals();
		return combineLists(hcpsHospitals, allHospitals);
	}

	
	/**
	 * Combines two lists of hospitals
	 * 
	 * @param hcpsHospitals hospitals the HCP is assigned to
	 * @param allHospitals all hospitals
	 * @return the combined list
	 */
	private List<HospitalBean> combineLists(List<HospitalBean> hcpsHospitals, List<HospitalBean> allHospitals) {
		for (HospitalBean hos : allHospitals) {
			if (!hcpsHospitals.contains(hos))
				hcpsHospitals.add(hos);
		}
		return hcpsHospitals;
	}

	/**
	 * Updates the office visit with information from the form passed in.  If 
	 * the office visit has not yet been saved, calling this method will save 
	 * it as well as make the sub actions able to be saved.
	 * 
	 * @param form
	 *            information to update
	 * @return "success" or exception's message
	 * @throws FormValidationException
	 */
	public String updateInformation(EditOfficeVisitForm form) throws FormValidationException {
		String confirm = "";
		try {
			updateOv(form);
			confirm = "success";
			return confirm;
		} catch (iTrustException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	/**
	 * Helper that logs an office visit event.  The associated patient id, HCP 
	 * id, and office visit id are automatically included.
	 * @param trans Transaction type for the log.
	 * @throws DBException
	 */
	public void logOfficeVisitEvent(TransactionType trans) throws DBException {
		loggingAction.logEvent(trans, loggedInMID, getPid(), "Office visit ID: " + getOvID());
	}
	
	/**
	 * 
	 * Sends e-mail regarding the prescribed dangerous drug.
	 * 
	 * @param hcpID HCP the prescription is made by
	 * @param patID ID of the patient prescription is for
	 * @param problem The allergy and/or interaction that is the problem
	 * @return the sent e-mail
	 * @throws DBException
	 */	
	public Email makeEmailApp(long hcpID, String patID, String problem) throws DBException, iTrustException {
		PatientBean p = patDAO.getPatient(Long.parseLong(patID));
		String hcpName = personnelDAO.getName(hcpID);
		Email email = new Email();
		email.setFrom("no-reply@itrust.com");
		email.setToList(Arrays.asList(p.getEmail()));
		email.setSubject("HCP has prescribed you a potentially dangerous medication");
		email.setBody(String
				.format(
					"%s has prescribed a medication that you are allergic to or that has a known interaction with a drug you are currently taking. %s",
					hcpName, problem));
		return email;
	}

	/**
	 * Updates the office visit.
	 * 
	 * @param form form with all the information
	 * @throws DBException
	 * @throws FormValidationException
	 */
	private void updateOv(EditOfficeVisitForm form) throws DBException, FormValidationException, iTrustException {
		validator.validate(form);
		OfficeVisitBean ov = getBean();
		ov.setNotes(form.getNotes());
		ov.setVisitDateStr(form.getVisitDate());
		ov.setHcpID(Long.valueOf(form.getHcpID()));
		ov.setPatientID(Long.valueOf(form.getPatientID()));
		ov.setHospitalID(form.getHospitalID());
		updateBean(ov);
	}
	
	
	/**
	 * @return The OfficeVisitBean associated with this office visit, or if it 
	 * has not been saved, a default OfficeVisitBean with the HCP id and 
	 * patient id filled in.
	 * @throws DBException
	 */
	private OfficeVisitBean getBean() throws DBException {
		if (isUnsaved()) {
			OfficeVisitBean b = new OfficeVisitBean();
			b.setHcpID(getHcpid());
			b.setPatientID(getPid());
			return b;
		} else {
			return ovDAO.getOfficeVisit(ovID);
		}
	}
	
	/**
	 * Update the office visit with the given data.  If the office visit has 
	 * not yet been saved, this will save it and reinitialize the sub actions.
	 * @param bean The data with which to update the office visit.
	 * @throws DBException
	 * @throws iTrustException
	 */
	private void updateBean(OfficeVisitBean bean) throws DBException, iTrustException {
		if (isUnsaved()) {
			// bean.getID() == -1
			ovID = ovDAO.add(bean);
			reinitializeSubActions();
		} else {
			ovDAO.update(bean);
		}
	}

}
