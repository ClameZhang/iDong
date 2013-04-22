package com.jbcb.idong.async;

import com.jbcb.idong.utilities.CommonUtility;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class NormalImageLoader extends AsyncTask<Object, Void, Bitmap> {
	String url;
	int displaypixels = 0;
	ImageView image;

	public NormalImageLoader(String url, int displaypixels, ImageView image) {
		this.url = url;
		this.displaypixels = displaypixels;
		this.image = image;
	}

	@Override
	protected Bitmap doInBackground(Object... params) {
		Bitmap drawable = CommonUtility.loadImageFromInternet(url, displaypixels);// »ñÈ¡ÍøÂçÍ¼Æ¬
		return drawable;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result == null) {
			return;
		}
		image.setImageBitmap(result);
	}
}
