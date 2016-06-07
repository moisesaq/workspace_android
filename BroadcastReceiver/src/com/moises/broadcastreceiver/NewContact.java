package com.moises.broadcastreceiver;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewContact extends Fragment implements OnClickListener{

	private View view;
	private EditText etFullName;
	private EditText etAddress;
	private ImageButton ibtnSelectAddress;
	private Button btnAddNewContact;
	public SelectAddressClickListener callBack;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.new_contact, container, false);
		initComponents(view);
		setHasOptionsMenu(true);
		return view;
	}
	
	

	public void initComponents(View v){
		etFullName = (EditText)v.findViewById(R.id.etFullName);
		etAddress = (EditText)v.findViewById(R.id.etAddress);
		ibtnSelectAddress = (ImageButton)v.findViewById(R.id.ibtnSelectAddress);
		ibtnSelectAddress.setOnClickListener(this);
		btnAddNewContact = (Button)v.findViewById(R.id.btnAdd);
		btnAddNewContact.setOnClickListener(this);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_list_contact).setVisible(true);
		menu.findItem(R.id.action_chat).setVisible(true);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.ibtnSelectAddress:
			callBack.SelectAddressClick();
			break;
		case R.id.btnAdd:
			addNewContact(etFullName.getText().toString(), etAddress.getText().toString());
			break;
		}
	}
	
	public void addNewContact(String fullName, String address){
		Contact contact = new Contact(fullName, address);
		Intent intent = new Intent("listacontactos");
		intent.putExtra("operation", ContactReceiver.CONTACT_ADD);
		intent.putExtra("datos", contact);
		getActivity().sendBroadcast(intent);
	}
	
	public interface SelectAddressClickListener{
		public void SelectAddressClick();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			callBack = (SelectAddressClickListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" Implemented SelectAddressClickListener");
		}
	}

	
}
