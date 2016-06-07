package com.moises.broadcastreceiver;

import java.util.ArrayList;

import com.moises.broadcastreceiver.Lista.OptionClickListener;
import com.moises.broadcastreceiver.NewContact.SelectAddressClickListener;
import com.moises.chat.ChatFragment;
import com.moises.generic.Global;
import com.moises.playlist.PlayListFragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class MainActivity extends FragmentActivity implements SelectAddressClickListener, OptionClickListener{

	private NewContact nuevoContacto;
	private Lista lista;
	private ListContact listContact;
	private ChatFragment chatFragment;
	private PlayListFragment playListFragment;
	
	private ArrayAdapter<Contact> adapter;
	private ContactReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupReceiver();
		Global.setContext(this);
		if(nuevoContacto == null)
			nuevoContacto = new NewContact();
		loadingFragment(nuevoContacto, "tagNC");
	}
	
	public void initTabs(){
		//ActionBar actionBar = getActionBar();
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}
	
	private void setupReceiver() {
		adapter = new ListContactAdapter(this, new ArrayList<Contact>());
		receiver = new ContactReceiver(adapter);
		registerReceiver(receiver, new IntentFilter("listacontactos"));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_list_contact:
				if(listContact==null){
					listContact = new ListContact();
				}
				loadingFragment(listContact, "tagListContact");
				return true;
			case R.id.action_chat:
				if(chatFragment==null){
					chatFragment = new ChatFragment();
				}
				loadingFragment(chatFragment, "tagChat");
				return true;
			case R.id.action_play_list:
				if(playListFragment==null){
					playListFragment = new PlayListFragment();
				}
				loadingFragment(playListFragment, PlayListFragment.TAG_FRAGMENT);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void loadingFragment(Fragment fragment, String tag){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft =  fm.beginTransaction();
		ft.replace(R.id.myContainer, fragment);
		if(tag!=null){
			ft.addToBackStack(tag);
		}
		ft.commit();
	}

	

	@Override
	public void SelectAddressClick() {
		lista = new Lista();
		loadingFragment(lista, null);
	}
	
	@Override
	public void OptionClick(String address) {
		Bundle bundle = new Bundle();
		bundle.putString("address", address);
		if(nuevoContacto!=null){
			//nuevoContacto.setArguments(bundle);
			loadingFragment(nuevoContacto, null);
		}
	}
	
	@Override
	public void onBackPressed(){
		if(getSupportFragmentManager().getBackStackEntryCount()>1){
			getSupportFragmentManager().popBackStack();
		}else{
			this.finish();
		}
	}	

//	public class MyTabListener implements ActionBar.{
//		
//		Fragment myFragmento;
//		public MyTabListener(Fragment fragment){
//			this.myFragmento = fragment;
//		}
//	
//		@Override
//		public void onTabReselected(Tab tab, FragmentTransaction ft) {
//			ft.replace(R.id.myContenedor, myFragmento);
//		}
//	
//		@Override
//		public void onTabSelected(Tab tab, FragmentTransaction ft) {
//			ft.replace(R.id.myContenedor, myFragmento);
//		
//		}
//	
//		@Override
//		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//			ft.remove(myFragmento);
//		}
//	}
	
}
