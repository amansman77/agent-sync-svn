package kr.co.micube.component.property;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AppProperty {

	private Properties appProps;
	private String appConfigPath;
	
	private AppProperty(){
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		appConfigPath = rootPath + "application.properties";
		 
		appProps = new Properties();
		try {
			appProps.load(new FileInputStream(appConfigPath));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("error while load property: " + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("error while load property: " + e.getMessage());
		}
	}
	
	private static class SingleTonHolder{
        private static final AppProperty instance = new AppProperty();
    }
	
	public static AppProperty getInstance(){
        return SingleTonHolder.instance;
    }

	public Properties getAppProps() {
		return appProps;
	}

	public String getProperty(String key) {
		return appProps.getProperty(key);
	}

	public void setProperty(String key, String value) {
		appProps.setProperty(key, value);
		try {
			appProps.store(new FileOutputStream(appConfigPath), null);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("error while store property: " + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("error while store property: " + e.getMessage());
		}
	}
	
}
