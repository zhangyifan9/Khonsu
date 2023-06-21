package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean;
import edu.ncsu.csc.itrust.beans.TelemedicineBean;
import edu.ncsu.csc.itrust.beans.loaders.RemoteMonitoringDataBeanLoader;
import edu.ncsu.csc.itrust.beans.loaders.PersonnelLoader;
import edu.ncsu.csc.itrust.beans.loaders.RemoteMonitoringListsBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Used for the keeping track of remote monitoring data.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 * 
 */
public class RemoteMonitoringDAO {
	private DAOFactory factory;
	private RemoteMonitoringDataBeanLoader loader = new RemoteMonitoringDataBeanLoader();
	private RemoteMonitoringListsBeanLoader rmListLoader = new RemoteMonitoringListsBeanLoader();
	private PersonnelLoader personnelLoader = new PersonnelLoader();

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public RemoteMonitoringDAO(DAOFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * Return remote monitoring list data for a given patient.
	 * 
	 * @param patientMID Patient to retrieve data for.
	 * @return List of TelemedicineBeans
	 * @throws DBException
	 */
	public List<TelemedicineBean> getTelemedicineBean(long patientMID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM RemoteMonitoringLists WHERE PatientMID=?");
			ps.setLong(1, patientMID);
			ResultSet rs = ps.executeQuery();
			
			return rmListLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns patient data for a given HCP
	 * 
	 * @return
	 * @throws DBException
	 */
	public List<RemoteMonitoringDataBean> getPatientsData(long loggedInMID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM RemoteMonitoringLists WHERE HCPMID=? ORDER BY PatientMID");
			ps.setLong(1, loggedInMID);
			ResultSet patientRS = ps.executeQuery();
			ps = conn.prepareStatement("SELECT * FROM RemoteMonitoringData WHERE timelogged >= CURRENT_DATE ORDER BY PatientID, timeLogged DESC");
			ResultSet dataRS = ps.executeQuery();

			List<String> patientList = new ArrayList<String>();
			while(patientRS.next()) {
				patientList.add(patientRS.getLong("PatientMID") + "");
			}
			List<RemoteMonitoringDataBean> dataList = loader.loadList(dataRS);			
			
			int i, j;
			//Go through all patients and remove any that aren't monitored by this HCP
			for(i = 0; i < dataList.size(); i++) {
				if(!patientList.contains(dataList.get(i).getPatientMID() + "")) {
					dataList.remove(i);
					i--;
				}
			}
			
			//Add values in patient list with no data for today to list.
			boolean itsThere;
			for(i = 0; i < patientList.size(); i++) {
				itsThere = false;
				for(j = 0; j < dataList.size(); j++) {
					if((dataList.get(j).getPatientMID() + "").equals(patientList.get(i))) {
						itsThere = true;
						break;
					}
				}
				if(!itsThere) {
					dataList.add(new RemoteMonitoringDataBean(Long.parseLong(patientList.get(i))));
				}
			}
			
			return dataList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	public List<RemoteMonitoringDataBean> getPatientDataByDate(long patientMID, Date lower, Date upper) throws DBException{
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM remotemonitoringdata WHERE PatientID=? AND timeLogged >= ? AND timeLogged <= ? ORDER BY timeLogged DESC");
			ps.setLong(1, patientMID);
			ps.setTimestamp(2, new Timestamp(lower.getTime()));
			// add 1 day's worth to include the upper
			ps.setTimestamp(3, new Timestamp(upper.getTime() + 1000L * 60L * 60 * 24L));
			ResultSet rs = ps.executeQuery();
			List<RemoteMonitoringDataBean> dataList = loader.loadList(rs);
			return dataList;
		}catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Get the requested type of data for the specified patient.
	 * 
	 * @param patientMID The MID of the patient
	 * @param dataType The type of telemedicine data to return
	 * @return A list of beans which all contain information of the requested type
	 * @throws DBException
	 */
	public List<RemoteMonitoringDataBean> getPatientDataByType(long patientMID, String dataType) throws DBException{
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM remotemonitoringdata WHERE PatientID=? AND "+dataType+" != -1 ORDER BY timeLogged ASC");
			ps.setLong(1, patientMID);

			ResultSet rs = ps.executeQuery();
			List<RemoteMonitoringDataBean> dataList = loader.loadList(rs);
			return dataList;
		}catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Store pedometer reading and height/weight data for a given patient in the RemoteMonitoringData table
	 * 
	 * @param patientMID The MID of the patient
	 * @param height The height of the patient
	 * @param weight The weight of the patient
	 * @param pedometerReading The pedometer reading of the patient
	 * @param reporterRole  The role of the person that reported these monitoring stats
	 * @param reporterMID  The MID of the person that reported these monitoring stats
	 * @throws DBException
	 */
	public void storePatientData(long patientMID, RemoteMonitoringDataBean bean, String reporterRole, long reporterMID)
	throws DBException, iTrustException {
		float height = bean.getHeight();
		float weight = bean.getWeight();
		int pedometer = bean.getPedometerReading();
		int sbp = bean.getSystolicBloodPressure();
		int dbp = bean.getDiastolicBloodPressure();
		int glucose = bean.getGlucoseLevel();
		
		if (height == 0)	height		= -1;
		if (weight == 0)	weight		= -1;
		if (pedometer == 0)	pedometer	= -1;
		if (sbp == 0)		sbp			= -1;
		if (dbp == 0)		dbp			= -1;
		if (glucose == 0)	glucose		= -1;
		
		if (getNumberOfDailyEntries(patientMID, "height") >= 1 && height != -1)
			throw new iTrustException("Patient height entries for today cannot exceed 1.");
		if (getNumberOfDailyEntries(patientMID, "weight") >= 1 && weight != -1)
			throw new iTrustException("Patient weight entries for today cannot exceed 1.");
		if (getNumberOfDailyEntries(patientMID, "pedometerReading") >= 1 && pedometer != -1)
			throw new iTrustException("Patient pedometer reading entries for today cannot exceed 1.");
		if (getNumberOfDailyEntries(patientMID, "glucoseLevel") >= 10 && glucose != -1)
			throw new iTrustException("Patient glucose level entries for today cannot exceed 10.");
		if (getNumberOfDailyEntries(patientMID, "systolicBloodPressure") >= 10 && sbp != -1)
			throw new iTrustException("Patient systolic blood pressure entries for today cannot exceed 10.");
		if (getNumberOfDailyEntries(patientMID, "diastolicBloodPressure") >= 10 && dbp != -1)
			throw new iTrustException("Patient diastolic blood pressure entries for today cannot exceed 10.");
		
		if(reporterRole.equals("patient representative"))
			validatePR(reporterMID, patientMID);
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO RemoteMonitoringData(PatientID, height, weight, "
					+ "pedometerReading, systolicBloodPressure, diastolicBloodPressure, glucoseLevel, ReporterRole, ReporterID) VALUES(?,?,?,?,?,?,?,?,?)");
			ps.setLong(1, patientMID);
			ps.setFloat(2, height);
			ps.setFloat(3, weight);
			ps.setInt(4, pedometer);
			ps.setInt(5, sbp);
			ps.setInt(6, dbp);
			ps.setInt(7, glucose);
			ps.setString(8, reporterRole);
			ps.setLong(9, reporterMID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Private method to get the number of entries for a certain patientID for today.
	 * @param patientMID
	 * @return the number of entries
	 * @throws DBException
	 */
/*	private int getNumberOfDailyEntries(long patientMID) throws DBException{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM RemoteMonitoringData WHERE PatientID=? AND DATE(timeLogged)=CURRENT_DATE");
			ps.setLong(1, patientMID);
			ResultSet rs = ps.executeQuery();
			List<RemoteMonitoringDataBean> patients = loader.loadList(rs);
			return patients.size();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}*/
	
	/**
	 * Private method to get the number of entries for a certain patientID and a certain
	 * data type for today.
	 * @param patientMID
	 * @param dataType
	 * @return the number of entries
	 * @throws DBException
	 */
	private int getNumberOfDailyEntries(long patientMID, String dataType) throws DBException{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM RemoteMonitoringData WHERE PatientID=? AND "
										+ dataType + "!=? AND DATE(timeLogged)=CURRENT_DATE");
			ps.setLong(1, patientMID);
			ps.setInt(2, -1);
			ResultSet rs = ps.executeQuery();
			List<RemoteMonitoringDataBean> patients = loader.loadList(rs);
			return patients.size();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	public void validatePR(long representativeMID, long patientMID)
			throws iTrustException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Representatives WHERE RepresenterMID=? AND RepresenteeMID=?");
			ps.setLong(1, representativeMID);
			ps.setLong(2, patientMID);
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) //no rows
				throw new iTrustException("Representer is not valid for patient " + patientMID);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Show the list of HCPs monitoring this patient
	 * 
	 * @param patientMID The MID of the patient
	 * @return list of HCPs monitoring the provided patient
	 */
	public List<PersonnelBean> getMonitoringHCPs(long patientMID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM Personnel, RemoteMonitoringLists "
					+ "WHERE RemoteMonitoringLists.PatientMID=? AND RemoteMonitoringLists.HCPMID=Personnel.MID");
			ps.setLong(1, patientMID);
			ResultSet rs = ps.executeQuery();
			return personnelLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	
	/**
	 * Add a patient to the list of HCPs' monitoring lists of Patients
	 * 
	 * @param patientMID The MID of the patient
	 * @param HCPMID The MID of the HCP
	 * @param tBean The TelemedicineBean indicating what telemedicine data the patient is allowed to enter.
	 * @return true if added successfully, false if already in list
	 */
	public boolean addPatientToList(long patientMID,
									long HCPMID,
									TelemedicineBean tBean) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM RemoteMonitoringLists WHERE PatientMID = ? AND HCPMID = ?");
			ps.setLong(1, patientMID);
			ps.setLong(2, HCPMID);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return false;
			String permissionPS = "SystolicBloodPressure, DiastolicBloodPressure, GlucoseLevel, Height, Weight, PedometerReading";
			ps = conn.prepareStatement("INSERT INTO RemoteMonitoringLists(PatientMID, HCPMID, " + permissionPS + ") VALUES(?,?,?,?,?,?,?,?)");
			ps.setLong(1, patientMID);
			ps.setLong(2, HCPMID);
			ps.setBoolean(3, tBean.isSystolicBloodPressureAllowed());
			ps.setBoolean(4, tBean.isDiastolicBloodPressureAllowed());
			ps.setBoolean(5, tBean.isGlucoseLevelAllowed());
			ps.setBoolean(6, tBean.isHeightAllowed());
			ps.setBoolean(7, tBean.isWeightAllowed());
			ps.setBoolean(8, tBean.isPedometerReadingAllowed());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Remove a patient from the list of HCPs' monitoring lists of Patients
	 * 
	 * @param patientMID The MID of the patient
	 * @param HCPMID The MID of the HCP
	 * @return true if removed successfully, false if not in list
	 */
	public boolean removePatientFromList(long patientMID, long HCPMID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("DELETE FROM RemoteMonitoringLists WHERE PatientMID = ? AND HCPMID = ?");
			ps.setLong(1, patientMID);
			ps.setLong(2, HCPMID);
			if(ps.executeUpdate() == 0)
				return false;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
}
