import yuan.flood.service.function.SendMail;

import javax.mail.MessagingException;

public class SendEmialTest {
    public static void main(String[] args) {
        String message = "\"您订阅的事件\"新汉川洪涝探测\"于2018-04-12T13:29:18.351+0800进入准备阶段。\n" +
                "该事件的urn:liesmars:insitusensor:hydrological:hanchuan传感器观测值ֵ在0天0时1分0秒内大于28.0m出现次数达到或超过1次。\n" +
                "该水位站点预测水位未来两天结果为28.417583317814675,28.42538559457838;\"";
        String message2 = "\"您订阅的事件'新汉川洪涝探测'于2018-04-12T13:33:18.355+0800进入响应阶段。\n" +
                "该事件的urn:liesmars:insitusensor:hydrological:hanchuan传感器观测值ֵ在0天0时1分0秒内大于29.0m出现次数达到或超过1次。\n" +
                "该水位站点预测水位未来两天结果为29.17193825213993,29.033425141532618;\"";
        try {
            SendMail.send("wenying3413ying@126.com", "dwytam1314", "yuansaii@qq.com", "新汉川洪涝探测事件进入准备阶段",message);
            SendMail.send("wenying3413ying@126.com", "dwytam1314", "yuansaii@qq.com", "新汉川洪涝探测事件进入响应阶段",message2);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
