package com.wordpress.michaelkyazze.codeperspective102;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.wordpress.michaelkyazze.codeperspective101.R;
import com.wordpress.michaelkyazze.codeperspective102.MongoLab.GetContactsAsyncTask;

/**
 * This activity retrieves the mongolab contacts and displays them in a listview.
 * @author KYAZZE MICHAEL
 *
 */
public class ViewContactsActivity extends ListActivity{
	ArrayList<MyContact> returnValues = new ArrayList<MyContact>();
	ArrayList<String> listItems = new ArrayList<String>();
	String valueTOUpdate_id;
	String valueTOUpdate_fname;
	String valueTOUpdate_lname;
	String valueTOUpdate_phone;
	String valueTOUpdate_email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view_contacts);
		
		//Get your cloud contacts
		GetContactsAsyncTask task = new GetContactsAsyncTask();
		try {
			returnValues = task.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		for(MyContact x: returnValues){
			
			listItems.add(x.getFirst_name());
		}
		
		setListAdapter(new ArrayAdapter<String>(this, 
	              android.R.layout.simple_list_item_1, listItems));

		
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		String selectedValue = (String) getListAdapter().getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
        selectedContact(selectedValue);
           
        Bundle dataBundle = new Bundle();
        dataBundle.putString("_id", valueTOUpdate_id);
        dataBundle.putString("first_name", valueTOUpdate_fname);
        dataBundle.putString("last_name", valueTOUpdate_lname);
        dataBundle.putString("phone", valueTOUpdate_phone);
        dataBundle.putString("email", valueTOUpdate_email);
        Intent moreDetailsIntent = new Intent(this,UpdateContactsActivity.class);
        moreDetailsIntent.putExtras(dataBundle);
        startActivity(moreDetailsIntent);	
	}
	
	/*
	 * Retrieves the full details of a selected contact. 
	 * The details are then passed onto the Update Contacts Record. 
	 * 
	 * This is a quick way for demo purposes. 
	 * You should consider storing this data in a database, shared preferences or text file
	 */
	public void selectedContact(String selectedValue){
		for(MyContact x: returnValues){
			if(selectedValue.contains(x.getFirst_name())){
				valueTOUpdate_id = x.getDoc_id();
				valueTOUpdate_fname = x.getFirst_name();
				valueTOUpdate_lname = x.getLast_name();
				valueTOUpdate_phone = x.getPhone();
				valueTOUpdate_email = x.getEmail();
			}		
		}
		
	}

	
}
