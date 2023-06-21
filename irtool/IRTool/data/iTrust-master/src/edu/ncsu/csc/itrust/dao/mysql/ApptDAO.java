package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.beans.loaders.ApptBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;

public class ApptDAO {
	private DAOFactory factory;
	private ApptBeanLoader abloader;
	
	public ApptDAO(DAOFactory factory) {
		this.factory = factory;
		this.abloader = new ApptBeanLoader();
	}
	
	public List<ApptBean> getAppt(int apptID) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		conn = factory.getConnection();
		
		ps = conn.prepareStatement("SELECT * FROM appointment WHERE appt_id=?");
		
		ps.setInt(1, apptID);
		
		ResultSet rs = ps.executeQuery();
		List<ApptBean> abList = this.abloader.loadList(rs);
		DBUtil.closeConnection(conn, ps);
		return abList;
	}
	
	public List<ApptBean> getApptsFor(long mid) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		conn = factory.getConnection();
		
		if(mid >= 999999999){
			ps = conn.prepareStatement("SELECT * FROM appointment WHERE doctor_id=? ORDER BY sched_date;");
		}
		else {
			ps = conn.prepareStatement("SELECT * FROM appointment WHERE patient_id=? ORDER BY sched_date;");
		}
		
		ps.setLong(1, mid);
		
		ResultSet rs = ps.executeQuery();
		List<ApptBean> abList = this.abloader.loadList(rs);
		DBUtil.closeConnection(conn, ps);
		return abList;
	}
	
	public void scheduleAppt(ApptBean appt) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		conn = factory.getConnection();
		
		ps = conn.prepareStatement(
				"INSERT INTO appointment (appt_type, patient_id, doctor_id, sched_date, comment) "
			  + "VALUES (?, ?, ?, ?, ?)");
		ps = this.abloader.loadParameters(ps, appt);
		
		ps.executeUpdate();
		DBUtil.closeConnection(conn, ps);
	}
	
	public void editAppt(ApptBean appt) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		conn = factory.getConnection();

		ps = conn.prepareStatement(
				"UPDATE appointment SET appt_type=?, sched_date=?, comment=? WHERE appt_id=?");
		ps.setString(1, appt.getApptType());
		ps.setTimestamp(2, appt.getDate());
		ps.setString(3, appt.getComment());
		ps.setInt(4, appt.getApptID());
		
		ps.executeUpdate();
		DBUtil.closeConnection(conn,ps);
	}
	
	public void removeAppt(ApptBean appt) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		conn = factory.getConnection();

		ps = conn.prepareStatement(
				"DELETE FROM appointment WHERE appt_id=?");
		ps.setInt(1, appt.getApptID());
		
		ps.executeUpdate();
		DBUtil.closeConnection(conn,ps);
	}
}
