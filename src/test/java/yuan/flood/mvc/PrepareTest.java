package yuan.flood.mvc;

import yuan.flood.phase.PreparePhaseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrepareTest {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date=simpleDateFormat.parse("2018-04-03 15:32:04.681");
        System.out.println( date.getTime());
        int x=0;
    }


}
