package edu.ncsu.csc.itrust.charts;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.links.CategoryItemLinkGenerator;
import de.laures.cewolf.tooltips.CategoryToolTipGenerator;
import edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean;

public class DiagnosisTrendData implements DatasetProducer, CategoryToolTipGenerator, CategoryItemLinkGenerator, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4675064587294163834L;
		
	private String[] seriesName;

	// Initialize the values for each week to 0
	private int[] values;
	
	// List of the beans for the diagnosis statistics 
	private DiagnosisStatisticsBean dsBean = new DiagnosisStatisticsBean();
	
	private DiagnosisStatisticsBean avgBean = new DiagnosisStatisticsBean();
	    
	// Name of the diagnosis being searched for
	private String diagnosisName;
	
	private Boolean epidemic = false;

	public boolean hasData() {
			
		if ( dsBean != null ) {
			
	    	return true;
	    		
	    }

	  	return false;
	    
	}
	 
	 /**
	     * Called from the JSP to initialize the list of HealthRecords needed
	     * to produce the desired chart.
	     * 
	     * @param hRecs Health records for the patient being viewed.
	     * @param name Type of data that is being graphed (originally Height, Weight, and BMI).
	     */
	public void initializeDiagnosisStatistics ( DiagnosisStatisticsBean DiagnosisBean, String name ) {
		
		this.dsBean = DiagnosisBean;
	    this.diagnosisName = name;
	    
	    values = new int[2];
	    values[0] = (int) dsBean.getZipStats();
	    values[1] = (int) dsBean.getRegionStats();
	    	
	}
	
	public void initializeAvgDiagnosisStatistics( DiagnosisStatisticsBean avgBean, DiagnosisStatisticsBean dsBean, String name ) {
		
		this.dsBean = dsBean;
		this.avgBean = avgBean;
		this.diagnosisName = name;
		this.epidemic = true;
		
		values = new int[4];
		values[0] = (int) dsBean.getZipStats();
		values[1] = (int) avgBean.getZipStats();
		values[2] = (int) dsBean.getRegionStats();
		values[3] = (int) avgBean.getRegionStats();
		
	}
	
	    /**
	     * This method parses the list of DiagnosisStatistics Beans to initialize the
	     * chart data set.
	     * @param Map parameters passed from CeWolf (though not actually used locally)
	     * @return A data set containing information to be graphed
	     * @throws DatasetProduceException
	     */
		public Object produceDataset(Map params) throws DatasetProduceException {
	    	// The DefaultCategoryDataset is used for bar charts.
	    	// This data set class may change based on the type of chart you wish to produce.
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				/**
				 * @see java.lang.Object#finalize()
				 */
				@Override
				protected void finalize() throws Throwable {
					super.finalize();
				}
	        };
	        
	        if ( epidemic == false ) {
	        	
	        	seriesName = new String[2];
	        	
	        	dataset.addValue(values[0], diagnosisName, "Zipcode Cases");
	        	dataset.addValue(values[1], diagnosisName, "Region Cases");
	        	seriesName[0] = "Zipcode Case";
	        	seriesName[1] = "Region Cases";
	        	
	        	
	        } else if ( epidemic == true ) {
	        	
	        	seriesName = new String[4];
	        	
	        	dataset.addValue(values[0], diagnosisName, "Current Week Zipcode Cases");
	        	dataset.addValue(values[1], diagnosisName, "Average Prior Zipcode Cases");
	        	dataset.addValue(values[2], diagnosisName, "Current Week Region Cases");
	        	dataset.addValue(values[3], diagnosisName, "Average Prior Region Cases");
	        	 	
	        	seriesName[0] = "Current Week Zipcode Cases";
	        	seriesName[1] = "Average Prior Zipcode Cases";
	        	seriesName[2] = "Current Week Region Cases";
	        	seriesName[3] = "Average Prior Region Cases";
	        	
	        }

	        return dataset;
	    }

	    /**
	     * This producer's data is invalidated after 5 seconds. By this method the
	     * producer can influence Cewolf's caching behavior the way it wants to.
	     */
		public boolean hasExpired(Map params, Date since) {		
			return (System.currentTimeMillis() - since.getTime())  > 5000;
		}

		/**
		 * @return A unique ID for this DatasetProducer
		 */
		public String getProducerId() {
			return "DiagnosisTrendData DatasetProducer";
		}

	    /**
	     * @return A link target for a special data item.
	     */
	    public String generateLink(Object data, int series, Object category) {
	        return seriesName[series];
	    }
	    
		/**
		 * @see java.lang.Object#finalize()
		 */
		@Override
		protected void finalize() throws Throwable {
			super.finalize();
		}

		/**
		 * @see org.jfree.chart.tooltips.CategoryToolTipGenerator#generateToolTip(CategoryDataset, int, int)
		 */
		public String generateToolTip(CategoryDataset arg0, int series, int arg2) {
			return seriesName[series];
		}
		
}
	


