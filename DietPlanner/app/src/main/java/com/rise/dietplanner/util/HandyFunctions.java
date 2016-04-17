package com.rise.dietplanner.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.widget.Toast;

import com.rise.dietplanner.R;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class HandyFunctions {
	Context _context;
	String playstoreLinkOfAndroidApp;
	String shareText;
	SharedPreferences sp;

	public String EXIT_POPUP_COUNTER = "EXIT_POPUP_COUNTER";
    public String LANGUAGE_ISO_CODE = "LANGUAGE_ISO_CODE";

	private static final String DATE_FORMATTER = "d MMM yyyy";
	private SimpleDateFormat formatter = null;

	// Activity _activity;

	public HandyFunctions(Context _context) {
		this._context = _context;
		sp = _context.getSharedPreferences("DietPlanner", Activity.MODE_PRIVATE);
		// playstoreLinkOfAndroidApp =
		// "https://play.google.com/store/apps/details?id="
		// + _context.getPackageName();
		formatter = new SimpleDateFormat(DATE_FORMATTER);
	}

	@SuppressWarnings("static-access")
	public void loadLanguage() {
		Locale locale = new Locale(sp.getString(LANGUAGE_ISO_CODE, "en"));
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		this._context.getResources().updateConfiguration(config,
				this._context.getResources().getDisplayMetrics());
	}

	public void showToast(String msg) {
		Toast t = Toast.makeText(_context, msg, Toast.LENGTH_LONG);
		t.show();
		// showCustomToast(msg);
	}

	public void showToast(int msg) {
		Toast t = Toast.makeText(_context, msg, Toast.LENGTH_LONG);
		t.show();
		// showCustomToast(msg);
	}

	public void showDevelopersAllApps(String developerName) {
		Uri uri = Uri.parse("market://search?q=pub:" + developerName);
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			_context.startActivity(goToMarket);
		} catch (Exception e) {
			showToast(R.string.tm_activity_not_found);
		}
	}

	public void rateUs() {
		Uri uri = Uri.parse("market://details?id=" + _context.getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			_context.startActivity(goToMarket);
		} catch (Exception e) {
			showToast(R.string.tm_activity_not_found);
		}

	}
	
	@SuppressWarnings("static-access")
	public void exitFromApp() {
        SharedPreferences.Editor editor = sp.edit();
		editor.putInt(EXIT_POPUP_COUNTER,
				sp.getInt(EXIT_POPUP_COUNTER, 0) + 1);
        editor.commit();

		AlertDialog alertDialog = new AlertDialog.Builder(_context).create();
		// Setting Dialog Title
		alertDialog.setTitle("Exit");

		// Setting Dialog Message
		alertDialog.setMessage("Are you sure ?");

		// Setting OK Button
		alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				_context.startActivity(intent);

			}
		});

		alertDialog.setButton2("Rate Us",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						rateUs();
					}
				});
		alertDialog.setIcon(android.R.drawable.ic_menu_help);
		// Showing Alert Message
		alertDialog.show();
		alertDialog.setCancelable(true);
	}

	@SuppressWarnings("static-access")
	public void exitFromAppWithoutRateOption() {
		SharedPreferences.Editor editor = sp.edit();
        editor.putInt(EXIT_POPUP_COUNTER,
				sp.getInt(EXIT_POPUP_COUNTER, 0) + 1);
		final AlertDialog alertDialog = new AlertDialog.Builder(_context)
				.create();
		// Setting Dialog Title
		alertDialog.setTitle("Exit");

		// Setting Dialog Message
		alertDialog.setMessage("Are you sure ?");

		// Setting OK Button
		alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				_context.startActivity(intent);

			}
		});

		alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.setIcon(android.R.drawable.ic_menu_help);
		// Showing Alert Message
		alertDialog.show();
		alertDialog.setCancelable(true);
	}

	public void openWebPage(String url) {
		try {
			Intent i1 = new Intent(Intent.ACTION_VIEW);
			i1.setData(Uri.parse(url));
			_context.startActivity(i1);
		} catch (Exception e) {
		}
	}

	public void openFacebookWebPage(String url, String urlId) {
		try {// this code open direct app
			_context.getPackageManager().getPackageInfo("com.facebook.katana",
					0);
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(urlId));
			_context.startActivity(i);
		} catch (Exception e) {
			// this code open pages in browser
			Intent i1 = new Intent(Intent.ACTION_VIEW);
			i1.setData(Uri.parse(url));
			_context.startActivity(i1);
		}
	}

	public void openTwitterWebPage(String url, String urlId) {
		try {// this code open direct app
			_context.getPackageManager().getPackageInfo("com.twitter.android",
					0);
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(urlId));
			_context.startActivity(i);
		} catch (Exception e) {
			// this code open pages in browser
			Intent i1 = new Intent(Intent.ACTION_VIEW);
			i1.setData(Uri.parse(url));
			_context.startActivity(i1);
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTimeStamp() {
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"dd-MM-yyyy HH:mm:ss");
			String currentTimeStamp = dateFormat.format(new Date()); // Find
																		// todays
																		// date

			return currentTimeStamp;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	public String getEmailAssocitedToDevice() {
		String myEmail = null;
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(_context).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				myEmail = account.name;
				break;
			}
		}
		return myEmail;
	}

	public String getServiceProvider() {
		String carrierName;
		try {
			TelephonyManager manager = (TelephonyManager) _context
					.getSystemService(Context.TELEPHONY_SERVICE);

			carrierName = manager.getNetworkOperatorName();
			return carrierName;
		} catch (Exception e) {
			carrierName = "default";
			e.printStackTrace();
		}
		return carrierName;
	}

	public Map<String, Object> mapProperties(Object bean) throws Exception {
	    Map<String, Object> properties = new HashMap<String, Object>();
	    for (Method method : bean.getClass().getDeclaredMethods()) {
	        if (Modifier.isPublic(method.getModifiers())
	            && method.getParameterTypes().length == 0
	            && method.getReturnType() != void.class
	            && method.getName().matches("^(get|is).+")
	        ) {
	            String name = method.getName().replaceAll("^(get|is)", "");
	            name = Character.toLowerCase(name.charAt(0)) + (name.length() > 1 ? name.substring(1) : "");
	            Object value = method.invoke(bean);
	            properties.put(name, value);
	        }
	    }
	    return properties;
	}

	public Drawable getVegetableBackground(String colorString) {

		Drawable mDrawable = null;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mDrawable = _context.getResources().getDrawable(R.drawable.selected_veg_indicator_drawable, _context.getTheme());
		}
		else {
			mDrawable = _context.getResources().getDrawable(R.drawable.selected_veg_indicator_drawable);
		}

		PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
		mDrawable.setColorFilter(Color.parseColor(colorString), mMode);

		return mDrawable;
	}

	public int dpToPx(int dp)
	{
	    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	public int pxToDp(int px)
	{
	    return (int) (px / Resources.getSystem().getDisplayMetrics().density);
	}

	public SimpleDateFormat getDateFormatter() {

		return formatter;
	}
}
