package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.action.base.OfficeVisitBaseAction;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.beans.ProcedureBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.DiagnosesDAO;
import edu.ncsu.csc.itrust.dao.mysql.LabProcedureDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.dao.mysql.PrescriptionsDAO;
import edu.ncsu.csc.itrust.dao.mysql.ProceduresDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.Messages;

/**
 * Handles viewing the office visits, prescriptions, and HCP name for the given ovID Used by
 * viewOfficeVisit.jsp
 * 
 * @author laurenhayward
 * 
 */
public class ViewOfficeVisitAction extends OfficeVisitBaseAction {
	private OfficeVisitDAO ovDAO;
	private PrescriptionsDAO prescriptionsDAO;
	private ProceduresDAO proceduresDAO;
	private LabProcedureDAO labProceduresDAO;
	private DiagnosesDAO diagnosesDAO;
	
	private PersonnelDAO personnelDAO;
	private PatientDAO patientDAO;

	/**
	 * Super class handles validating the loggedInMid and ovIDString
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person viewing their office visits. 
	 * @param ovIDString
	 *            The unique identifier of the office visit as a String.
	 * @throws iTrustException
	 */
	public ViewOfficeVisitAction(DAOFactory factory, long loggedInMID, String ovIDString)
			throws iTrustException {
		super(factory, String.valueOf(loggedInMID), ovIDString);
		this.personnelDAO = factory.getPersonnelDAO();
		this.ovDAO = factory.getOfficeVisitDAO();

		prescriptionsDAO = factory.getPrescriptionsDAO();
		proceduresDAO = factory.getProceduresDAO();
		labProceduresDAO = factory.getLabProcedureDAO();
		diagnosesDAO = factory.getDiagnosesDAO();
	}

	/**
	 * Super class handles validating the pidString and ovIDString. Usually used for representing a patient.
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param repPIDString The MID of the representative viewing the records.
	 * @param ovIDString
	 *            The unique identifier of the office visit as a String.
	 * @throws iTrustException
	 */
	public ViewOfficeVisitAction(DAOFactory factory, long loggedInMID, String repPIDString, String ovIDString)
			throws iTrustException {
		super(factory, repPIDString, ovIDString);
		this.personnelDAO = factory.getPersonnelDAO();
		this.patientDAO = factory.getPatientDAO();
		this.ovDAO = factory.getOfficeVisitDAO();

		prescriptionsDAO = factory.getPrescriptionsDAO();
		proceduresDAO = factory.getProceduresDAO();
		labProceduresDAO = factory.getLabProcedureDAO();
		diagnosesDAO = factory.getDiagnosesDAO();
		
		checkRepresented(loggedInMID, repPIDString);
	}

	private void checkRepresented(long loggedInMID, String repPIDString) throws iTrustException {
		try {
			long repee = Long.valueOf(repPIDString);
			if (!patientDAO.represents(loggedInMID, repee))
				throw new iTrustException(
						Messages.getString("ViewOfficeVisitAction.0")); //$NON-NLS-1$
		} catch (NumberFormatException e) {
			throw new iTrustException(Messages.getString("ViewOfficeVisitAction.1")); //$NON-NLS-1$
		}
	}

	/**
	 * Returns the office visit as an OfficeVisitBean for the ovID that was initially passed to the
	 * constructor
	 * 
	 * @return the OfficeVisitBean for the ovID
	 * @throws iTrustException
	 */
	public OfficeVisitBean getOfficeVisit() throws iTrustException {
		return ovDAO.getOfficeVisit(ovID);
	}

	/**
	 * Returns the prescriptions associated with the ovID initially passed to the constructor
	 * 
	 * @return list of PrescriptionBeans for the ovID
	 * @throws DBException
	 */
	public List<PrescriptionBean> getPrescriptions() throws DBException {
		return prescriptionsDAO.getList(ovID);
	}

	public List<ProcedureBean> getAllProcedures() throws DBException {
		return proceduresDAO.getList(ovID);
	}

	public List<ProcedureBean> getProcedures() throws DBException {
		return proceduresDAO.getMedProceduresList(ovID);
	}
	
	public List<ProcedureBean> getImmunizations() throws DBException {
		return proceduresDAO.getImmunizationList(ovID);
	}
	
	public List<DiagnosisBean> getDiagnoses() throws DBException {
		return diagnosesDAO.getList(ovID);
	}
	
	public List<LabProcedureBean> getLabProcedures() throws DBException {
		return labProceduresDAO.getLabProceduresForPatientOV(ovID);
	}
	
	public void setViewed(List<LabProcedureBean> procs) throws DBException {
		for (LabProcedureBean b : procs) {
			b.setViewedByPatient(true);
			labProceduresDAO.markViewed(b);
		}
	}

	/**
	 * Returns the name of the HCP for the hcpID passed as a param
	 * 
	 * @param hcpID
	 * @return the name of the HCP
	 * @throws iTrustException
	 */
	public String getHCPName(long hcpID) throws iTrustException {
		String name = null;
		try {
			name = personnelDAO.getName(hcpID);
		} catch (iTrustException e) {
			e.printStackTrace();
			name = e.getMessage();
		}
		return name;
			
	}
}
