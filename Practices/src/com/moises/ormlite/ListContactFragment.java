package com.moises.ormlite;

import java.util.ArrayList;

import com.moises.practices.R;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListContactFragment extends Fragment{

	private View view;
	private ListView listContact;
	private ArrayAdapter<Contact> adapter;
	private ContactReceiver contactReceiver;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.list_contact, container, false);
		setup();
		return view;
	}

	private void setup() {
		listContact = (ListView)view.findViewById(R.id.listContact);
		adapter = new ListContactAdapter(getActivity());
		adapter.setNotifyOnChange(true);
		listContact.setAdapter(adapter);	
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.main, menu);
		menu.findItem(R.id.action_delete).setVisible(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
			case R.id.action_delete: deleteContact(item); return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void deleteContact(MenuItem item) {
		SparseBooleanArray array = listContact.getCheckedItemPositions();
		ArrayList<Contact> select = new ArrayList<Contact>();
		for(int i = 0; i<array.size(); i++){
			int position = array.keyAt(i);
			if(array.valueAt(i)) select.add(adapter.getItem(position));
		}
		
		Intent intent = new Intent("listcontact");
		intent.putExtra(ContactReceiver.OPERATION, ContactReceiver.DELETE);
		intent.putExtra(ContactReceiver.DATA, select);
		listContact.clearChoices();
	}

	@Override
	public void onResume(){
		super.onResume();
		contactReceiver = new ContactReceiver(adapter);
		getActivity().registerReceiver(contactReceiver, new IntentFilter("listcontact"));
	}
	
	@Override
	public void onPause(){
		super.onPause();
		getActivity().unregisterReceiver(contactReceiver);
	}
	
}
