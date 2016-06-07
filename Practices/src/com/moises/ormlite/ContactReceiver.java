package com.moises.ormlite;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

public class ContactReceiver extends BroadcastReceiver{

	public static final int ADD = 0;
	public static final int DELETE = 1;
	public static final int UPDATE = 2;
	public static final String OPERATION = "operation";
	public static final String DATA = "data";
	
	private static ArrayAdapter<Contact> adapter;
	
	public ContactReceiver(ArrayAdapter<Contact> adapter){
		this.adapter = adapter;
	}
	
	@Override
	public void onReceive(Context contexr, Intent intent) {
		int operation = intent.getIntExtra(OPERATION, -1);
		switch(operation){
			case ADD: addContact(intent); break;
			case DELETE: deleteContact(intent); break;
			case UPDATE: updateContact(intent); break;
		}
	}
	
	private void addContact(Intent intent) {
		Contact contact = (Contact)intent.getSerializableExtra(DATA);
		adapter.add(contact);
	}
	
	private void deleteContact(Intent intent) {
		ArrayList<Contact> list = (ArrayList<Contact>)intent.getSerializableExtra(DATA);
		for(Contact contact: list){
			adapter.remove(contact);
		}
	}

	private void updateContact(Intent intent) {
		Contact contact = (Contact)intent.getSerializableExtra(DATA);
		int position = adapter.getPosition(contact);
		adapter.insert(contact, position);
	}	

}
