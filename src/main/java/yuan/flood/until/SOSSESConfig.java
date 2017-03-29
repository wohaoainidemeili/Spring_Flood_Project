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

    static String sosurl;
    static String sesurl;
    static String sesbasicporttypepath;
    static String sesversion;
    static String sesdefaulttopicdialect;
    static String sesdefaulttopic;
    static String sesregisterpublisherlifetime;
    static String sesregisterpublisherendpoint;
    public SOSSESConfig(Properties properties){
        setSosurl(properties.getProperty(sosUrl));
        setSesurl(properties.getProperty(sesUrl));
        setSesbasicporttypepath(properties.getProperty(sesBasicPortTypePath));
        setSesversion(properties.getProperty(sesVersion));
        setSesdefaulttopicdialect(properties.getProperty(sesDefaultTopicDialect));
        setSesdefaulttopic(properties.getProperty(sesDefaultTopic));
        setSesregisterpublisherlifetime(properties.getProperty(sesRegisterPublisherLifeTime));
        setSesregisterpublisherendpoint(properties.getProperty(sesRegisterPublisherEndpoint));
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
}
