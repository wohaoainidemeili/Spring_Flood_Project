package yuan.flood.until;

import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

/**
 * feature结构数据处理
 */
public class FeatureUtil {
    final static String SP = ";";
    final static String SSP = ":";
    /**
     * 从String转化为Map
     * @param featureStr
     * @return
     */
    public static Map<String, String> getFeatureFromString(String featureStr) {
        Map<String, String> featureMap = new HashMap<>();
        if (Strings.isNullOrEmpty(featureStr)) return  null;
        String[] featureSPStr = featureStr.split(SP);
        for (int i=0;i<featureSPStr.length;i++) {
            String[] featureSSPStr = featureSPStr[i].split(SSP);
            //如果长度不为2则，消除该记录，不存储
            if (featureSSPStr.length!=2) continue;
            featureMap.put(featureSSPStr[0], featureSSPStr[1]);
        }
        return featureMap;
    }

    /**
     * 从Map转为String
     * @param featureMap
     * @return
     */
    public static String getStringFromFeature(Map<String,String> featureMap) {
        if (featureMap==null||featureMap.isEmpty()) return "";
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry entry : featureMap.entrySet()) {
            stringBuffer.append(entry.getKey());
            stringBuffer.append(SSP);
            stringBuffer.append(entry.getValue());
            stringBuffer.append(SP);
        }
        return stringBuffer.toString();
    }

}
