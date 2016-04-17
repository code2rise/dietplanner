package com.rise.dietplanner.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageUtility {
	
	private static final int IMAGE_MAX_SIZE = 1024;
	public static String PROFILE_PIC_FOLDER_PATH, CAPTURED_IMAGE_FOLDER_PATH;
	private String root;
	private String profileImageName, capturedImageName;		
	private String profileImagePath;
	private File saveImageDir;
	private File lastImageDir;
	private int height_width;
	private HandyFunctions handyFunctions = null;
	
	public ImageUtility(Context context) {
		root = context.getFilesDir().toString();
//		root = Environment.getExternalStorageDirectory().toString();
		CAPTURED_IMAGE_FOLDER_PATH = root + "/DietPlanner/Captured/";
		
		handyFunctions = new HandyFunctions(context);
		
		profileImageName = "profile_pic_";
		capturedImageName = "mh_";
		height_width = 256;
	}

	public Bitmap getLastSavedProfilePic() {
		lastImageDir = new File(PROFILE_PIC_FOLDER_PATH);
		if (!lastImageDir.exists())
			lastImageDir.mkdirs();
		Bitmap myBitmap = BitmapFactory.decodeFile(profileImagePath);
		return getRoundedShape(myBitmap);
	}

	public String saveProfilePic(Bitmap finalBitmap) {
		saveImageDir = new File(PROFILE_PIC_FOLDER_PATH);
		if (!saveImageDir.exists())
			saveImageDir.mkdirs();
		
		profileImageName = profileImageName + System.currentTimeMillis()/1000L + ".jpg";
		File file = new File(saveImageDir, profileImageName);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return profileImageName;
	}

	public String saveCapturedImage(Bitmap finalBitmap) {
		saveImageDir = new File(CAPTURED_IMAGE_FOLDER_PATH);
		if (!saveImageDir.exists())
			saveImageDir.mkdirs();
		
		String finalSavedImgName = capturedImageName + System.currentTimeMillis()/1000L + ".jpg";
		
		File file = new File(saveImageDir, finalSavedImgName);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return file.getAbsolutePath();
	}

	public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
		Bitmap result = null;
		try {
			result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(result);

			int color = 0xff424242;
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			RectF rectF = new RectF(rect);
			int roundPx = pixels;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 255, 255);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (NullPointerException e) {
			// return bitmap;
		} catch (OutOfMemoryError o) {
		}
		return result;
	}

	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = height_width;
		int targetHeight = height_width;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		try {
			canvas.drawBitmap(
					sourceBitmap,
					new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap
							.getHeight()), new Rect(0, 0, targetWidth,
							targetHeight), null);
		} catch (NullPointerException e) {

			e.printStackTrace();
			return null;

		}
		return targetBitmap;
	}

	/** SD */
	public List<String> getImagePathFromSD() {
		/*  */
		List<String> it = new ArrayList<String>();

		// SDCard
//		String imagePath = Environment.getExternalStorageDirectory().toString()
//				+ capturedFolderName;

		File mFile = new File(CAPTURED_IMAGE_FOLDER_PATH);
		File[] files = mFile.listFiles();

		/* ArrayList */
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (checkIsImageFile(file.getPath()))
				it.add(file.getPath());
		}
		Collections.reverse(it);
		return it;
	}
	
	public Bitmap loadCapturedImageFromStorage(String fileName)
	{
		Bitmap capturedImageBitmap = null;
	    try {
	        File f = new File(CAPTURED_IMAGE_FOLDER_PATH, fileName);
	        capturedImageBitmap = BitmapFactory.decodeStream(new FileInputStream(f));
	    } 
	    catch (FileNotFoundException e) 
	    {
	        e.printStackTrace();
	    }

	    return capturedImageBitmap;
	}
	
	public Bitmap loadProfilePicFromStorage(String fileName)
	{
		Bitmap profilePicBitmap = null;
	    try {
	        File f = new File(PROFILE_PIC_FOLDER_PATH, fileName);
	        profilePicBitmap = BitmapFactory.decodeStream(new FileInputStream(f));
	    } 
	    catch (FileNotFoundException e) 
	    {
	        e.printStackTrace();
	    }

	    return profilePicBitmap;
	}

	private boolean checkIsImageFile(String fName) {
		boolean isImageFormat;

		/*  */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* MimeType */
		if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			isImageFormat = true;
		} else {
			isImageFormat = false;
		}
		return isImageFormat;
	}

	public int getCameraPhotoOrientation(Context context, String imagePath) {
		int rotate = 0;
		try {
			ExifInterface exif = new ExifInterface(imagePath);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}

			Log.i("RotateImage", "Exif orientation: " + orientation);
			Log.i("RotateImage", "Rotate value: " + rotate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}
	
    public Bitmap decodeFile(File f) {
        Bitmap b = null;

        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return b;
    }
    
    public Bitmap rotateImage(Bitmap bitmap, int angle) {

        Bitmap targetBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setFilterBitmap(true);

        Matrix matrix = new Matrix();
        matrix.setRotate((float) angle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
//        RectF rectf = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        matrix.mapRect(rectf);

//        targetBitmap = Bitmap.createBitmap((int)rectf.width(), (int)rectf.height(), Bitmap.Config.ARGB_8888);
        targetBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) bitmap.getWidth(), (int) bitmap.getHeight(), matrix, true);

//        Canvas mCanvas = new Canvas(targetBitmap);
//        mCanvas.drawBitmap(bitmap, matrix, p);

        return targetBitmap;
    }
    
    public Bitmap generateThumbnails(String imagePath) {
    	
		return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath),
				handyFunctions.dpToPx(50), handyFunctions.dpToPx(50));
    }
}
