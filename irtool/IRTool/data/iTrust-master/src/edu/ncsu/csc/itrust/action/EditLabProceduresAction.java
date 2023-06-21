package edu.ncsu.csc.itrust.action;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.action.base.EditOfficeVisitBaseAction;
import edu.ncsu.csc.itrust.beans.LOINCbean;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.LOINCDAO;
import edu.ncsu.csc.itrust.dao.mysql.LabProcedureDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Handles lab procedures
 * add lab procedure
 * Edit lab procedure
 * Remove lab procedure
 * @author student
 * @author student
 *
 */
public class EditLabProceduresAction extends EditOfficeVisitBaseAction {
	
	private LabProcedureDAO labProcedureDAO;
	private PersonnelDAO personnelDAO;
	private LOINCDAO loincDAO;
	
	public EditLabProceduresAction(DAOFactory factory, long hcpid, 
			   	String pidString, String ovIDString) 
		throws iTrustException {
		super(factory, hcpid, pidString, ovIDString);
		labProcedureDAO = factory.getLabProcedureDAO();
		personnelDAO = factory.getPersonnelDAO();
		loincDAO = factory.getLOINCDAO();
	}
	
	public EditLabProceduresAction(DAOFactory factory, long hcpid, 
				String pidString) 
		throws iTrustException {
		super(factory, hcpid, pidString);
		labProcedureDAO = factory.getLabProcedureDAO();
		personnelDAO = factory.getPersonnelDAO();
		loincDAO = factory.getLOINCDAO();
		
	}
	
	public List<LabProcedureBean> getLabProcedures() throws DBException {
		if (isUnsaved()) {
			return new ArrayList<LabProcedureBean>();
		} else {
			return labProcedureDAO.getAllLabProceduresForDocOV(getOvID());
		}
	}
	
	public LabProcedureBean getLabProcedure(long id) throws iTrustException {
		verifySaved();
		return labProcedureDAO.getLabProcedure(id);
	}
	
	public void addLabProcedure(LabProcedureBean bean) throws iTrustException {
		verifySaved();
		// TODO: choose lab tech if not assigned in bean
		if ("".equals(bean.getStatus())) {
			bean.setStatus(LabProcedureBean.In_Transit);
		}
		labProcedureDAO.addLabProcedure(bean);
	}
	
	public LabProcedureBean autoAssignLabTech(LabProcedureBean bean) throws iTrustException {
		verifySaved();
		// TODO: determine the lab tech using more intelligent means
		List<PersonnelBean> techs = personnelDAO.getLabTechs();
		if (!techs.isEmpty()) {
			bean.setLTID(techs.get(0).getMID());
		} else {
			bean.setLTID(0);
		}

		return bean;
	}
	
	public void editLabProcedure(LabProcedureBean bean) throws iTrustException {
		verifySaved();
		labProcedureDAO.updateLabProcedure(bean);
	}
	
	public void deleteLabProcedure(LabProcedureBean bean) throws iTrustException {
		verifySaved();
		labProcedureDAO.removeLabProcedure(bean.getProcedureID());
	}
	
	public List<PersonnelBean> getLabTechs() throws iTrustException {
		return personnelDAO.getLabTechs();
	}
	
	public String getLabTechName(long mid) throws iTrustException {
		try {
			return personnelDAO.getName(mid);
		} catch (iTrustException e) {
			return "";
		}
	}
	
	public int getLabTechQueueSize(long mid) throws iTrustException {
		return labProcedureDAO.getLabTechQueueSize(mid);
	}
	
	public int[] getLabTechQueueSizeByPriority(long mid) throws iTrustException {
		return labProcedureDAO.getLabTechQueueSizeByPriority(mid);
	}
	
	public List<LOINCbean> getLabProcedureCodes() throws DBException {
		return loincDAO.getAllLOINC();
	}
	
}
