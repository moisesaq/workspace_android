package com.moises.broadcastreceiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ContactReceiver extends BroadcastReceiver{

	public static final int CONTACT_ADD = 1;
	public static final int CONTACT_DELETE = 2;
	public static final int CONTACT_UPDATE = 3;
	
	public static ArrayAdapter<Contact> adapter;
	
	
	public ContactReceiver(ArrayAdapter<Contact> adapter){
		this.adapter = adapter;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int operation = intent.getIntExtra("operation", -1);
		switch (operation) {
			case CONTACT_ADD:
				//TODO NO ESTA FUNCIONANDO ESTO
				addContact(context, intent);
				break;
			case CONTACT_DELETE:
				deleteContact(intent);
				break;
			case CONTACT_UPDATE:
				updateContact(intent);
				break;
		}
	}

	private void updateContact(Intent intent) {
		Contact contact = (Contact)intent.getSerializableExtra("datos");
		int position = adapter.getPosition(contact);
		adapter.insert(contact, position);
	}

	private void deleteContact(Intent intent) {
		ArrayList<Contact> list = (ArrayList<Contact>)intent.getSerializableExtra("datos");
		for (Contact c: list) {
			adapter.remove(c);
		}
	}

	private void addContact(Context context, Intent intent) {
		Contact contact = (Contact)intent.getSerializableExtra("datos");
		Log.d("Nombreeeee", contact.getFullName());
		Toast.makeText(context, "LLEGO NOMBRE: "+contact.getFullName(), Toast.LENGTH_SHORT).show();
		if(adapter!=null)
			adapter.add(contact);
	}
	
	public static ArrayAdapter<Contact> getAdapter(){
		return adapter;
	}
	
	

}
