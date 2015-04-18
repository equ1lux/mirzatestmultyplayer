package com.example.mirzatestmultyplayer;

import java.util.ArrayList;

import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class RoomsAdapter extends BaseAdapter {

	private ArrayList<String> roomIdList = new ArrayList<String>();
    private ArrayList<String> roomNameList = new ArrayList<String>();
	private Context context;
	private RoomsActivity roomsActivity;
	
	RoomsAdapter(Context c){
		this.context = c;
		roomsActivity = (RoomsActivity)context;
	}
	
	@Override
	public int getCount() {	
		return roomIdList.size();
	}
	
	public void setData(RoomData[] roomData){
		roomIdList.clear();
		for(int i=0;i<roomData.length;i++){
			roomIdList.add(roomData[i].getId());
            roomNameList.add(roomData[i].getName());
		}
		notifyDataSetChanged();
	}
	public void clear(){
		roomIdList.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public Object getItem(int number) {
		return roomIdList.get(number);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.room_item, null);
        }
        if (roomIdList != null) {
        	TextView roomName = (TextView) convertView.findViewById(R.id.item_roomId);
        	Button joinButton = (Button) convertView.findViewById(R.id.item_joinButton);
            roomName.setText(roomNameList.get(position));
        	joinButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					roomsActivity.joinRoom(roomIdList.get(position));
				}
			});
        }
        return convertView;	
	}
}
