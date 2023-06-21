package edu.ncsu.csc.itrust.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.RemoteMonitoringDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Handles retrieving the patient data for a certain HCP as used by viewTelemedicineData.jsp
 * 
 */
public class ViewMyRemoteMonitoringListAction {
	private RemoteMonitoringDAO rmDAO;
	private AuthDAO authDAO;
	private long loggedInMID;

	/**
	 * Constructor
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the HCP retrieving the patient data.
	 */
	public ViewMyRemoteMonitoringListAction(DAOFactory factory, long loggedInMID) {
		this.loggedInMID = loggedInMID;
		this.rmDAO = factory.getRemoteMonitoringDAO();
		this.authDAO = factory.getAuthDAO();
	}

	/**
	 * Returns a list of RemoteMonitoringDataBeans for the logged in HCP
	 * 
	 * @return list of TransactionBeans
	 * @throws DBException
	 * @throws FormValidationException
	 */
	public List<RemoteMonitoringDataBean> getPatientsData() throws DBException {
		return rmDAO.getPatientsData(loggedInMID);
	}
	
	/**
	 * Returns a list of RemoteMonitoringDataBeans for the logged in HCP
	 * 
	 * @return list of TransactionBeans
	 * @throws DBException
	 * @throws FormValidationException
	 */
	public List<RemoteMonitoringDataBean> getPatientDataByDate(long patientMID, String startDate, String endDate) throws DBException,FormValidationException {
		Date lower;
		Date upper;
		try {
			lower = new SimpleDateFormat("MM/dd/yyyy").parse(startDate);
			upper = new SimpleDateFormat("MM/dd/yyyy").parse(endDate);
			if (lower.after(upper))
				throw new FormValidationException("Start date must be before end date!");
		} catch (ParseException e) {
			throw new FormValidationException("Enter dates in MM/dd/yyyy");
		}		
		
		return rmDAO.getPatientDataByDate(patientMID, lower, upper);
		
	}
	
	/**
	 * Returns a list of RemoteMonitoringDataBeans for the logged in HCP based on a certain data type
	 * @param patientMID
	 * @param dataType
	 * @return list of TransactionBeans
	 * @throws DBException
	 * @throws FormValidationException
	 */
	public List<RemoteMonitoringDataBean> getPatientDataByType(long patientMID, String dataType) throws DBException,FormValidationException {

		String types[] = {"weight", "systolicBloodPressure", "diastolicBloodPressure", "glucoseLevel",
				"pedometerReading"};
		boolean valid = false;
		for (String dType : types) {
			if (dType.equals(dataType)) {
				valid = true;
				break;
			}
		}
		
		if (!valid) {
			throw new FormValidationException("Input must be a valid telemedicine data type!");
		}
		
		return rmDAO.getPatientDataByType(patientMID, dataType);
	}
	
	public List<RemoteMonitoringDataBean> getPatientDataWithoutLogging() throws DBException {
		return rmDAO.getPatientsData(loggedInMID);
	}
	
	/**
	 * returns the patient name
	 * 
	 * @return patient name
	 * @throws DBException
	 * @throws iTrustException
	 */
	public String getPatientName(long pid) throws DBException, iTrustException {
		return authDAO.getUserName(pid);
	}
	
	/**
	 * Useful to figure out who is monitoring a given patient
	 * 
	 * @return list of HCPs monitoring this patient
	 * @throws DBException
	 * @throws iTrustException
	 */
	public List<PersonnelBean> getMonitoringHCPs() throws DBException, iTrustException {
		return rmDAO.getMonitoringHCPs(loggedInMID);
	}
}
