package yuan.flood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.AlertFloodResult;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.service.IService.IEventService;
import yuan.flood.service.IService.IPreparePhaseServiceT;
import yuan.flood.service.function.SendMail;

import javax.mail.MessagingException;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
@Service("emailTestService")
public class EamilTestService implements IPreparePhaseServiceT {
    @Autowired
    IEventService eventService;
    @Override
    public void executeService(String sesID, Date date) {
        Date date1 = new Date();
        SubscibeEventParams subscibeEventParams = eventService.getRegisteredEventParamsBySesid(sesID);
        //生成预警消息内容，并将预警消息存储到数据库中
        AlertFloodResult alertFloodResult = new AlertFloodResult();
        alertFloodResult.setTime(date);
        alertFloodResult.setSubscibeEventParams(subscibeEventParams);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String dateStr = simpleDateFormat.format(date);
        String date1Str = simpleDateFormat.format(date1);
        try {
            //create message
            StringBuffer message = new StringBuffer();
            message.append("您订阅的事件\"" + subscibeEventParams.getUserDefineName() + "\"于" + dateStr + "进入准备阶段。\r\n");
            message.append("该事件的" + subscibeEventParams.getPrepareSensor() + "传感器观测值ֵ");
            message.append("在" + subscibeEventParams.getPrepareDay() + "天");
            message.append(subscibeEventParams.getPrepareHour() + "时");
            message.append(subscibeEventParams.getPrepareMinute() + "分");
            message.append(subscibeEventParams.getPrepareSecond() + "秒");
            message.append("内大于" + subscibeEventParams.getPrepareThreshold() + "m");
            message.append("出现次数达到或超过" + subscibeEventParams.getPrepareRepeatTimes() + "次。\r\n");
            alertFloodResult.setMessage(message.toString());
            alertFloodResult.setSubject(subscibeEventParams.getUserDefineName() + "事件进入准备阶段");
            //email发送消息内容
            SendMail.send("wenying3413ying@126.com", "dwytam1314", subscibeEventParams.getEmail(), subscibeEventParams.getUserDefineName() + "事件进入准备阶段", message.toString());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        Date date2 = new Date();
        System.out.println("测试邮件构建时间" + (date2.getTime() - date1.getTime())+ ",当前邮件发送时间为" + date1Str);

    }
}
