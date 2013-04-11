package com.jbcb.idong;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jbcb.idong.model.Party;
import com.jbcb.idong.widget.ImageListAdapter;
import com.jbcb.idong.widget.ImageListView;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ��Ҫʹ�ò��������Ĺ�����������ʹ��getActivity()�滻context����
 * ������Ļ����ת��ʱ����׳��쳣��
 * Caused by: java.lang.InstantiationException: 
 * can't instantiate class com.michael.fragment.FragmentExecute; no empty constructor
 * 
 * @see http://stackoverflow.com/questions/7016632/unable-to-instantiate-fragment
 * */
public class FragmentSearch extends Fragment
{

	public FragmentSearch()
	{
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		if (container == null) 
		{
            // Currently in a layout without a container, so no
            // reason to create our view.
            return null;
        }
		LayoutInflater myInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	    View layout = myInflater.inflate(R.layout.fragment_search, container, false);
	    
		final List<Party> partyList = new ArrayList<Party>();
		
		for (int i = 0; i < 3; i ++) {
			Party party = new Party();
			String bitmapPath = "";
			if (i == 0) {
				party.setTitle("���߱�2");
				party.setDescription("�ȱ�˭��2");
				bitmapPath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/1.png";
			} else if (i == 1) {
				party.setTitle("��������˹��ķ");
				party.setDescription("�����Ǹ��õط�");
				bitmapPath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/2.png";
			} else if (i == 2) {
				party.setTitle("¬����Ӱ");
				party.setDescription("¬����̫�������");
				bitmapPath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/4.png";
			}
			
			Set<String> imageURLSet = new HashSet<String>();
			imageURLSet.add(bitmapPath);			
			party.setPartyImageURLSet(imageURLSet);
			partyList.add(party);
		}

		final ImageListView list = (ImageListView) layout.findViewById(R.id.lv_search_partylist);
		ImageListAdapter adapter = new ImageListAdapter(this.getActivity(), partyList);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		
		return layout;
	}
}
