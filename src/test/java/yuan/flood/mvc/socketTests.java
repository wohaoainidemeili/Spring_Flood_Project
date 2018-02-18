package yuan.flood.mvc;

import yuan.flood.service.DecodeWNSEventService;
import yuan.flood.sos.DataTimeSeries;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Yuan on 2017/2/19.
 */
public class socketTests {
    public static void main(String[] args){
        List<DataTimeSeries> dataTimeSeries = new ArrayList<>();
        DataTimeSeries dataTimeSeries1 = new DataTimeSeries();
        dataTimeSeries1.setDataValue(10d);
        dataTimeSeries1.setTimeLon(19L);

        DataTimeSeries dataTimeSeries2 = new DataTimeSeries();
        dataTimeSeries2.setDataValue(20d);
        dataTimeSeries2.setTimeLon(20L);

        DataTimeSeries dataTimeSeries3 = new DataTimeSeries();
        dataTimeSeries3.setDataValue(9d);
        dataTimeSeries3.setTimeLon(3L);

        dataTimeSeries.add(dataTimeSeries1);
        dataTimeSeries.add(dataTimeSeries2);
        dataTimeSeries.add(dataTimeSeries3);

        Collections.sort(dataTimeSeries);
        for (int i = 0; i < dataTimeSeries.size(); i ++) {
            System.out.println(dataTimeSeries.get(i).getTimeLon());

        }

    }
}
