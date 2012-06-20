package com.vzb.sys.prp;

import org.jboss.system.ServiceMBean;

public interface SystemPropertiesServiceMBean extends ServiceMBean{

	public void setStartMessage(String in_msg);
	public void setStopMessage(String in_msg);
	public String getStartMessage();
	public String getStopMessage();
	public String activateLogLevel(String category, String level);
	public String getLogLevel(String category);

}
