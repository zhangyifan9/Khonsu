package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.beans.SurveyResultBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.SurveyResultDAO;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.SurveySearchValidator;

/**
 * This class is used to handle retrieving survey results from the database.  It also logs the transaction.
 */
public class ViewSurveyResultAction {
	
	private SurveyResultDAO surveyResultDAO;
	private SurveySearchValidator validator;
	long loggedInMID;
	
	/**
	 * Set up defaults.
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the person retrieving survey results.
	 */
	public ViewSurveyResultAction(DAOFactory factory, long loggedInMID) {
		surveyResultDAO = factory.getSurveyResultDAO();
		this.loggedInMID = loggedInMID;
		validator = new SurveySearchValidator();
	}
	
	/**
	 * Retrieves survey results for a whole hospital.
	 * @param bean The SurveyResultBean containing the survey result specs.
	 * @return A java.util.List of Survey Results.
	 * @throws iTrustException
	 * @throws FormValidationException
	 */
	public List<SurveyResultBean> getSurveyResultsForHospital(SurveyResultBean bean) throws iTrustException, FormValidationException {
		
		return surveyResultDAO.getSurveyResultsForHospital(bean.getHCPhospital(), bean.getHCPspecialty());

		
	}
	
	/**
	 * Retrieves survey results for a given zip code.
	 * @param bean The SurveyResultBean containing the survey result specs.
	 * @return A java.util.List of Survey Results.
	 * @throws iTrustException
	 * @throws FormValidationException
	 */
	public List<SurveyResultBean> getSurveyResultsForZip(SurveyResultBean bean) throws iTrustException, FormValidationException {
		
		validator.validate(bean);
		
		return surveyResultDAO.getSurveyResultsForZip(bean.getHCPzip(), bean.getHCPspecialty());

	}

}
