package yuan.flood.until;

import java.util.Properties;

/**
 * Created by Yuan on 2017/1/16.
 */
public class SOSSESConfig {
    String sosUrl="sos_url";
    String sesUrl="ses_url";
    String sesBasicPortTypePath="ses_basic_port_type_path";
    String sesVersion="ses_version";
    String sesDefaultTopicDialect="ses_default_topic_dialect";
    String sesDefaultTopic="ses_default_topic";
    String sesRegisterPublisherLifeTime="ses_register_publisher_lifetime";
    String sesRegisterPublisherEndpoint="ses_register_publisher_endpoint";
    String diagnosisServiceClass = "diagnosisService";
    String prepareServiceClass = "prepareService";
    String responseServiceClass = "responseService";
    String recoveryServiceClass = "recoveryService";


    static String sosurl;
    static String sesurl;
    static String sesbasicporttypepath;
    static String sesversion;
    static String sesdefaulttopicdialect;
    static String sesdefaulttopic;
    static String sesregisterpublisherlifetime;
    static String sesregisterpublisherendpoint;
    static String diagnosisserviceclass;
    static String prepareserviceclass;
    static String responseserviceclass;
    static String recoveryserviceclass;

    public SOSSESConfig(Properties properties){
        setSosurl(properties.getProperty(sosUrl));
        setSesurl(properties.getProperty(sesUrl));
        setSesbasicporttypepath(properties.getProperty(sesBasicPortTypePath));
        setSesversion(properties.getProperty(sesVersion));
        setSesdefaulttopicdialect(properties.getProperty(sesDefaultTopicDialect));
        setSesdefaulttopic(properties.getProperty(sesDefaultTopic));
        setSesregisterpublisherlifetime(properties.getProperty(sesRegisterPublisherLifeTime));
        setSesregisterpublisherendpoint(properties.getProperty(sesRegisterPublisherEndpoint));

        setDiagnosisserviceclass(properties.getProperty(diagnosisServiceClass));
        setPrepareserviceclass(properties.getProperty(prepareServiceClass));
        setResponseserviceclass(properties.getProperty(responseServiceClass));
        setRecoveryserviceclass(properties.getProperty(recoveryServiceClass));

    }

    public static String getSosurl() {
        return sosurl;
    }

    public static void setSosurl(String sosurl) {
        SOSSESConfig.sosurl = sosurl;
    }

    public static String getSesurl() {
        return sesurl;
    }

    public static void setSesurl(String sesurl) {
        SOSSESConfig.sesurl = sesurl;
    }

    public static String getSesbasicporttypepath() {
        return sesbasicporttypepath;
    }

    public static void setSesbasicporttypepath(String sesbasicporttypepath) {
        SOSSESConfig.sesbasicporttypepath = sesbasicporttypepath;
    }

    public static String getSesversion() {
        return sesversion;
    }

    public static void setSesversion(String sesversion) {
        SOSSESConfig.sesversion = sesversion;
    }

    public static String getSesdefaulttopicdialect() {
        return sesdefaulttopicdialect;
    }

    public static void setSesdefaulttopicdialect(String sesdefaulttopicdialect) {
        SOSSESConfig.sesdefaulttopicdialect = sesdefaulttopicdialect;
    }

    public static String getSesdefaulttopic() {
        return sesdefaulttopic;
    }

    public static void setSesdefaulttopic(String sesdefaulttopic) {
        SOSSESConfig.sesdefaulttopic = sesdefaulttopic;
    }

    public static String getSesregisterpublisherlifetime() {
        return sesregisterpublisherlifetime;
    }

    public static void setSesregisterpublisherlifetime(String sesregisterpublisherlifetime) {
        SOSSESConfig.sesregisterpublisherlifetime = sesregisterpublisherlifetime;
    }

    public static String getSesregisterpublisherendpoint() {
        return sesregisterpublisherendpoint;
    }

    public static void setSesregisterpublisherendpoint(String sesregisterpublisherendpoint) {
        SOSSESConfig.sesregisterpublisherendpoint = sesregisterpublisherendpoint;
    }

    public static String getDiagnosisserviceclass() {
        return diagnosisserviceclass;
    }

    public static void setDiagnosisserviceclass(String diagnosisserviceclass) {
        SOSSESConfig.diagnosisserviceclass = diagnosisserviceclass;
    }

    public static String getPrepareserviceclass() {
        return prepareserviceclass;
    }

    public static void setPrepareserviceclass(String prepareserviceclass) {
        SOSSESConfig.prepareserviceclass = prepareserviceclass;
    }

    public static String getResponseserviceclass() {
        return responseserviceclass;
    }

    public static void setResponseserviceclass(String responseserviceclass) {
        SOSSESConfig.responseserviceclass = responseserviceclass;
    }

    public static String getRecoveryserviceclass() {
        return recoveryserviceclass;
    }

    public static void setRecoveryserviceclass(String recoveryserviceclass) {
        SOSSESConfig.recoveryserviceclass = recoveryserviceclass;
    }
}
