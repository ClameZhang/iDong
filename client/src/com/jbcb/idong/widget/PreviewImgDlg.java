package com.jbcb.idong.widget;

import com.jbcb.idong.async.NormalImageLoader;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * @ClassName PreviewImgDlg.java
 * @author Clame
 * 
 */
public class PreviewImgDlg extends Activity {

	/** Called when the activity is first created. */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);   
	    
	    ImageView image=new ImageView(this);
        image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        setContentView(image);
        
		Intent intent = this.getIntent();
		if (intent.getExtras() != null) {
			String imageurl = intent.getExtras().getString("imageurl");
			
	        DisplayMetrics dm = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(dm);			
			NormalImageLoader yncTask=new NormalImageLoader(imageurl, dm.widthPixels * dm.heightPixels, image);
			yncTask.execute();
		}
		
		this.setFinishOnTouchOutside(true);
		
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreviewImgDlg.this.finish();
			}
		});
	}

}
