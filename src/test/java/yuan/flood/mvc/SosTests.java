package yuan.flood.mvc;

import yuan.flood.sos.Decode;

import java.io.*;

/**
 * Created by Yuan on 2017/1/15.
 */
public class SosTests {
    public static void main(String[] args) {
        int x=0;
        try {
            InputStream inputStream=new BufferedInputStream(
                    new FileInputStream("E:\\SES\\Yuan\\WebÍøÕ¾ SpringMVC+Hibernate\\Spring_Flood_Project\\target\\classes\\config.properties"));
           int y=0;
            InputStreamReader in = new InputStreamReader(inputStream,"UTF8");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        //InputStreamReader reader=new InputStreamReader(new FileInputStream("E:\\SES\\Yuan\\WebÍøÕ¾%20SpringMVC+Hibernate\\Spring_Flood_Project\\target\\Spring_Flood_Project\\WEB-INF\\classes\\config.properties"));
    }
}
