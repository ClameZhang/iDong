package com.jbcb.idong;

import com.jbcb.idong.async.NormalImageLoader;
import com.jbcb.idong.model.Party;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName PartyDetailActivity.java
 * @author Clame
 * 
 */
public class PartyDetailActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partydetail);
        
        Party party = (Party)getIntent().getSerializableExtra("Party");
        
        TextView title = (TextView)findViewById(R.id.tv_partydetailtitle);
        title.setText(party.getTitle());
        
        ImageView icon = (ImageView)findViewById(R.id.iv_partydetailicon);
		String imageurl = party.getThumbnailURL();
		
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);			
		NormalImageLoader yncTask=new NormalImageLoader(imageurl, dm.widthPixels * dm.heightPixels, icon);
		yncTask.execute();
	
	    // TODO Auto-generated method stub
	}

}
