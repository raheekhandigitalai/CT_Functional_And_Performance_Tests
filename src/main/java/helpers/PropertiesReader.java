package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.Properties;

public class PropertiesReader {

    public PropertiesReader() {}

    public String getProperty(String property) {
        Properties props = System.getProperties();

        try {
            props.load(Files.newInputStream(new File(System.getProperty("user.dir") + "/config.properties").toPath()));

            if (props.getProperty("seetest.accessKey").isEmpty()) {
                throw new Exception("SeeTest Cloud Access Key not found. Please look in config.properties.");
            }

            if (props.getProperty("seetest.cloudURL").isEmpty()) {
                throw new Exception("SeeTest Cloud Base URL not found. Please look in config.properties.");
            }

            if (props.getProperty("QABuild").isEmpty()) {
                throw new Exception("QA Build not found. Please look in config.properties.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return System.getProperty(property);
    }

}