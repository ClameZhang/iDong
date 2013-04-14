package com.jbcb.idong;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jbcb.idong.model.Party;
import com.jbcb.idong.thread.HttpThread;
import com.jbcb.idong.utilities.CommonUtility;
import com.jbcb.idong.widget.ImageListAdapter;
import com.jbcb.idong.widget.ImageListView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ��Ҫʹ�ò��������Ĺ�����������ʹ��getActivity()�滻context���� ������Ļ����ת��ʱ����׳��쳣�� Caused by:
 * java.lang.InstantiationException: can't instantiate class
 * com.michael.fragment.FragmentExecute; no empty constructor
 * 
 * @see http
 *      ://stackoverflow.com/questions/7016632/unable-to-instantiate-fragment
 * */
public class FragmentSearch extends Fragment {
	HttpThread thread = null;
	String jsonStr = "";
	boolean showList = false;
	Activity context = null;
	View layout = null;
	List<Party> partyList = null;

	public FragmentSearch() {
	}

	private Handler handler = new Handler() {

		@Override
		// ������Ϣ���ͳ�����ʱ���ִ��Handler���������
		public void handleMessage(Message msg) {
			switch (msg.what) {  
            case 0:  
				ImageListAdapter adapter = new ImageListAdapter(context, partyList);
				ImageListView list = (ImageListView) layout
						.findViewById(R.id.lv_search_partylist);
				list.setAdapter(adapter);
				list.setItemsCanFocus(false);
				break;
        }  

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
		layout = myInflater.inflate(R.layout.fragment_search,
				container, false);

		partyList = new ArrayList<Party>();
		context = this.getActivity();

		new Thread() {
			@Override
			public void run() {
				getData();
				JSONArray array;
				try {
					array = new JSONArray(jsonStr);
					for (int i = 0; i < array.length(); i++) {
						JSONObject item = array.getJSONObject(i);
						String title = item.getString("title");
						String description = item.getString("category");
						String thumbnail = item.getString("thumbnail");

						Party party = new Party();
						party.setTitle(title);
						party.setDescription(description);		
						Bitmap img = CommonUtility.getImageThumbnail(thumbnail, 80, 80);
						party.setThumbnail(img);
						party.setThumbnailURL(thumbnail);
						partyList.add(party);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				handler.sendEmptyMessage(0);
			}
		}.start();

		// for (int i = 0; i < 3; i++) {
		// Party party = new Party();
		// String bitmapPath = "";
		// if (i == 0) {
		// party.setTitle("���߱�2");
		// party.setDescription("�ȱ�˭��2");
		// bitmapPath = Environment.getExternalStorageDirectory()
		// .getPath() + "/DCIM/Camera/1.png";
		// } else if (i == 1) {
		// party.setTitle("��������˹��ķ");
		// party.setDescription("�����Ǹ��õط�");
		// bitmapPath = Environment.getExternalStorageDirectory()
		// .getPath() + "/DCIM/Camera/2.png";
		// } else if (i == 2) {
		// party.setTitle("¬����Ӱ");
		// party.setDescription("¬����̫�������");
		// bitmapPath = Environment.getExternalStorageDirectory()
		// .getPath() + "/DCIM/Camera/4.png";
		// }
		//
		// Set<String> imageURLSet = new HashSet<String>();
		// imageURLSet.add(bitmapPath);
		// party.setPartyImageURLSet(imageURLSet);
		// partyList.add(party);
		// }

		return layout;
	}

	private void getData() {
		String path = "http://holyweibo.sinaapp.com/parties/";
		URL url = null;
		byte[] data = null;
		try {
			url = new URL(path);
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		jsonStr = new String(data);
		// Handler handler = new Handler();
		// thread = new HttpThread(handler, this.getActivity()); // �����߳�ʵ��
		//
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// // try {
		// // String strvalidate = "galyglxxxt";
		// // strvalidate = new String(strvalidate.toString().getBytes(),
		// "UTF-8");
		// // params.put("username", username);// �������
		// // params.put("pwd", password);
		// // params.put("validate", strvalidate);
		// // } catch (Exception ex) {
		// // ex.printStackTrace();
		// // }
		// String url = "http://holyweibo.sinaapp.com/parties/.json";//
		// webserivce��ַ
		// String nameSpace = "http://tempuri.org/"; // �ռ���,���޸�
		// String methodName = "User_login"; // �����webservice����
		// thread.doStart(url, nameSpace, methodName, params); // �����߳�
	}

	public byte[] readInputSream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}
}
