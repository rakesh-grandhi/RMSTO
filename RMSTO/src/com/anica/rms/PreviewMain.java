package com.anica.rms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

public class PreviewMain extends View {
	// used to determine whether user moved a finger enough to draw again
	private static final float TOUCH_TOLERANCE = 10;

	private Bitmap bitmap; // drawing area for display or saving
	private Canvas bitmapCanvas; // used to draw on bitmap
	private Paint paintScreen; // use to draw bitmap onto screen
	private Paint paintLine; // used to draw lines onto bitmap
	private HashMap<Integer, Path> pathMap; // current Paths being drawn
	private HashMap<Integer, Point> previousPointMap; // current Points

	String Filepath;
	Bitmap SourceBitmap;

	// DoodleView constructor initializes the DoodleView
	public PreviewMain(Context context, AttributeSet attrs) {
		super(context, attrs); // pass context to View's constructor

		paintScreen = new Paint(); // used to display bitmap onto screen

		// set the initial display settings for the painted line
		paintLine = new Paint();
		paintLine.setAntiAlias(true); // smooth edges of drawn line
		paintLine.setColor(Color.BLACK); // default color is black
		paintLine.setStyle(Paint.Style.STROKE); // solid line
		paintLine.setStrokeWidth(5); // set the default line width
		paintLine.setStrokeCap(Paint.Cap.ROUND); // rounded line ends
		pathMap = new HashMap<Integer, Path>();
		previousPointMap = new HashMap<Integer, Point>();

	} // end DoodleView constructor

	// Method onSizeChanged creates BitMap and Canvas after app displays
	@Override
	public void onSizeChanged(int w, int h, int oldW, int oldH) {

		// bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
		// Bitmap.Config.ARGB_8888);
		Log.w("CP10APP", Filepath);
		// SourceBitmap = BitmapFactory.decodeFile(Filepath);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		SourceBitmap = BitmapFactory.decodeFile(Filepath, options);

		bitmap = Bitmap.createScaledBitmap(SourceBitmap, getWidth(),
				getHeight(), false);

		// eend of code addition.
		bitmapCanvas = new Canvas(bitmap);
		// bitmap.eraseColor(Color.WHITE); // erase the BitMap with white
	} // end method onSizeChanged

	// clear the painting
	public void clear() {
		pathMap.clear(); // remove all paths
		previousPointMap.clear(); // remove all previous points
		bitmap.eraseColor(Color.WHITE); // clear the bitmap
		invalidate(); // refresh the screen
	} // end method clear

	// set the painted line's color
	public void setDrawingColor(int color) {
		paintLine.setColor(color);
	} // end method setDrawingColor

	// return the painted line's color
	public int getDrawingColor() {
		return paintLine.getColor();
	} // end method getDrawingColor

	// set the painted line's width
	public void setLineWidth(int width) {
		paintLine.setStrokeWidth(width);
	} // end method setLineWidth

	// return the painted line's width
	public int getLineWidth() {
		return (int) paintLine.getStrokeWidth();
	} // end method getLineWidth

	// called each time this View is drawn

	// set the Image File Name
	public void setFileName(String FilepathO) {
		Filepath = FilepathO;
	} // end method set File Name

	@Override
	protected void onDraw(Canvas canvas) {
		// draw the background screen
		canvas.drawBitmap(bitmap, 0, 0, paintScreen);

		// for each path currently being drawn
		for (Integer key : pathMap.keySet())
			canvas.drawPath(pathMap.get(key), paintLine); // draw line
	} // end method onDraw

	// handle touch event
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// get the event type and the ID of the pointer that caused the event
		int action = event.getActionMasked(); // event type
		int actionIndex = event.getActionIndex(); // pointer (i.e., finger)

		// determine which type of action the given MotionEvent
		// represents, then call the corresponding handling method
		if (action == MotionEvent.ACTION_DOWN
				|| action == MotionEvent.ACTION_POINTER_DOWN) {
			touchStarted(event.getX(actionIndex), event.getY(actionIndex),
					event.getPointerId(actionIndex));
		} // end if
		else if (action == MotionEvent.ACTION_UP
				|| action == MotionEvent.ACTION_POINTER_UP) {
			touchEnded(event.getPointerId(actionIndex));
		} // end else if
		else {
			touchMoved(event);
		} // end else

