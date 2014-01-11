package com.anica.rms;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class XmlPullSQuestionParser {

	private XmlPullParser xpp;

	public static String parseQuestion(InputStream xml, String TagNameO)
			throws Exception {
		return new XmlPullSQuestionParser().parse(xml, TagNameO);
	}

	public String parse(InputStream xml, String TagName) throws Exception {

		String Question;
		int QAttributeValue;
		String AtQuestion;
		xpp = XmlPullParserFactory.newInstance().newPullParser();
		xpp.setInput(xml, "UTF-8");

		// skipToTag("QUESTION");
		skipToTag(TagName);
		Question = xpp.nextText();
		QAttributeValue = xpp.getAttributeCount();

		AtQuestion = QAttributeValue + "," + Question;

		return Question;
	}

	private void skipToTag(String tagName) throws Exception {
		int event = xpp.getEventType();
		while (event != XmlPullParser.END_DOCUMENT
				&& !tagName.equals(xpp.getName())) {
			event = xpp.next();
		}
	}

}
