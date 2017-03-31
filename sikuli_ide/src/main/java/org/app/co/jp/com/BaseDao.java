package org.app.co.jp.com;

import org.app.co.jp.util.ComPropertyUtil;

public class BaseDao {
	public String patternCommonMasterDir = "";
	public String patternCommonPatternDir = "";
	public String patternCustomerDir = "";
	public String seqDir = "";
	public String parameterDir = "";
	
	public String tableDir = "";
	
	// script saved path
	public String scriptDir = "";
	
	// page saved path
	public String pageDir = "";
	
	// scenario saved path
	public String scenarioDir = "";
	
	// operation saved path
	public String operationDir = "";
	
	public String parameterFileName = "PARAMETERS.xml";
	
	public BaseDao() {
		super();
		patternCommonMasterDir = ComPropertyUtil.getProperty("pattern_common_master");
		patternCommonPatternDir = ComPropertyUtil.getProperty("pattern_common_patern");
		patternCustomerDir = ComPropertyUtil.getProperty("pattern_customer");
		seqDir = ComPropertyUtil.getProperty("system_path");
		tableDir = ComPropertyUtil.getProperty("table_path");
		parameterDir = ComPropertyUtil.getProperty("parameter_path");
		
		// script saved path
		scriptDir = ComPropertyUtil.getProperty("script_path");
		
		// page saved path
		pageDir = ComPropertyUtil.getProperty("page_path");
		
		// scenario saved path
		scenarioDir = ComPropertyUtil.getProperty("scenario_path");
		
		// operation saved path
		operationDir = ComPropertyUtil.getProperty("operation_path");
	}
	
}
