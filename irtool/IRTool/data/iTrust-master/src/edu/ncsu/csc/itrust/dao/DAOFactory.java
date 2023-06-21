package edu.ncsu.csc.itrust.dao;

import java.sql.Connection;
import java.sql.SQLException;
import edu.ncsu.csc.itrust.dao.mysql.*;
import edu.ncsu.csc.itrust.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

/**
 * The central mediator for all Database Access Objects. The production instance uses the database connection pool
 * provided by Tomcat (so use the production instance when doing stuff from JSPs in the "real code"). Both the
 * production and the test instance parses the context.xml file to get the JDBC connection.
 * 
 * Also, @see {@link EvilDAOFactory} and @see {@link TestDAOFactory}.
 * 
 * Any DAO that is added to the system should be added in this class, in the same way that all other DAOs are.
 * 
 * @author Andy
 * 
 */
public class DAOFactory {
	private static DAOFactory productionInstance = null;
	private IConnectionDriver driver;

	/**
	 * 
	 * @return A production instance of the DAOFactory, to be used in deployment (by Tomcat).
	 */
	public static DAOFactory getProductionInstance() {
		if (productionInstance == null)
			productionInstance = new DAOFactory();
		return productionInstance;
	}

	protected DAOFactory() {
		this.driver = new ProductionConnectionDriver();
	}

	public Connection getConnection() throws SQLException {
		return driver.getConnection();
	}

	public AccessDAO getAccessDAO() {
		return new AccessDAO(this);
	}

	public AllergyDAO getAllergyDAO() {
		return new AllergyDAO(this);
	}
	
	public ApptDAO getApptDAO() {
		return new ApptDAO(this);
	}
	
	public ApptTypeDAO getApptTypeDAO() {
		return new ApptTypeDAO(this);
	}

	public AuthDAO getAuthDAO() {
		return new AuthDAO(this);
	}

	public CPTCodesDAO getCPTCodesDAO() {
		return new CPTCodesDAO(this);
	}
	
	public DrugInteractionDAO getDrugInteractionDAO() {
		return new DrugInteractionDAO(this);
	}

	public FamilyDAO getFamilyDAO() {
		return new FamilyDAO(this);
	}

	public HealthRecordsDAO getHealthRecordsDAO() {
		return new HealthRecordsDAO(this);
	}

	public HospitalsDAO getHospitalsDAO() {
		return new HospitalsDAO(this);
	}

	public ICDCodesDAO getICDCodesDAO() {
		return new ICDCodesDAO(this);
	}

	public NDCodesDAO getNDCodesDAO() {
		return new NDCodesDAO(this);
	}

	public OfficeVisitDAO getOfficeVisitDAO() {
		return new OfficeVisitDAO(this);
	}

	public PatientDAO getPatientDAO() {
		return new PatientDAO(this);
	}
	
	public ProfilePhotoDAO getProfilePhotoDAO() {
		return new ProfilePhotoDAO(this);
	}

	public PersonnelDAO getPersonnelDAO() {
		return new PersonnelDAO(this);
	}

	public ReferralDAO getReferralDAO() {
		return new ReferralDAO(this);
	}
	
	public RiskDAO getRiskDAO() {
		return new RiskDAO(this);
	}

	public TransactionDAO getTransactionDAO() {
		return new TransactionDAO(this);
	}

	public VisitRemindersDAO getVisitRemindersDAO() {
		return new VisitRemindersDAO(this);
	}

	public FakeEmailDAO getFakeEmailDAO() {
		return new FakeEmailDAO(this);
	}

	public ReportRequestDAO getReportRequestDAO() {
		return new ReportRequestDAO(this);
	}

	public SurveyDAO getSurveyDAO() {
		return new SurveyDAO(this);
	}

	public LabProcedureDAO getLabProcedureDAO() {
		return new LabProcedureDAO(this);
	}

	public LOINCDAO getLOINCDAO() {
		return new LOINCDAO(this);
	}

	public SurveyResultDAO getSurveyResultDAO() {
		return new SurveyResultDAO(this);
	}
	
	public MessageDAO getMessageDAO() {
		return new MessageDAO(this);
	}
	
	public AdverseEventDAO getAdverseEventDAO() {
		return new AdverseEventDAO(this);
	}
	
	public RemoteMonitoringDAO getRemoteMonitoringDAO() {
		return new RemoteMonitoringDAO(this);
	}

	public PrescriptionsDAO getPrescriptionsDAO() {
		return new PrescriptionsDAO(this);
	}

	public DiagnosesDAO getDiagnosesDAO() {
		return new DiagnosesDAO(this);
	}

	public ProceduresDAO getProceduresDAO() {
		return new ProceduresDAO(this);
	}

	public PrescriptionReportDAO getPrescriptionReportDAO() {
		return new PrescriptionReportDAO(this);
	}

	public ReasonCodesDAO getORCodesDAO() {
		return new ReasonCodesDAO(this);
	}

	public PatientInstructionsDAO getPatientInstructionsDAO() {
		return new PatientInstructionsDAO(this);
	}
	
}
