package com.jbcb.idong;

import java.util.ArrayList;
import java.util.List;

import com.jbcb.idong.model.Party;
import com.jbcb.idong.thread.GetPartyListThread;
import com.jbcb.idong.widget.ImageListAdapter;
import com.jbcb.idong.widget.ImageListView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * @ClassName FragmentSearch.java
 * @author Clame
 * 
 */
public class FragmentSearch extends Fragment {
	String jsonStr = "";
	boolean showList = false;
	static Activity context = null;
	static View layout = null;
	static ImageListAdapter adapter;

	public FragmentSearch() {
	}
	
	public static Handler handler = new Handler() {

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            ArrayList list = bundle.getParcelableArrayList("partylist");

            List<Party> partyList = new ArrayList<Party>();
            partyList= (List<Party>) list.get(0);
            
            ImageListAdapter adapter = new ImageListAdapter(context, partyList);
            ImageListView listView = (ImageListView) layout.findViewById(R.id.lv_search_partylist);
            listView.setAdapter(adapter);
            listView.setItemsCanFocus(false);
        }

    };

	static OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_FLING:
				adapter.setFlagBusy(true);
				break;
			case OnScrollListener.SCROLL_STATE_IDLE:
				adapter.setFlagBusy(false);
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				adapter.setFlagBusy(false);
				break;
			default:
				break;
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			// Currently in a layout without a container, so no
			// reason to create our view.
			return null;
		}
		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = myInflater.inflate(R.layout.fragment_search, container, false);
		context = this.getActivity();

		GetPartyListThread thread = new GetPartyListThread();
		thread.start();

		return layout;
	}
}
