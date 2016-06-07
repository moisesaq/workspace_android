package com.moises.ormlite;

import com.moises.practices.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewContactFragment extends Fragment implements OnClickListener{

	private View view;
	private EditText fullName, phone, email, address;
	private Button btnAddContact;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.new_contact, container, false);
		setup();
		return view;
	}
	
	private void setup() {
		fullName = (EditText)view.findViewById(R.id.fullName);
		phone = (EditText)view.findViewById(R.id.phone);
		email = (EditText)view.findViewById(R.id.email);
		address = (EditText)view.findViewById(R.id.address);
		btnAddContact = (Button)view.findViewById(R.id.btnAddContact);
		btnAddContact.setOnClickListener(this);
		
		fullName.addTextChangedListener(new TextChangedListener(){
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				btnAddContact.setEnabled(!s.toString().trim().isEmpty());
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		///String msg = String.format("% is add to list", fullName.getText().toString());
		Contact contact = new Contact()
		.setFullName(fullName.getText().toString())
		.setPhone(phone.getText().toString())
		.setAddress(address.getText().toString())
		.setEmail(email.getText().toString());
		
		addContact(contact);
		
	}
	
	public void addContact(Contact contact){
		Intent intent = new Intent("listcontact");
		intent.putExtra(ContactReceiver.OPERATION, ContactReceiver.ADD);
		intent.putExtra(ContactReceiver.DATA, contact);
		getActivity().sendBroadcast(intent);
		
		Toast.makeText(getActivity(), contact.getFullName() + "added to list", Toast.LENGTH_SHORT).show();
		clear();
	}
	
	public void clear(){
		fullName.getText().clear();
		phone.getText().clear();
		email.getText().clear();
		address.getText().clear();
		fullName.requestFocus();
	}
	
}
