package yuan.flood.phase;

import com.google.common.base.Strings;
import yuan.flood.until.SOSSESConfig;

public class PhaseFactory {
    public final static String DIAGNOSIS_MESSAGE = "diagnosis";
    public final static String PREPARE_MESSAGE = "prepare";
    public final static String RESPONSE_MESSAGE = "response";
    public final static String RECOVERY_MESSAGE = "recovery";

   public static IPhaseService createPhaseService(String messageType){
       AbastractPhaseService phaseService = null;
       String classPath;
       switch (messageType) {
           case DIAGNOSIS_MESSAGE:
               classPath = SOSSESConfig.getDiagnosisserviceclass();
               break;
           case PREPARE_MESSAGE:
               classPath = SOSSESConfig.getPrepareserviceclass();
               break;
           case RESPONSE_MESSAGE:
               classPath = SOSSESConfig.getResponseserviceclass();
               break;
           case RECOVERY_MESSAGE:
               classPath = SOSSESConfig.getRecoveryserviceclass();
               break;
               default:
                   classPath = "";
                   break;

       }
       if (Strings.isNullOrEmpty(classPath)) {
           try {
               throw new Exception("无法加载服务项!");
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       Class currentClass = null;
       try {
           currentClass = Class.forName(classPath);
            phaseService = (AbastractPhaseService) currentClass.newInstance();
            //设置服务初始化，得到所有的服务内容
       } catch (InstantiationException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       }catch (ClassNotFoundException e) {
           e.printStackTrace();
       }
       return phaseService;
   }
}
