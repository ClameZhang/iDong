package com.jbcb.idong.cache;

import com.jbcb.idong.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName PartyListVierCache.java
 * @author Clame
 * 
 */
public class PartyListViewCache {
	private View baseView;
	private TextView textView_title;
	private TextView textView_detail;
	private ImageView imageView_icon;

	public PartyListViewCache(View baseView) {
		this.baseView = baseView;
	}

	public TextView getTextViewTitle() {
		if (textView_title == null) {
			textView_title = (TextView) baseView.findViewById(R.id.tv_partytitle);
		}
		return textView_title;
	}

	public TextView getTextViewDetail() {
		if (textView_detail == null) {
			textView_detail = (TextView) baseView.findViewById(R.id.tv_partydetail);
		}
		return textView_detail;
	}

	public ImageView getImageViewIcon() {
		if (imageView_icon == null) {
			imageView_icon = (ImageView) baseView.findViewById(R.id.iv_partyicon);
		}
		return imageView_icon;
	}
}
