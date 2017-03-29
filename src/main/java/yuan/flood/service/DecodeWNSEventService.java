package yuan.flood.service;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import yuan.flood.dao.Entity.DetectedEvent;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.IDao.IDetectedEventDao;
import yuan.flood.dao.IDao.IEventDao;
import yuan.flood.service.IService.IDecodeWNSEventService;
import yuan.flood.service.IService.ISubScribeEventService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Yuan on 2017/2/17.
 */
@Service
public class DecodeWNSEventService implements IDecodeWNSEventService{
    @Autowired
    private IEventDao eventDao;
    @Autowired
    private IDetectedEventDao detectedEventDao;
    public void saveEventFromWNS(){
        try {
            ServerSocket serverSocket=new ServerSocket(8092);
            while (true) {

                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String receiveStrLine;
                StringBuffer resultXML=new StringBuffer();
                boolean isStartAppend=false;
                while ((receiveStrLine = bufferedReader.readLine()) != null) {
                    if (receiveStrLine.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"))
                        isStartAppend=true;
                    if (isStartAppend)
                         resultXML.append(receiveStrLine);
                }
                socket.close();
                bufferedReader.close();
                DetectedEvent detectedEvent=new DetectedEvent();
                String result=resultXML.toString();
                EnvelopeDocument envelopeDocument=EnvelopeDocument.Factory.parse(result);
                Envelope envelope= envelopeDocument.getEnvelope();
                Body body= envelope.getBody();
                XmlCursor cursor= body.newCursor();
                cursor.toFirstContentToken();
                cursor.toChild(0);
                cursor.toChild(2);
                cursor.toChild(0);
                //the location of DerivedEvent qname
                cursor.addToSelection();
                cursor.toChild(0);
                cursor.toChild(0);
                cursor.addToSelection();
                cursor.toChild(0);
                String startTime=cursor.getTextValue().replace("+08:00","+0800");
                cursor.toSelection(1);
                cursor.toChild(1);
                String endTime=cursor.getTextValue().replace("+08:00", "+0800");
                // get the long type of time
                cursor.toSelection(0);
                cursor.toChild(1);
                cursor.toChild(0);
                cursor.toChild(1);
                String[] timeLongStr=cursor.getTextValue().split("until");
                Long startLongTime=Long.valueOf(timeLongStr[0]);
                Long endLongTime=Long.valueOf(timeLongStr[1]);
                cursor.toSelection(0);
                cursor.toChild(2);
                cursor.toChild(0);
                cursor.toChild(1);
                String eventMessage=cursor.getTextValue();

                String[] message_IdStr= eventMessage.split(":");

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                String t= simpleDateFormat.format(new Date());
                Date startDate= simpleDateFormat.parse(startTime);
                Date endDate=simpleDateFormat.parse(endTime);
                detectedEvent.setEventTypeName(message_IdStr[1]);
                detectedEvent.setStartTime(startDate);
                detectedEvent.setEndTime(endDate);
                detectedEvent.setStartTimeLong(startLongTime);
                detectedEvent.setEndTimeLong(endLongTime);

                String eventStr="from SubscibeEventParams s where s.eventID='"+message_IdStr[0]+"'";
                List subscibeEventParams=eventDao.find(eventStr);
                detectedEvent.setEvent((SubscibeEventParams) subscibeEventParams.get(0));
//                detectedEvent.setEvent(s);
                detectedEventDao.saveOrUpdate(detectedEvent);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (XmlException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveTest() {
      		DetectedEvent detectedEvent=new DetectedEvent();
		detectedEvent.setStartTimeLong(10L);
		detectedEventDao.saveOrUpdate(detectedEvent);
    }

}
