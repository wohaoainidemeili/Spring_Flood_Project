package yuan.flood.mvc;

import net.opengis.eml.x001.ComplexPatternType;
import net.opengis.eml.x001.EMLDocument;
import net.opengis.eml.x001.SimplePatternType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;
import org.n52.oxf.ses.adapter.ISESRequestBuilder;
import org.n52.oxf.ses.adapter.SESUtils;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import org.w3.x2003.x05.soapEnvelope.Header;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Yuan on 2017/2/14.
 */
public class XMLBeansTests {

    public static void main(String[] args){



        /** The ns_addressing. */
       final String ns_addressing = "http://www.w3.org/2005/08/addressing";

        /** The ns_ses. */
         final String ns_ses = "http://www.opengis.net/ses/0.0";

        /** The ns_ws notification. */
         final String ns_wsNotification = "http://docs.oasis-open.org/wsn/b-2";

        /** The ns_ws brokered notification. */
         final String ns_wsBrokeredNotification = "http://docs.oasis-open.org/wsn/br-2";
        String request=null;

        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        String sesURL = "sdsad";

        XmlCursor cur = null;
        SESUtils.addNamespacesToEnvelope_000(env);
        cur = header.newCursor();
        cur.toFirstContentToken();
        cur.addToSelection();
        cur.insertElementWithText(new QName(ns_addressing, "Action", "wsa"), "http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/SubscribeRequest");
     cur.addToSelection();
     cur.insertElementWithText(new QName(ns_addressing,"To","wsa"),sesURL );

        cur.beginElement(new QName(ns_addressing,"From","wsa"));
        cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),"http://www.w3.org/2005/08/addressing/role/anonymous");
        cur.toNextToken();
        cur.insertElementWithText(new QName(ns_addressing,"MessageID","wsa"), UUID.randomUUID().toString());
        cur.dispose();

        cur = body.newCursor();
        cur.toFirstContentToken();
        cur.beginElement(new QName(ns_wsNotification,"Subscribe","wsn-b"));
     cur.addToSelection();
        cur.beginElement(new QName(ns_wsNotification,"ConsumerReference","wsn-b"));
        cur.insertElementWithText(new QName(ns_addressing, "Address","wsa"),"http://localhost:8092/");
        cur.toSelection(0);
        cur.toEndToken();

        cur.beginElement(new QName(ns_addressing,"Filter","wsn-b"));
        cur.beginElement(new QName(ns_addressing,"MessageContent","wsn-b"));
        cur.insertAttributeWithValue("Dialect","http://www.opengis.net/ses/filter/level3");
        cur.insertChars("@MSG_REPLACER@");
     cur.beginElement(new QName(ns_addressing,"Filter","wsn-b"));
        cur.dispose();


        request = envDoc.xmlText();
        int x=0;

    }
}
