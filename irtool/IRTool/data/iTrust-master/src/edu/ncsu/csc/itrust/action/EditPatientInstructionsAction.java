package edu.ncsu.csc.itrust.action;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.action.base.EditOfficeVisitBaseAction;
import edu.ncsu.csc.itrust.beans.PatientInstructionsBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PatientInstructionsDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.PatientInstructionsBeanValidator;

/**
 * Allows an HCP to add/edit/delete patient instructions for an office visit.
 */
public class EditPatientInstructionsAction extends EditOfficeVisitBaseAction {
	
	private PatientInstructionsDAO instructionsDAO; 

	/**
	 * Creates an EditPatientInstructionsAction for an existing office visit.
	 * @param factory The DAO Factory.
	 * @param hcpid The HCP id.
	 * @param pidString The patient id as a string.
	 * @param ovIDString The office visit as a string.
	 * @throws iTrustException
	 */
	public EditPatientInstructionsAction(DAOFactory factory, long hcpid, String pidString, String ovIDString)
			throws iTrustException {
		super(factory, hcpid, pidString, ovIDString);
		instructionsDAO = factory.getPatientInstructionsDAO();
	}

	/**
	 * Creates an EditPatientInstructionsAction for an non-existent office 
	 * visit.  Most methods will throw exceptions if built with this 
	 * constructor.
	 * @param factory  
	 * @param hcpid  The HCP id.
	 * @param pidString The patient ID as a string.
	 * @throws iTrustException
	 */
	public EditPatientInstructionsAction(DAOFactory factory, long hcpid, String pidString)
			throws iTrustException {
		super(factory, hcpid, pidString);
		instructionsDAO = factory.getPatientInstructionsDAO();
	}

	/**
	 * Get the list of patient instructions associated with the office visit.
	 * @return A list of patient instructions.
	 * @throws DBException
	 */
	public List<PatientInstructionsBean> getPatientInstructions() throws DBException {
		if (isUnsaved()) {
			return new ArrayList<PatientInstructionsBean>();
		} else {
			return instructionsDAO.getList(getOvID());
		}
	}
	
	/**
	 * Add patient instructions to the office visit.
	 * @param bean The instructions to add.
	 * @throws iTrustException
	 */
	public void addPatientInstructions(PatientInstructionsBean bean) throws iTrustException {
		verifySaved();
		instructionsDAO.add(bean);
	}
	
	/**
	 * Modify patient instruction in this office visit.
	 * @param bean The instructions to modify
	 * @throws iTrustException
	 */
	public void editPatientInstructions(PatientInstructionsBean bean) throws iTrustException {
		verifySaved();
		instructionsDAO.edit(bean);
	}
	
	/**
	 * Delete patient instructions from this office visit.
	 * @param bean The instructions to delete.
	 * @throws iTrustException
	 */
	public void deletePatientInstructions(PatientInstructionsBean bean) throws iTrustException {
		verifySaved();
		instructionsDAO.remove(bean.getId());
	}
	
	/**
	 * Verify the contents of the given instruction bean.
	 * @param bean The instructions to check.
	 * @throws iTrustException
	 * @throws FormValidationException
	 */
	public void validate(PatientInstructionsBean bean) throws iTrustException, FormValidationException {
		verifySaved();
		PatientInstructionsBeanValidator validator = new PatientInstructionsBeanValidator();
		validator.validate(bean);
	}

}
