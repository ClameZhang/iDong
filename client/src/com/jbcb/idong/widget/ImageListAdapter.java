package com.jbcb.idong.widget;

import java.util.List;

import com.jbcb.idong.R;
import com.jbcb.idong.async.ListViewImageLoader;
import com.jbcb.idong.cache.PartyListViewCache;
import com.jbcb.idong.model.Party;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName ImageListAdapter.java
 * @author Clame
 * 
 */
public class ImageListAdapter extends BaseAdapter {
    private Activity    context;
    private List<Party> partyList;
	private boolean		mBusy = false;
	private ListViewImageLoader mImageLoader;

    public ImageListAdapter(Activity context, List<Party> partyList) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.partyList = partyList;
        
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);        
		mImageLoader = new ListViewImageLoader(dm.widthPixels * dm.heightPixels);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return partyList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return partyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
    	View rowView = convertView;
    	PartyListViewCache cache;
    	
    	// check if listview cache exist or not
    	if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listveiw_searchparty, null);
            cache = new PartyListViewCache(rowView);
            rowView.setTag(cache);
    	} else {
    		cache = (PartyListViewCache)rowView.getTag();
    	}
        
        ImageView iv_partyicon = cache.getImageViewIcon();
        TextView tv_title = cache.getTextViewTitle();
        TextView tv_detail = cache.getTextViewDetail();

        Party party = partyList.get(position);

        tv_title.setText(party.getTitle());
        tv_detail.setText(party.getDescription());

        final String imageURL = party.getThumbnailURL();
        if (!mBusy) {
			mImageLoader.loadImage(imageURL, this, cache);
        } else {
			Bitmap bitmap = mImageLoader.getBitmapFromCache(imageURL);
			if (bitmap != null) {
				iv_partyicon.setImageBitmap(bitmap);
			} else {
				iv_partyicon.setImageResource(R.drawable.ic_launcher);
			}
        }

        iv_partyicon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Click(imageURL);
            }
        });

        return rowView;
    }

    public void Click(String imageURL) {
        Intent intent = new Intent();
        intent.putExtra("imageurl", imageURL);
        intent.setClass(context, PreviewImgDlg.class);
        context.startActivity(intent);
    }

	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}
}
