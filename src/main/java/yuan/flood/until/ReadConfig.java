package yuan.flood.until;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Created by Yuan on 2017/1/16.
 */
@Component
public class ReadConfig {
    public void read(){
        Properties properties=new Properties();
        try {
            //get Config.properties from the resources
            URI uri=new URI(Thread.currentThread().getContextClassLoader().getResource("/config.properties").toString());
            InputStream inputStream=new BufferedInputStream(
                    new FileInputStream(uri.getPath()));
            InputStreamReader in = new InputStreamReader(inputStream,"UTF8");
            properties.load(in);//load the input stream of the property file
            SOSSESConfig sensorConfigInfo = new SOSSESConfig(properties);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }catch (URISyntaxException e){
            System.out.println(e.getMessage());
        }
    }

}
