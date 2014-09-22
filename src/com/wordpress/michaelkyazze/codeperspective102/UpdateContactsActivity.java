package com.wordpress.michaelkyazze.codeperspective102;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.wordpress.michaelkyazze.codeperspective101.R;
import com.wordpress.michaelkyazze.codeperspective102.MongoLab.QueryBuilder;
/**
 * This activity updates a given contact record
 * @author KYAZZE MICHAEL
 *
 */
public class UpdateContactsActivity extends Activity {

	EditText editText_last_name;
	EditText editText_phone;
	EditText editText_email;
	EditText editText_fname;
	
	String _id;
    String f_name;
    String l_name;
    String phone;
    String email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		editText_fname = (EditText) findViewById(R.id.editText_fname);
		editText_last_name = (EditText) findViewById(R.id.editText_last_name);
		editText_email = (EditText) findViewById(R.id.editText_email);
		editText_phone = (EditText) findViewById(R.id.editText_phone);
		
		//Obtain details of the clicked contact
	    Bundle getBundle = null;
	    getBundle = this.getIntent().getExtras();
	    _id = getBundle.getString("_id");
	    f_name = getBundle.getString("first_name");
	    l_name = getBundle.getString("last_name");
	    phone = getBundle.getString("phone");
	    email = getBundle.getString("email");

	    editText_fname.setText(f_name);
	    editText_last_name.setText(l_name);
	    editText_email.setText(email);
	    editText_phone.setText(phone);	
	}
	
	/**
	 * Method that updates a given cloud contact
	 * @param v
	 * @throws UnknownHostException
	 */
	public void updateContact(View v) throws UnknownHostException {
		MyContact contact = new MyContact();
		contact.setDoc_id(_id);
		contact.setFirst_name(editText_fname.getText().toString());
		contact.setLast_name(editText_last_name.getText().toString());
		contact.setEmail(editText_email.getText().toString());
		contact.setPhone(editText_phone.getText().toString());
		MongoLabUpdateContact tsk = new MongoLabUpdateContact();
		tsk.execute(contact);	
		Intent i = new Intent(this, ViewContactsActivity.class);
		startActivity(i);
	}
	
	/**
	 * AsyncTask to update a given contact
	 * @author KYAZZE MICHAEL
	 *
	 */
	final class MongoLabUpdateContact extends AsyncTask<Object, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Object... params) {
			MyContact contact = (MyContact) params[0];
		
			try {

				QueryBuilder qb = new QueryBuilder();
				URL url = new URL(qb.buildContactsUpdateURL(contact.getDoc_id()));
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("PUT");
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type",
						"application/json");
				connection.setRequestProperty("Accept", "application/json");
				
				OutputStreamWriter osw = new OutputStreamWriter(
						connection.getOutputStream());
				
				osw.write(qb.setContactData(contact)); 
				osw.flush();
				osw.close();
				if(connection.getResponseCode() <205)
				{

					return true;
				}
				else
				{
					return false;
					
				}
				
			} catch (Exception e) {
				e.getMessage();
				return false;
				
			}

		}
		
	}

}
