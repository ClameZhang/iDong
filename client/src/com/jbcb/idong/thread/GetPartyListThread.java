/**
 * 
 */
package com.jbcb.idong.thread;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Message;

import com.jbcb.idong.FragmentSearch;
import com.jbcb.idong.model.Party;
import com.jbcb.idong.utilities.CommonUtility;

/**
 * @ClassName GetPartyThread.java
 * @author Clame
 * 
 */
public class GetPartyListThread extends Thread {
    private List<Party> partyList;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void run() {
        String URL = "http://holyweibo.sinaapp.com/parties/";
        String jsonStr = CommonUtility.getWebData(URL);
        
        if (!"".equals(jsonStr)) {
            partyList = new ArrayList<Party>();
            
            try {
                JSONArray array = new JSONArray(jsonStr);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    String title = item.getString("title");
                    String description = item.getString("category");
                    String thumbnail = item.getString("thumbnail");

                    Party party = new Party();
                    party.setTitle(title);
                    party.setDescription(description);
                    party.setThumbnailURL(thumbnail);
                    partyList.add(party);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            Bundle bundle = new Bundle();
            ArrayList list = new ArrayList();
            list.add(partyList);
            bundle.putParcelableArrayList("partylist", list);
            
            Message msg = new Message();
            msg.setData(bundle);
            FragmentSearch.handler.sendMessage(msg);
        }
    }
}
