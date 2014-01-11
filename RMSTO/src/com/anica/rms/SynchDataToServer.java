package com.anica.rms;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class SynchDataToServer {

	public static String UpdateOrder(String RoleI, String ProcessID,
			String OrderNum, String ItemNum, String UserID, String HStatus,
			String XmlData) throws Exception

	{

		String responseBody = "Start";
		HttpClient httpclient = new DefaultHttpClient();

		String Url = "http://anica.azurewebsites.net/update.asp";

		try {

			String url = AddToUrl(Url, RoleI, ProcessID, OrderNum, ItemNum,
					UserID, HStatus);

			Log.w("ORDERSYNCH", " URL String" + url);

			HttpPost httpPost = new HttpPost(url);

			Log.w("ORDERSYNCH", " Calling Method to add XML Data");
			// System.out.println("executing request " + httpPost.getURI());

			httpPost.setEntity(new StringEntity(XmlData));

			// Create a response handler

			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			Log.w("ORDERSYNCH", " Calling HTTP Request");

			responseBody = httpclient.execute(httpPost, responseHandler);

			Log.w("ORDERSYNCH", "HTTP Call response" + responseBody);

			// System.out.println("----------------------------------------");

			// System.out.println(responseBody);

			// System.out.println("----------------------------------------");

		} finally {

			// When HttpClient instance is no longer needed,

			// shut down the connection manager to ensure

			// immediate deallocation of all system resources

			httpclient.getConnectionManager().shutdown();

		}

		return responseBody;

	}

	static protected String AddToUrl(String url, String RoleI2,
			String ProcessID2, String OrderNum2, String ItemNum2,
			String UserID2, String Status2) {

		if (!url.endsWith("?"))

			url += "?";

		List<NameValuePair> params = new LinkedList<NameValuePair>();

		params.add(new BasicNameValuePair("RoleID", RoleI2));

		params.add(new BasicNameValuePair("ProcID", ProcessID2));

		params.add(new BasicNameValuePair("DocID", OrderNum2));

		params.add(new BasicNameValuePair("UserID", UserID2));

		params.add(new BasicNameValuePair("Status", Status2));
		params.add(new BasicNameValuePair("ItemID", ItemNum2));

		params.add(new BasicNameValuePair("DVID", LogIn.DeviceID));

		String paramString = URLEncodedUtils.format(params, "utf-8");

		url += paramString;

		return url;

	}

}
