package billbook.smart.com.smartbillbook.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Utils_functions {

	public static String profilePicURL;
	private static final String USER_NAME_PATTERN = "^[_A-Za-z0-9-\\+]+$";

	public static boolean eMailValidation(String emailstring) {
		Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher emailMatcher = emailPattern.matcher(emailstring);
		return emailMatcher.matches();
	}

	public static boolean userNameValidation(String username) {

		Pattern emailPattern = Pattern.compile(USER_NAME_PATTERN);
		Matcher emailMatcher = emailPattern.matcher(username);
		return emailMatcher.matches();
	}

	/**
	 * Check Connectivity of network.
	 */
	public static boolean isOnline(Context context) {
		try {
			if (context == null)
				return false;

			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (cm != null) {
				if (cm.getActiveNetworkInfo() != null) {
					return cm.getActiveNetworkInfo().isConnected();
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			Log.e("Exception", e + "");
			return false;
		}

	}

	public static Typeface getFont(Context context, int tag) {
		if (tag == 100) {
			return Typeface.createFromAsset(context.getAssets(),
					"ProductSans-Regular.ttf");
		} else if (tag == 200) {
			return Typeface.createFromAsset(context.getAssets(),
					"ProductSans-Regular.ttf");
		}
		return Typeface.DEFAULT;
	}

	public static boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
	}

	// public static boolean isTablet(Context context) {
	// return (context.getResources().getConfiguration().screenLayout &
	// Configuration.SCREENLAYOUT_SIZE_MASK) >=
	// Configuration.SCREENLAYOUT_SIZE_LARGE;
	// }

	public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = 145;
		int targetHeight = 145;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_4444);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}

	/**
	 * Method to extract the user's age from the entered Date of Birth.
	 * 
	 * //@param DoB
	 *            String The user's date of birth.
	 * 
	 * @return ageS String The user's age in years based on the supplied DoB.
	 */

	/*public static void customAlert(Context context, String message,
			boolean isLargeAlert) {

		TextView txtMessage;
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert);
		dialog.setCancelable(false);

		if (isLargeAlert)
			txtMessage = (TextView) dialog.findViewById(R.id.txtAlertMsgBig);
		else
			txtMessage = (TextView) dialog.findViewById(R.id.txtAlertMsg);

		txtMessage.setVisibility(View.VISIBLE);
		txtMessage.setText(message);

		LinearLayout layoutOk = (LinearLayout) dialog
				.findViewById(R.id.llAlertSingle);

		layoutOk.setVisibility(View.VISIBLE);
		layoutOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.show();
	}

	public static void customAlert(Context context, final String message,
			boolean isLargeAlert, final ResponseListener responseListener) {

		TextView txtMessage;
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert);
		dialog.setCancelable(false);

		if (isLargeAlert)
			txtMessage = (TextView) dialog.findViewById(R.id.txtAlertMsgBig);
		else
			txtMessage = (TextView) dialog.findViewById(R.id.txtAlertMsg);

		txtMessage.setVisibility(View.VISIBLE);
		txtMessage.setText(message);

		LinearLayout layoutOk = (LinearLayout) dialog
				.findViewById(R.id.llAlertSingle);

		layoutOk.setVisibility(View.VISIBLE);
		layoutOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				responseListener.onResponce(message, Const.API_SUCCESS, null);
			}
		});
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.show();
	}

	public static void customResponseAlert(Context context,
			final String message, boolean isLargeAlert,
			final ResponseListener responseListener) {

		TextView txtMessage;
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert);
		dialog.setCancelable(false);

		if (isLargeAlert)
			txtMessage = (TextView) dialog.findViewById(R.id.txtAlertMsgBig);
		else
			txtMessage = (TextView) dialog.findViewById(R.id.txtAlertMsg);

		txtMessage.setVisibility(View.VISIBLE);
		txtMessage.setText(message);

		LinearLayout layoutOk = (LinearLayout) dialog
				.findViewById(R.id.llAlertSingle);

		layoutOk.setVisibility(View.VISIBLE);
		layoutOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				responseListener.onResponce(message, Const.API_SUCCESS, null);
			}
		});
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.show();
	}

	public static void customAlert(final Context context, final String message,
			boolean isLargeAlert, boolean isTwoButton,
			final ResponseListener responseListener) {

		TextView txtMessage;
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert);
		dialog.setCancelable(false);

		if (isLargeAlert)
			txtMessage = (TextView) dialog.findViewById(R.id.txtAlertMsgBig);
		else
			txtMessage = (TextView) dialog.findViewById(R.id.txtAlertMsg);

		txtMessage.setVisibility(View.VISIBLE);
		txtMessage.setText(message);

		if (isTwoButton) {

			LinearLayout layout = (LinearLayout) dialog
					.findViewById(R.id.llAlertDouble);
			layout.setVisibility(View.VISIBLE);

			TextView txtFirst = (TextView) dialog
					.findViewById(R.id.txtAlertFirst);
			txtFirst.setText("YES");
			TextView txtSecond = (TextView) dialog
					.findViewById(R.id.txtAlertSecond);
			txtSecond.setText("NO");

			LinearLayout layoutOk = (LinearLayout) dialog
					.findViewById(R.id.llAlertOk);

			layoutOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					responseListener.onResponce(message, Const.API_SUCCESS,
							null);
				}
			});

			LinearLayout layoutCancel = (LinearLayout) dialog
					.findViewById(R.id.llAlertCancel);
			layoutCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					responseListener.onResponce(message, Const.API_FAIL, null);
				}
			});

		} else {
			LinearLayout layoutOk = (LinearLayout) dialog
					.findViewById(R.id.llAlertSingle);

			layoutOk.setVisibility(View.VISIBLE);
			layoutOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					responseListener.onResponce(message, Const.API_SUCCESS,
							null);
				}
			});
		}
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.show();
	}*/

	public static void createDirectoryAndSaveFile(Bitmap imageToSave,
			String fileName) {

		File file = new File(fileName);

		try {
			FileOutputStream out = new FileOutputStream(file);
			imageToSave.compress(Bitmap.CompressFormat.PNG, 50, out);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void downloadFile(String src, String dest) {
		try {
			int count = 0;
			URL url = new URL(src);
			URLConnection conexion = url.openConnection();
			conexion.connect();

			int lenghtOfFile = conexion.getContentLength();

			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream(dest); // save file in SD
																// Card

			byte data[] = new byte[1024];

			long total = 0;

			while ((count = input.read(data)) != -1) {
				total += count;
				// publishProgress(""+(int)((total*100)/lenghtOfFile));
				System.out.println(":: progress"
						+ ((int) ((total * 100) / lenghtOfFile)));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void copy(String src, String dest) {

		FileInputStream in = null;
		FileOutputStream out = null;
		byte[] buf = null;
		int len;

		try {
			System.out.println(":: src ::" + src);
			System.out.println(":: dest ::" + dest);
			in = new FileInputStream(src);
			out = new FileOutputStream(dest);

			buf = new byte[1024];

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			in = null;
			out = null;
			buf = null;
			len = 0;
			System.gc();
		}

	}

	public static String getDate(long time) {
		String mySelectedDate = "";
		Calendar cal = Calendar.getInstance();
		TimeZone tz = cal.getTimeZone();// get your local time zone.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(tz);// set time zone.
		String localTime = sdf.format(time * 1000);
		Date date = new Date();
		try {
			date = sdf.parse(localTime);// get local date

			mySelectedDate = sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// System.out.println("::: mySelectedDate :"+ mySelectedDate);

		return mySelectedDate;
	}

	public static String getDate(long milliSeconds, String dateFormat) {
		// Create a DateFormatter object for displaying date in specified
		// format.
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

		// Create a calendar object that will convert the date and time value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	/**
	 * Convert Current Date to String Format Function
	 */
	public static String convertDateToString(Date objDate, String parseFormat) {
		try {
			return new SimpleDateFormat(parseFormat, Locale.US).format(objDate);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Convert Current String to Date Function
	 */
	public static Date convertStringToDate(String strDate, String parseFormat) {
		try {
			return new SimpleDateFormat(parseFormat, Locale.US).parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Convert Current String to String formate with apply new date formate
	 * Function
	 */
	public static String convertDateStringToString(String strDate,
			String currentFormat, String parseFormat) {
		try {
			return convertDateToString(
					convertStringToDate(strDate, currentFormat), parseFormat);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	public static String getFileExtension(String file) {
		String fileName = new File(file).getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}



	/** example : (888) 777-7777 */
	public static void setPhoneMasking(InputFilter filter) {
		filter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (source.length() > 0) {

					if (!Character.isDigit(source.charAt(0)))
						return "";
					else {
						if (dstart == 3) {
							return source + ") ";
						} else if (dstart == 0) {
							return "(" + source;
						} else if ((dstart == 5) || (dstart == 9))
							return "-" + source;
						else if (dstart >= 14)
							return "";
					}

				} else {

				}

				return null;

			}
		};
	}

	// public static void generatePushID(Context context) {
	//
	// final String regId = GCMRegistrar.getRegistrationId(context);
	// System.out.println("::::::: REG ID :: "+ regId);
	// if(regId.equals("")){
	// GCMRegistrar.register(context, CommonUtilities.SENDER_ID);
	// }
	// }
	// public static void generatePushID(Context context) {
	//
	// try {
	// String regId;
	// System.out.println("::PUSH ID:"
	// + Pref.getValue(context, Const.PREF_PUSH_ID, "") + ":");
	// if ((Pref.getValue(context, Const.PREF_PUSH_ID, "")).equals("")) {
	// GCMRegistrar.checkDevice(context);
	// GCMRegistrar.checkManifest(context);
	// regId = GCMRegistrar.getRegistrationId(context);
	//
	// if (regId.equals("")) {
	//
	// // Registration is not present, register now with GCM
	// GCMRegistrar.register(context, CommonUtilities.SENDER_ID);
	// } else {
	// if (GCMRegistrar.isRegisteredOnServer(context)) {
	// Pref.setValue(context, Const.PREF_PUSH_ID, regId);
	// }
	// // else {
	// // mRegisterTask = new AsyncTask<Void, Void, Void>() {
	// //
	// // @Override
	// // protected Void doInBackground(Void... params) {
	// // //ServerUtilities.register(SplashActivity.this, regId);
	// // return null;
	// // }
	// //
	// // @Override
	// // protected void onPostExecute(Void result) {
	// // mRegisterTask = null;
	// // }
	// //
	// // };
	// // mRegisterTask.execute(null, null, null);
	// // }
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public static String filter(String value) {
		String output = "";
		try {
			// System.out.println(":Utils_functions: intput " + value);
			for (int k = 0; k < value.length(); k++) {

				if (k == 3) {
					output += ("-" + value.charAt(k));
				} else if (k == 6) {
					output += ("-" + value.charAt(k));
				} else {
					output += value.charAt(k);
				}
			}
			// System.out.println(":Utils_functions: output:" + output);
			return output;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public static String millisToDate(long millis, String format) {
		return new SimpleDateFormat(format).format(new Date(millis));
	}

	@SuppressLint("NewApi")
	public static String getPath(Uri uri, Context context) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = ((Activity) context).managedQuery(uri, projection,
				null, null, null);
		if (cursor != null) {
			if (uri.getHost().contains("com.android.providers.media")) {
				// Image pick from recent

				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;

				contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };
				String filePath = getDataColumn(context, contentUri, selection,
						selectionArgs);
				System.out.println("::::::::::recent:::::::::" + filePath);

				return filePath;
			} else {
				// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
				// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE
				// MEDIA
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				return cursor.getString(column_index);
			}
		} else
			return null;
	}

//	private static String getDataColumn(Context context, Uri uri,
//			String selection, String[] selectionArgs) {
//		Cursor cursor = null;
//		final String column = "_data";
//		final String[] projection = { column };
//
//		try {
//			cursor = context.getContentResolver().query(uri, projection,
//					selection, selectionArgs, null);
//			if (cursor != null && cursor.moveToFirst()) {
//				final int column_index = cursor.getColumnIndexOrThrow(column);
//				return cursor.getString(column_index);
//			}
//		} finally {
//			if (cursor != null)
//				cursor.close();
//		}
//		return null;
//	}

	/*public static void callState(Context context, String phoneNo) {

		Intent intent;
		TelephonyManager telMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int simState = telMgr.getSimState();
		switch (simState) {
		case TelephonyManager.SIM_STATE_ABSENT:
			customAlert(context, "Sim Card not Available", false);
			break;
		case TelephonyManager.SIM_STATE_READY:
			intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + phoneNo));
			context.startActivity(intent);
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			customAlert(context, "Unknown Sim Card", false);
			break;
		}
	}*/
	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @author paulburke
	 */
	/*public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {
	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}*/

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
}
