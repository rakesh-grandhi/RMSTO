package com.anica.rms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.widget.Toast;

public class XMLFill {

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int MEDIA_TYPE_DATAIN = 3;
	public static final int MEDIA_TYPE_DATAOUT = 4;
	private static String file_name;

	/**
	 * Getting XML DOM element
	 * 
	 * @param XML
	 *            string
	 * */
	public Document getDomElement(String xml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		}

		return doc;
	}

	/**
	 * Getting node value
	 * 
	 * @param elem
	 *            element
	 */
	public final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	/**
	 * Getting node value
	 * 
	 * @param Element
	 *            node
	 * @param key
	 *            string
	 * */
	public String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));
	}

	public void setValue(Element elem, String str) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						child.setNodeValue(str);
					}
				}
			}
		}
	}

	public String GetElementAttribute(Element item, String attribName) {
		return item.getAttribute(attribName);
	}

	public String SerializeXML(Document doc) {

		// create Transformer object

		Transformer transformer = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		try {
			transformer.transform(new DOMSource(doc), result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return XML string
		return writer.toString();
	}

	public void writeXMLToFile(Context context, String xmlFile, String xmlData) {

		FileOutputStream fOut = null;

		OutputStreamWriter osw = null;

		try {

			// File sdCard = Environment.getExternalStorageDirectory();
			// File dir = new File (sdCard.getAbsolutePath() + "/CP10/ORDERS");
			File dir = getOutputMediaFile(3);

			if (!dir.exists()) {
				if (!(dir.mkdirs()))
					Log.e("mkdirs",
							"Failed to create SDCARD mounted directory!!!");
			}

			fOut = new FileOutputStream(new File(dir, xmlFile));

			// fOut = new FileOutputStream(dir);

			osw = new OutputStreamWriter(fOut);

			osw.write(xmlData);

			osw.flush();

			// Toast.makeText(context,
			// "Settings saved",Toast.LENGTH_SHORT).show();

			Log.w("FILEWRITE", "Saved Updated XML file at" + dir.getPath());

		}

		catch (Exception e) {
			e.printStackTrace();
			// Toast.makeText(context,
			// "Settings not saved",Toast.LENGTH_SHORT).show();
			Log.w("FILEWRITE", "File Save Failed");
		}

		finally {
			try {
				osw.close();
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Getting XML from URL making HTTP request
	 * 
	 * @param url
	 *            string
	 * */
	public String getXmlFromUrl(String url) {
		String xml = null;

		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			xml = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return XML
		return xml;
	}

	public String readXMLFromFile(Context activity, String xmlFile,
			boolean useConfigDir) {
		InputStream is = null;
		File file = null;
		File sdCard = null;
		Writer writer = new StringWriter();
		boolean exists = false;

		if (useConfigDir) {
			// sdCard = Environment.getExternalStorageDirectory();
			// file = new File (sdCard.getAbsolutePath() + "/kjv/config/" +
			// xmlFile);
			// file = getOutputMediaFile(3);
			File mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"CP10APP");

			file = new File(mediaStorageDir.getPath() + File.separator
					+ xmlFile + ".xml");

			if (!(file == null)) {
				if (exists = file.exists())
					try {
						is = new FileInputStream(file);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}

		if (!exists) {
			AssetManager assetManager = activity.getAssets();
			try {
				is = assetManager.open(xmlFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (is != null) {

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
				is.close();
			} catch (IOException e) {
				Log.e("Error: ", e.getMessage());
				return null;
			}
		}

		return writer.toString();

	}

	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"CP10APP");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.
		// Create the storage directory if it does not exist

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs())

			{
				Log.d("CP10APP", "failed to create directory");

				return null;

			}

		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {

			file_name = "IMG_" + timeStamp + ".jpg";

			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");

			String filepath = mediaFile.getAbsolutePath();
			Log.w("CP10APP", filepath);
		} else if (type == MEDIA_TYPE_VIDEO) {

			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		}

		else if (type == MEDIA_TYPE_DATAOUT) {

			// file_name = "CP10_"+ timeStamp + ".xml";

			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "CP10OUT_" + timeStamp + ".xml");

			String filepath = mediaFile.getAbsolutePath();
			Log.w("CP10APP", filepath);
		}

		else if (type == MEDIA_TYPE_DATAIN) {

			// file_name = "CP10_"+ timeStamp + ".xml";

			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "CP10IN_" + timeStamp + ".xml");

			String filepath = mediaFile.getAbsolutePath();
			Log.w("CP10APP", filepath);
		}

		else {
			return null;
		}

		return mediaFile;
	} // end of getOutputMediaFile

	public static boolean syncStream(FileOutputStream fos) {

		if (fos != null) {
			try {
				fos.getFD().sync();

			} catch (IOException e) {
				Log.e("FILEWRITE", "error Syncing fos" + e.getMessage(), e);
			}
			return true;
		}
		return true;

	} // end SyncStream

} // end of class

