package yuan.flood.until;

import com.google.common.base.Strings;

import java.util.*;

/**
 * feature结构数据处理
 */
public class FeatureUtil {
    final static String SP = ";";
    final static String SSP = ":";
    final static String SSSP = ",";
    /**
     * 从String转化为Map
     * @param featureStr
     * @return
     */
    public static Map<String, String> getFeatureFromString(String featureStr) {
        Map<String, String> featureMap = new HashMap<>();
        if (Strings.isNullOrEmpty(featureStr)) return  featureMap;
        String[] featureSPStr = featureStr.split(SP);
        for (int i=0;i<featureSPStr.length;i++) {
            String trimedStr = featureSPStr[i].trim();
            if (!Strings.isNullOrEmpty(trimedStr)) {
                String[] featureSSPStr = featureSPStr[i].split(SSP);
                //如果长度不为2则，消除该记录，不存储
                if (featureSSPStr.length != 2) continue;
                featureMap.put(featureSSPStr[0], featureSSPStr[1]);
            }
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

    public static List<String> getListFromString(String listStr) {
        if (Strings.isNullOrEmpty(listStr)) {
            return new ArrayList();
        }
        String[] eles = listStr.split(SSSP);
        List<String> strings = new ArrayList<>();
        for (int i=0;i<eles.length;i++) {
            if (Strings.isNullOrEmpty(eles[i].trim())) {
                strings.add(eles[i].trim());
            }
        }
        return strings;
    }

    public static String getStringFromList(List<String> list) {
        StringBuffer stringBuffer = new StringBuffer();
        if (list==null||list.isEmpty()) return stringBuffer.toString();
        for (int i=0;i<list.size()-1;i++) {
            stringBuffer.append(list.get(i));
            stringBuffer.append(SSSP);
        }
        stringBuffer.append(list.get(list.size() - 1));
        return stringBuffer.toString();
    }

}
