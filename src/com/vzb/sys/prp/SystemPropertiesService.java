package com.vzb.sys.prp;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

public class SystemPropertiesService extends ServiceMBeanSupport implements SystemPropertiesServiceMBean {

	private static Logger logger = Logger.getLogger(SystemPropertiesService.class.getName());
	private String startMessage = "app server started";
	private String stopMessage = "app server ended";
	Properties env = new Properties();

	@Override protected void startService() throws Exception {
		if (startMessage != null) {
			logger.info(SystemPropertiesService.class.getName() + ": " + queryHost() + " "+ getStartMessage());
		}
		logger.info("ln:"+new Exception().getStackTrace()[0].getLineNumber()+"> SborsService: startMessage=" + startMessage);

		if (logger.isDebugEnabled()) {
			logger.debug("SborsService:> **************************************************************");
			logger.debug("SborsService:> startService().start");
			logger.debug("SborsService:> **************************************************************");
		}
		String url = "systems.properties";
		try {
			InputStream is = SystemPropertiesService.class.getResourceAsStream(url);
			if (is != null) {
				logger.info("Successfully read- '" + url+ "' file as input stream, available bytes "+ is.available());
				env.load(is);
				for (int a = 0, an = env.size(); a < an; a++) {
					System.setProperty(env.keySet().toArray()[a].toString(), env.getProperty(env.keySet().toArray()[a].toString()));
					if (logger.isDebugEnabled()) {
						logger.debug("loaded property : "+ env.keySet().toArray()[a].toString()	+ " value : "+ env.getProperty(env.keySet().toArray()[a].toString()));
					}
				}
				logger.info("Successfully loaded " + url);
				is.close();
			} else {
				logger.error("Can't load properties file : " + url);
			}
		} catch (Exception ce) {
			// Can't connect
			ce.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SborsService:> **************************************************************");
			logger.debug("SborsService:> startService().end");
			logger.debug("SborsService:> **************************************************************");

		}
	}

	@Override protected void stopService() throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("SborsService:> **************************************************************");
			logger.debug("SborsService:> stopService().start");
			logger.debug("SborsService:> **************************************************************");

		}

		if (stopMessage != null) {
			logger.info(SystemPropertiesService.class.getName() + ": " + queryHost() + " "+ getStopMessage());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SborsService:> **************************************************************");
			logger.debug("SborsService:> stopService().end");
			logger.debug("SborsService:> **************************************************************");

		}
	}

	@Override public void setStartMessage(String in_msg) {
		startMessage = in_msg;
	}

	@Override public void setStopMessage(String in_msg) {
		stopMessage = in_msg;
	}

	@Override public String getStartMessage() {
		return startMessage;
	}

	@Override public String getStopMessage() {
		return stopMessage;
	}

	@Override public String activateLogLevel(String category, String inLevel) {
		Logger l = LogManager.getLogger(category);
		if (l != null) {
			Level level = l.getLevel();
			if (level == null) {
				return "Category doesn't exist";
			}
			if (inLevel.equalsIgnoreCase("DEBUG")) {
				l.setLevel(Level.DEBUG);
			} else if (inLevel.equalsIgnoreCase("ERROR")) {
				l.setLevel(Level.ERROR);
			} else if (inLevel.equalsIgnoreCase("WARN")) {
				l.setLevel(Level.WARN);
			} else if (inLevel.equalsIgnoreCase("FATAL")) {
				l.setLevel(Level.FATAL);
			} else {
				l.setLevel(Level.INFO);
			}
		} else {
			return "Category doesn't exist";
		}
		return "SUCCESS";
	}

	@Override public String getLogLevel(String category) {

		Logger l = LogManager.getLogger(category);
		if (l == null) {
			return "Category doesn't exist";
		}
		Level level = l.getLevel();
		if (level == null) {
			return "Category doesn't exist";
		}
		if (level.equals(Level.INFO)) {
			return "INFO";
		}
		if (level.equals(Level.DEBUG)) {
			return "DEBUG";
		}
		if (level.equals(Level.ERROR)) {
			return "ERROR";
		}
		if (level.equals(Level.FATAL)) {
			return "FATAL";
		}
		if (level.equals(Level.WARN)) {
			return "WARN";
		}
		return "OFF";
	}

	private String queryHost() {
		String result = null;
		String arch = System.getProperty("os.name");
		if (arch != null && arch.startsWith("Windows")
				|| arch.equalsIgnoreCase("Linux")) {
			try {
				Process p = Runtime.getRuntime().exec("hostname");
				if (p.waitFor() == 0) {
					BufferedInputStream bns = new BufferedInputStream(p.getInputStream());
					byte[] sb = new byte[bns.available()];
					bns.read(sb);
					result = new String(sb);
				} else {
					result = "waitFor error";
				}

			} catch (IOException ioe) {
				result = "unknown IOException";
			} catch (InterruptedException ie) {
				result = "interrupted";
			}
		} else {
			result = "unknown os.name=" + arch;
		}

		return result;

	}
}
