package com.jbcb.idong.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;

/**
 * @ClassName CommonUtility.java
 * @Description Some common methods
 * @author Clame
 * 
 */
public class CommonUtility {

    /**
     * @FunName getAppPath
     * @Description get the path of iDong app
     * @param N/A
     * @return the path of iDong app
     * 
     */
    public static String getAppPath() {
        String appPath = "";
        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
            appPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/iDong/";
        } else {
            appPath = android.os.Environment.getDataDirectory().getAbsolutePath() + "/iDong/";
        }

        File app = new File(appPath);
        if (!app.exists()) {
            app.mkdir();
        }

        return appPath;
    }

    /**
     * @FunNam0 getImageThumbnail
     * @Description get the thumbnail of an bitmap from Internet with the appointed width and height
     * @param bitmapURL
     * @param width
     * @param height
     * @return the thumbnail of an image
     * 
     */
    public static Bitmap getImageThumbnail(String bitmapURL, int width, int height) {
        Bitmap bitmap = null;
        InputStream in = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        // set the inJustDecodeBounds as false to get a real bitmap object
        options.inJustDecodeBounds = false;

        try {
            URL url = new URL(bitmapURL);
            in = url.openStream();
            bitmap = BitmapFactory.decodeStream(in, null, options);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // get the basic height and width of the bitmap
        int basicHeight = options.outHeight;
        int basicWidth = options.outWidth;

        // get the scaling
        int beHeight = basicHeight / height;
        int beWidth = basicWidth / width;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;

        // get the thumbnail of the bitmap
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return bitmap;
    }

    /**
     * @FunNam0 getImageThumbnail
     * @Description get the thumbnail of an bitmap from Internet with the appointed width and height
     * @param bitmap
     * @param width
     * @param height
     * @return the thumbnail of an image
     * 
     */
    public static Bitmap getImageThumbnail(Bitmap bitmapBasic, int widthT, int heightT) {
    	int width = bitmapBasic.getWidth(); 
    	int height = bitmapBasic.getHeight(); 
    	// 设置想要的大小 
    	int newWidth = widthT; 
    	int newHeight = heightT; 
    	// 计算缩放比例 
    	float scaleWidth = ((float) newWidth) / width; 
    	float scaleHeight = ((float) newHeight) / height; 
    	// 取得想要缩放的matrix参数
    	Matrix matrix = new Matrix(); 
    	matrix.postScale(scaleWidth, scaleHeight); 
    	// 得到新的图片 
    	Bitmap newbm = Bitmap.createBitmap(bitmapBasic, 0, 0, width, height, matrix, true);
    	return newbm;
    }
    
    /**
     * @FunName getData
     * @Description get data from the parties URL
     * @param N/A
     * @return N/A
     * 
     */
    public static String getWebData(String URL) {
        URL url = null;
        byte[] data = null;
        String jsonStr = "";
        
        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            InputStream inStream = conn.getInputStream();
            data = readInputSream(inStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return jsonStr;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return jsonStr;
        }

        jsonStr = new String(data);        
        return jsonStr;
    }

    /**
     * @FunName readInputSream
     * @Description read the input stream, and save it as byte[]
     * @param inStream
     * @return byte[]
     * 
     */
    private static byte[] readInputSream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
    

	public static Bitmap loadImageFromInternet(String url, int displaypixels) {		
		Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        
        InputStream stream;
		try {
			stream = new URL(url).openStream();
	        byte[] bytes = getBytes(stream);
	        //这3句是处理图片溢出的begin( 如果不需要处理溢出直接 opts.inSampleSize=1;)
	        opts.inJustDecodeBounds = true;
	        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);	        
	        opts.inSampleSize = computeSampleSize(opts, -1, displaypixels);
	        //end
	        opts.inJustDecodeBounds = false;
	        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return bitmap;
	}

    /**
     * 数据流转成btyle[]数组
     * */
    private static byte[] getBytes(InputStream is) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[2048];
        int len = 0;
        try {
            while ((len = is.read(b, 0, 2048)) != -1) {
                baos.write(b, 0, len);
                baos.flush();
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }
    /****
    *    处理图片bitmap size exceeds VM budget （Out Of Memory 内存溢出）
    */
    private static int computeSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }
    
    private static int computeInitialSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
