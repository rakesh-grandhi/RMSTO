package com.anica.rms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

public class WorkRequest extends Activity {
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private final static int TAKE_PHOTO = 1;
	private Uri photoUri;
	private final static String PHOTO_URI = "photoUri";
	private static int step = 1;

	public final static String EXTRA_FILEPATH = "com.anica.cpapp1.FILEPATH";
	public final static String EXTRA_ORDERNUM = "com.anica.cpapp1.ORDERNUM";
	public final static String EXTRA_OPRNUM = "com.anica.cpapp1.OPRNUM";
	public final static String EXTRA_USER = "com.anica.cpapp1.USER";
	public final static String EXTRA_LOC = "com.anica.cpapp1.LOC";
	public final static String EXTRA_EQPNUM = "com.anica.cpapp1.EQPNUM";

	String WorkReqNum, SeqNum;
	String SUpdatedXml;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_request);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		Log.w("CPD10APP", "Request Code" + requestCode);
		Log.w("CPD10APP", "resultCode" + resultCode);

		if (resultCode != Activity.RESULT_OK) {

			Log.w("CPD10APP", "Result not OK");
			return;
		} else {

			if (requestCode == TAKE_PHOTO) {

				step = 2;
				// ImageView img = (ImageView) findViewById(R.id.photoThumb);

				String filepath = photoUri.getPath();
				// CapturePicTask task = new CapturePicTask(filepath);
				// task.execute();

				Intent IPreview = new Intent(this, Preview.class);

				IPreview.putExtra(EXTRA_FILEPATH, photoUri.getPath());

				/*
				 * IPreview.putExtra(EXTRA_ORDERNUM, OrderNum);
				 * IPreview.putExtra(EXTRA_USER, SUserName);
				 * IPreview.putExtra(EXTRA_OPRNUM, SOprNum);
				 * IPreview.putExtra(EXTRA_EQPNUM, S_Equipment);
				 */
				Log.w("CPD10APP", "Picture taken from camera");

				startActivity(IPreview);

				// finish();

				// InputStream stream =
				// getContentResolver().openInputStream(photoUri);
				// Bitmap bmp = BitmapFactory.decodeStream(stream);

			} // Request code 1

		} // end of if else.

	} // End of Onactivityresult

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.work_request, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void TakePicture(View view) {

		Intent Picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// photoUri = getContentResolver().insert(
		// EXTERNAL_CONTENT_URI, new ContentValues());

		photoUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, WorkReqNum, SeqNum); // create
																				// a
																				// file
																				// to
																				// save
																				// the
																				// image

		String filepath3 = photoUri.getPath();
		Log.w("CP10APP", "File path passed to camera application:" + filepath3);
		Picture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		startActivityForResult(Picture, TAKE_PHOTO);

	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type, String ordnum,
			String opernum) {
		return Uri.fromFile(getOutputMediaFile(type, ordnum, opernum));

	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type, String ordnum,
			String opernum) {
		// String file_name;
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"RMS");
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

			// file_name = "IMG_"+ timeStamp + ".jpg";

			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + ordnum + "_" + opernum + "_" + timeStamp
					+ ".jpg");

			String filepath = mediaFile.getAbsolutePath();
			Log.w("RMS", filepath);
		} else if (type == MEDIA_TYPE_VIDEO) {

			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		}

		else {
			return null;
		}

		return mediaFile;
	} // end of getOutputMediaFile

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(PHOTO_URI, photoUri);
	}

	private String GetTodaysdate() {
		String STodayDate = "0000-00-00";

		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		month++;

		String SDay;
		String SMonth;

		if (day < 10)

			SDay = "0" + Integer.toString(day);
		else
			SDay = Integer.toString(day);

		if (month < 10)

			SMonth = "0" + Integer.toString(month);
		else
			SMonth = Integer.toString(month);

		STodayDate = Integer.toString(year) + "-" + SMonth + "-" + SDay;

		return STodayDate;

	}

	public void SubmitWR(View view) {

		String WRXML = CreateWRXML();
		// create a instance of database object and create new Work Request
		// entry in database.

	}

	String CreateWRXML() {

		XMLFill parser;
		parser = new XMLFill();

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("data");
			doc.appendChild(rootElement);

			// staff elements
			Element staff = doc.createElement("WR");
			rootElement.appendChild(staff);

			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue("1");
			staff.setAttributeNode(attr);

			// shorten way
			// staff.setAttribute("id", "1");

			// firstname elements
			Element docid = doc.createElement("DocId");
			docid.appendChild(doc.createTextNode("000013137917"));
			staff.appendChild(docid);

			// lastname elements
			Element roomid = doc.createElement("RoomID");
			roomid.appendChild(doc.createTextNode("12345"));
			staff.appendChild(roomid);

			SUpdatedXml = parser.SerializeXML(doc);
			/*
			 * // nickname elements Element nickname =
			 * doc.createElement("nickname");
			 * nickname.appendChild(doc.createTextNode("mkyong"));
			 * staff.appendChild(nickname);
			 * 
			 * // salary elements Element salary = doc.createElement("salary");
			 * salary.appendChild(doc.createTextNode("100000"));
			 * staff.appendChild(salary);
			 */
		} catch (ParserConfigurationException pce) {
			// tfe.printStackTrace();
		}

		return SUpdatedXml;
	}

}
