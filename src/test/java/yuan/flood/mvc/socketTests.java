package yuan.flood.mvc;

import yuan.flood.service.DecodeWNSEventService;

/**
 * Created by Yuan on 2017/2/19.
 */
public class socketTests {
    public static void main(String[] args){
        DecodeWNSEventService decodeWNSEventService =new DecodeWNSEventService();
        decodeWNSEventService.saveEventFromWNS();
    }
}
