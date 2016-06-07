package com.moises.broadcastreceiver;

import java.util.ArrayList;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListContact extends Fragment{

	private View view;
	private ListView lvListContact;
	private ListContactAdapter adapter;
	private ContactReceiver receiver;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.list_contact, container, false);
		setup();
		setHasOptionsMenu(true);
		return view;
	}
	
	private void setup() {
		lvListContact = (ListView)view.findViewById(R.id.lvListContact);
//		adapter = new ListContactAdapter(getActivity(), new ArrayList<Contact>());
//		adapter.setNotifyOnChange(true);
		try {
			Log.d("COUNT>>>>>>>>>>>>>", ContactReceiver.getAdapter().getCount()+"");
			adapter = (ListContactAdapter)ContactReceiver.getAdapter();
			adapter.notifyDataSetChanged();
			lvListContact.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_list_contact).setVisible(false);
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
//		receiver = new ContactReceiver(adapter);
//		getActivity().registerReceiver(receiver, new IntentFilter("listacontactos"));
	}

//	@Override
//	public void onPause() {
//		super.onPause();
//		getActivity().unregisterReceiver(receiver);
//	}
//	
}
