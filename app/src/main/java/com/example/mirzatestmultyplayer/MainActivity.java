package com.example.mirzatestmultyplayer;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity implements ConnectionRequestListener {
	private WarpClient theClient;
	private EditText nameEditText;
    private ProgressDialog progressDialog;
    private boolean isConnected = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nameEditText = (EditText) findViewById(R.id.editText1);
		init();
	}
	
	private void init(){
		WarpClient.initialize(Constants.apiKey, Constants.secretKey);
        try {
            theClient = WarpClient.getInstance();
        } catch (Exception ex) {
        	Utils.showToastAlert(this, "Exception in Initilization");
        }
        theClient.addConnectionRequestListener(this); 
    }


	@Override
	public void onConnectDone(final ConnectEvent event) {
		Log.d("onConnectDone", event.getResult()+"");
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(progressDialog!=null){
					progressDialog.dismiss();
					progressDialog=null;
				}
			}
		});
		if(event.getResult() == WarpResponseResultCode.SUCCESS){// go to room  list 
			isConnected = true;
			Intent intent = new Intent(MainActivity.this, RoomsActivity.class);
			startActivity(intent);
		}else{
			isConnected = false;
			Utils.showToastOnUIThread(MainActivity.this, "connection failed");
		}
	}
	
	public void onEnterLobbyClicked(View view){
		if(nameEditText.getText().length()==0){
			Utils.showToastAlert(this, getApplicationContext().getString(R.string.Enter_username));
			return;
		}
		else{
			String userName = nameEditText.getText().toString();
			Utils.userName = userName;
			Log.d("Name to Join", ""+userName);
			theClient.connectWithUserName(userName);
			progressDialog =  ProgressDialog.show(this, "", "entering lobby");
		}
	}

	@Override
	public void onDisconnectDone(final ConnectEvent event) {
		Log.d("onDisconnectDone", event.getResult()+"");
		isConnected = false;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		theClient.removeConnectionRequestListener(this);
		if(theClient!=null && isConnected){
			theClient.disconnect();
		}
		
	}

	@Override
	public void onInitUDPDone(byte arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
