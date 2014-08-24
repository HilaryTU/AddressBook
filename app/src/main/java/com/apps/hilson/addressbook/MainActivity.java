package com.apps.hilson.addressbook;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import java.util.*;
import android.view.*;
import android.net.*;


public class MainActivity extends Activity {

    List<AddressContacts> bookContacts = new ArrayList<AddressContacts>();
    ListView contactListview;
    EditText firstNameTxt;
    EditText lastNameTxt;
    EditText emailTxt;
    EditText addressTxt;
    EditText cellPhoneTxt;

    ImageView cntctImmgView;
    ImageView adCntctImg;
    Button addBtn;
    TabHost tabHost;
    Uri imageUri = Uri.parse("android.resource://com.apps.hilson.addressbook/drawable-hdpi/nnnn.png");
    DataBaseHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNameTxt = (EditText) findViewById(R.id.txtFirstname);
        lastNameTxt = (EditText) findViewById(R.id.txtLastname);
        emailTxt = (EditText) findViewById(R.id.txtEmailAddress);
        addressTxt = (EditText) findViewById(R.id.txtAddress);
        contactListview = (ListView) findViewById(R.id.contactLv);
        cellPhoneTxt = (EditText) findViewById(R.id.txtPhoneNumber);

        cntctImmgView = (ImageView) findViewById(R.id.contactImage);
        addBtn = (Button) findViewById(R.id.btnAdd);
        tabHost = (TabHost) findViewById(R.id.tabHost);
        dbHandler = new DataBaseHandler(getApplicationContext());

        tabHost.setup();//setting up the tab

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");//creating the create contact tab
        tabSpec.setContent(R.id.createContact);
        tabSpec.setIndicator("Create Contact");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("list");//creating the contact list tab
        tabSpec.setContent(R.id.contactList);
        tabSpec.setIndicator("Contact List");
        tabHost.addTab(tabSpec);



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bookContacts.add(new AddressContacts(0,firstNameTxt.getText().toString(), lastNameTxt.getText().toString(), emailTxt.getText().toString(), cellPhoneTxt.getText().toString(), addressTxt.getText().toString(), imageUri));//Passing Variables into the method
                AddressContacts myContacts = new  AddressContacts(dbHandler.getContactCount(),String.valueOf(firstNameTxt.getText()), String.valueOf(lastNameTxt.getText()), String.valueOf(emailTxt.getText()), String.valueOf(cellPhoneTxt.getText()), String.valueOf(addressTxt.getText()),imageUri);//Passing Variables into the method

                // populateList();
                if(!contactExits(myContacts)) {
                    dbHandler.createContact(myContacts);
                    bookContacts.add(myContacts);

                    Toast.makeText(getApplicationContext(), String.valueOf(firstNameTxt.getText()) + " has been added to your contacts!", Toast.LENGTH_SHORT).show();
                    firstNameTxt.setText("");
                    lastNameTxt.setText("");
                    emailTxt.setText("");
                    cellPhoneTxt.setText("");
                    addressTxt.setText("");//Clear Textfields

                    return;
                }
                Toast.makeText(getApplicationContext(), String.valueOf(firstNameTxt.getText()) + " already exist please use another name!", Toast.LENGTH_SHORT).show();
            }
        });

        firstNameTxt.addTextChangedListener(new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                    addBtn.setEnabled(String.valueOf(firstNameTxt.getText()).trim().length()>0);

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {

                                                }
                                            }
        );

        cntctImmgView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"),1);
            }
        });

        if(dbHandler.getContactCount()!=0)
            bookContacts.addAll(dbHandler.getAllContacts());
            populateList();






    }

    public boolean contactExits(AddressContacts contact){
        String name = contact.get_firstName();
        int contactCount = bookContacts.size();

        for(int i =0; i<contactCount;i++){
            if(name.compareToIgnoreCase(bookContacts.get(i).get_firstName())== 0)
                return true;
        }
        return false;
    }

    public void onActivityResult(int reqCode, int resCode, Intent data)
    {
        if (resCode == RESULT_OK)
        {
            if(reqCode == 1) {
                imageUri = data.getData();
                cntctImmgView.setImageURI(data.getData());
            }
        }
    }


    private void populateList()
    {
        ArrayAdapter<AddressContacts> adapter = new ContactListAdapter();
        contactListview.setAdapter(adapter);
    }
    private class ContactListAdapter extends ArrayAdapter<AddressContacts>{
        public ContactListAdapter ()
        {
            super(MainActivity.this, R.layout.listview_item, bookContacts);//connecting to the view layout
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)//creating a fixed position of a specific contact
        {
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item,parent,false);

                AddressContacts currentContact = bookContacts.get(position);//getting details of specific contact

                TextView fName = (TextView) view.findViewById(R.id.contactName);
                fName.setText(currentContact.get_firstName());

                TextView lName = (TextView) view.findViewById(R.id.lsLastName);
                lName.setText(currentContact.get_lastName());

                TextView eAddress = (TextView) view.findViewById(R.id.lsEmailAddress);
                eAddress.setText(currentContact.get_emailAddress());


                TextView cPhone = (TextView) view.findViewById(R.id.lsCellNumber);
                cPhone.setText(currentContact.get_cellNumber());

                TextView hAddress = (TextView) view.findViewById(R.id.lsHomeAddress);
                hAddress.setText(currentContact.get_contactAddress());

                adCntctImg = (ImageView) view.findViewById(R.id.lvContactImg);
                adCntctImg.setImageURI(currentContact.get_imageUri());


            return view;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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
