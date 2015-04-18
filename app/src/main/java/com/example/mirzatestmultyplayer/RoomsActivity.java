package com.example.mirzatestmultyplayer;

import java.util.HashMap;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class RoomsActivity extends Activity implements ZoneRequestListener {
	
	private WarpClient theClient;
	private RoomsAdapter roomsAdapter;
	private ListView listView;
	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rooms);
		listView = (ListView)findViewById(R.id.roomList);
		roomsAdapter = new RoomsAdapter(this);
		init();
	}
	
	private void init(){
        try {
            theClient = WarpClient.getInstance();
        } catch (Exception ex) {
        	Utils.showToastAlert(this, "Exception in Initilization");
        }
    }
	
	public void onStart(){
		super.onStart();
		theClient.addZoneRequestListener(this);
		theClient.getRoomInRange(1, 1);// zemi sobi so eden user
	}
	
	public void onStop(){
		super.onStop();
		theClient.removeZoneRequestListener(this);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		theClient.disconnect();
	}
	
	public void onCreateNewRoomClicked(View view){
		progressDialog = ProgressDialog.show(this,"","Pleaes wait...");
		progressDialog.setCancelable(true);
		HashMap<String, Object> properties = new HashMap<String, Object>();
		/*properties.put("topLeft", "");
		properties.put("topRight", "");
		properties.put("bottomLeft", "");
		properties.put("bottomRight", "");*/
		theClient.createRoom(""+System.currentTimeMillis(), Utils.userName, 4, properties);
	}

	@Override
	public void onCreateRoomDone(final RoomEvent event) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progressDialog!=null){
					progressDialog.dismiss();
					progressDialog = null;
				}
				if(event.getResult()==WarpResponseResultCode.SUCCESS){// if room created successfully
					String roomId = event.getData().getId();
					joinRoom(roomId);
					Log.d("onCreateRoomDone", event.getResult()+" "+roomId);
				}else{
					Utils.showToastAlert(RoomsActivity.this, "Room creation failed...");
				}
			}
		});
	}
	
	public void joinRoom(String roomId){
		if(roomId!=null && roomId.length()>0){
			goToGameScreen(roomId);
		}else{
			Log.d("joinRoom", "failed:"+roomId);
		}
	}
	
	
	private void goToGameScreen(String roomId){
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("roomId", roomId);
		startActivity(intent);
	}

	@Override
	public void onDeleteRoomDone(RoomEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetAllRoomsDone(AllRoomsEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetLiveUserInfoDone(LiveUserInfoEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetMatchedRoomsDone(final MatchedRoomsEvent event) {
		runOnUiThread(new Runnable(){
			@Override
			public void run() {
				RoomData[] roomDataList = event.getRoomsData();
				if(roomDataList!=null && roomDataList.length>0){
					roomsAdapter.setData(roomDataList);
					listView.setAdapter(roomsAdapter);
				}else{
					roomsAdapter.clear();
				}
			}
		});
	}

	@Override
	public void onGetOnlineUsersDone(AllUsersEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetCustomUserDataDone(LiveUserInfoEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
