package ir.utux.config;
import lombok.Data;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Data
public class WebConfiguration {

    private Integer port;
    private String webFolder;
    private String defaultPage;

    public WebConfiguration() {


        try (FileInputStream fileInputStream=new FileInputStream("config/setting.conf")) {
            Properties prop=new Properties();
            prop.load(fileInputStream);
            try {
                this.port=Integer.valueOf(prop.getProperty("port"));
                this.webFolder=prop.getProperty("directory");
                this.defaultPage=prop.getProperty("defaultPage");

            } catch (RuntimeException e) {
                throw new RuntimeException("Can Not Read Correct Property in setting.conf");
            }

        } catch (IOException e) {
            throw new RuntimeException("Can Not Read 'ir.utux.config/setting.conf' File");
        }
    }

}
