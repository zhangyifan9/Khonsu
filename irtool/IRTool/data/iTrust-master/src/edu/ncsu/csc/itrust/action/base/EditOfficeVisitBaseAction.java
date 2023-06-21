/**
 * 
 */
package edu.ncsu.csc.itrust.action.base;

import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Base class for actions used to edit office visits.  This stores the HCP id, 
 * patient id, and office visit id.
 * 
 * @author student
 * @author student
 */

public class EditOfficeVisitBaseAction extends OfficeVisitBaseAction {
		
	private long hcpid;

	/**
	 * @param factory
	 * @param pidString
	 * @param ovIDString
	 * @throws iTrustException
	 */
	
	public EditOfficeVisitBaseAction(DAOFactory factory, long hcpid, String pidString, String ovIDString)
			throws iTrustException {
		super(factory, pidString, ovIDString);
		this.hcpid = hcpid;
	}
	
	/**
	 * An office visit that is initially unsaved.
	 * 
	 * @param factory
	 * @param hcpid
	 * @param pidString
	 * @throws iTrustException
	 */
	public EditOfficeVisitBaseAction(DAOFactory factory, long hcpid, String pidString)
			throws iTrustException {
		super(factory, pidString);
		this.hcpid = hcpid;
	}
	
	/**
	 * Get the HCP id.
	 * 
	 * @return the HCP id
	 */
	public long getHcpid() {
		return hcpid;
	}

}