		invalidate(); // redraw
		return true; // consume the touch event
	} // end method onTouchEvent

	// called when the user touches the screen
	private void touchStarted(float x, float y, int lineID) {
		Path path; // used to store the path for the given touch id
		Point point; // used to store the last point in path

		// if there is already a path for lineID
		if (pathMap.containsKey(lineID)) {
			path = pathMap.get(lineID); // get the Path
			path.reset(); // reset the Path because a new touch has started
			point = previousPointMap.get(lineID); // get Path's last point
		} // end if
		else {
			path = new Path(); // create a new Path
			pathMap.put(lineID, path); // add the Path to Map
			point = new Point(); // create a new Point
			previousPointMap.put(lineID, point); // add the Point to the Map
		} // end else

		// move to the coordinates of the touch
		path.moveTo(x, y);
		point.x = (int) x;
		point.y = (int) y;
	} // end method touchStarted

	// called when the user drags along the screen
	private void touchMoved(MotionEvent event) {
		// for each of the pointers in the given MotionEvent
		for (int i = 0; i < event.getPointerCount(); i++) {
			// get the pointer ID and pointer index
			int pointerID = event.getPointerId(i);
			int pointerIndex = event.findPointerIndex(pointerID);

			// if there is a path associated with the pointer
			if (pathMap.containsKey(pointerID)) {
				// get the new coordinates for the pointer
				float newX = event.getX(pointerIndex);
				float newY = event.getY(pointerIndex);

				// get the Path and previous Point associated with
				// this pointer
				Path path = pathMap.get(pointerID);
				Point point = previousPointMap.get(pointerID);

				// calculate how far the user moved from the last update
				float deltaX = Math.abs(newX - point.x);
				float deltaY = Math.abs(newY - point.y);

				// if the distance is significant enough to matter
				if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) {
					// move the path to the new location
					path.quadTo(point.x, point.y, (newX + point.x) / 2,
							(newY + point.y) / 2);

					// store the new coordinates
					point.x = (int) newX;
					point.y = (int) newY;
				} // end if
			} // end if
		} // end for
	} // end method touchMoved

	// called when the user finishes a touch
	private void touchEnded(int lineID) {
		Path path = pathMap.get(lineID); // get the corresponding Path
		bitmapCanvas.drawPath(path, paintLine); // draw to bitmapCanvas
		path.reset(); // reset the Path
	} // end method touch_ended

	// save the current image to the Gallery
	public boolean saveImage() {

		/*
		 * // use "Doodlz" followed by current time as the image file name
		 * String fileName = "Doodlz" + System.currentTimeMillis();
		 * 
		 * // create a ContentValues and configure new image's data
		 * ContentValues values = new ContentValues();
		 * values.put(Images.Media.TITLE, fileName);
		 * values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
		 * values.put(Images.Media.MIME_TYPE, "image/jpg");
		 * 
		 * // get a Uri for the location to save the file Uri uri =
		 * getContext().getContentResolver().insert(
		 * Images.Media.EXTERNAL_CONTENT_URI, values);
		 */
		File mediaFile;
		mediaFile = new File(Filepath);

		Uri uri = Uri.fromFile(mediaFile);

		try {
			// get an OutputStream to uri
			OutputStream outStream = getContext().getContentResolver()
					.openOutputStream(uri);

			// copy the bitmap to the OutputStream
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

			// flush and close the OutputStream
			outStream.flush(); // empty the buffer
			outStream.close(); // close the stream

			// display a message indicating that the image was saved
			Toast message = Toast.makeText(getContext(),
					R.string.message_saved, Toast.LENGTH_SHORT);
			message.setGravity(Gravity.CENTER, message.getXOffset() / 2,
					message.getYOffset() / 2);
			message.show(); // display the Toast

			return true;

			// dbHelper.addPicture(ordernum, itemnum, SeqNum, ObjNum, CreatDate,
			// RecStatus, Remarks, Location, User, Filepath);
		} // end try
		catch (IOException ex) {
			// display a message indicating that the image was saved
			Toast message = Toast.makeText(getContext(),
					R.string.message_error_saving, Toast.LENGTH_SHORT);
			message.setGravity(Gravity.CENTER, message.getXOffset() / 2,
					message.getYOffset() / 2);
			message.show(); // display the Toast

			return false;
		} // end catch
	} // end method saveImage

	public String UploadImage() {
		saveImage();
		String Result = null;

		UploadPicTask uploadpicktask = new UploadPicTask(Filepath);
		uploadpicktask.execute();

		return Result;
	}

	private class UploadPicTask extends AsyncTask<Void, Void, Void> {

		Bitmap bm;

		String filepath = null;

		public UploadPicTask(String ImageFileO) {
			filepath = ImageFileO;
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.i("CP10APP", "doInBackground");

			try {

				Log.w("CP10APP", "File saved  " + filepath);
				File pictureFile = new File(filepath);

				long filesize2 = pictureFile.length();
				FileInputStream purge = new FileInputStream(pictureFile);

				int filesize3 = (int) filesize2;

				byte[] buffer = new byte[filesize3];

				purge.read(buffer);

				purge.close();

				String filename = pictureFile.getName();

				int file_size = buffer.length;

				Log.w("CP10APP", "File size: " + file_size);

				executeMultipartPost(buffer, filename);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				Log.w("CP10APP", "File read failed ");
			}
			return null;
		}

		public void executeMultipartPost(byte[] data, String fileName)
				throws Exception {

			try {

				HttpClient httpClient = new DefaultHttpClient();

				HttpPost postRequest = new HttpPost(
						"http://anica.azurewebsites.net/MRSave.asp");

				ByteArrayBody bab = new ByteArrayBody(data, fileName);

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				reqEntity.addPart("uploaded", bab);
				// reqEntity.addPart("photoCaption", new
				// StringBody("MeterRead"));

				reqEntity.addPart("CUST", new StringBody("1"));

				postRequest.setEntity(reqEntity);
				Log.w("CP10APP", "calling HTTP Request");

				HttpResponse response = httpClient.execute(postRequest);

				Log.w("CP10APP", "Called HTTP Request");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(

						response.getEntity().getContent(), "UTF-8"));

				String sResponse;

				StringBuilder s = new StringBuilder();

				while ((sResponse = reader.readLine()) != null) {

					s = s.append(sResponse);

					Log.w("CPA10APP", s.toString());

					// System.out.println("Response: " + s);

				}

			} catch (Exception e) {

				// handle exception here
				Log.e("CP10APP", " HTTP Request failed");

				Log.e("CP10APP", e.getMessage());

			}

		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i("CP10APP", "onPostExecute");

			// If Required we can delete the file here

		}

		@Override
		protected void onPreExecute() {
			Log.i("CP10APP", "onPreExecute");
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.i("CP10APP", "onProgressUpdate");
		}

	}
} // end class DoodleView

