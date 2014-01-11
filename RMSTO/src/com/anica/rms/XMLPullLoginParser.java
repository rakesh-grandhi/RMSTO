package com.anica.rms;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class XMLPullLoginParser {

	String status = "";

	private XmlPullParser xpp;

	public static LoginObj parseOrder(InputStream xml) throws Exception {
		return new XMLPullLoginParser().parse(xml);
	}

	public LoginObj parse(InputStream xml) throws Exception {
		LoginObj loginObj = new LoginObj();
		// Log.w("XMLORDERPR","Started pharsing");
		xpp = XmlPullParserFactory.newInstance().newPullParser();
		// Log.w("XMLORDERPR","created Xpp object");
		xpp.setInput(xml, "UTF-8");

		// Log.w("XMLORDERPR","added data stream");

		skipToTag("STATUS");

		status = xpp.nextText();
		// Log.w("XMLORDERPR",matcode);
		loginObj.setStatus(status);
		// Log.w("XMLORDERPR","Assigned MAt code to order");

		skipToTag("Role");
		loginObj.setRole(xpp.nextText());

		return loginObj;
	}

	// Log.w("XMLORDERPR","Assigned basic start to order");

	private void skipToTag(String tagName) throws Exception {
		int event = xpp.getEventType();
		while (event != XmlPullParser.END_DOCUMENT
				&& !tagName.equals(xpp.getName())) {
			event = xpp.next();
		}
	}

}
