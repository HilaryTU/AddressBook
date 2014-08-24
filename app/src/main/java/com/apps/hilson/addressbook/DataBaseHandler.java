package com.apps.hilson.addressbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import android.widget.*;



/**
 * Created by Hilson on 8/24/2014.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactManager",
            TABLE_CONTACTS = "contacts",
            KEY_ID = "id",
            KEY_FIRSTNAME = "firstname",
            KEY_LASTNAME = "lastname",
            KEY_EMAIL = "email",
            KEY_CELLNUMBER = "cellNumber",
            KEY_CONTACTADDREESS = "contactAddress",
            KEY_IMAGEURI = "imageUri";


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE" + TABLE_CONTACTS + "(" + KEY_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_FIRSTNAME + "TEXT," + KEY_LASTNAME + "TEXT," + KEY_EMAIL + "TEXT," + KEY_CELLNUMBER + "TEXT," + KEY_CONTACTADDREESS + "TEXT," + KEY_IMAGEURI + "TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS" + TABLE_CONTACTS);
        onCreate(db);
    }

    public void createContact(AddressContacts contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_FIRSTNAME, contact.get_firstName());
        values.put(KEY_LASTNAME, contact.get_lastName());
        values.put(KEY_EMAIL, contact.get_emailAddress());
        values.put(KEY_CELLNUMBER, contact.get_cellNumber());
        values.put(KEY_CONTACTADDREESS, contact.get_contactAddress());
        values.put(KEY_IMAGEURI,contact.get_imageUri().toString());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public  AddressContacts getContact(int id){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS,new String[]{KEY_ID,KEY_FIRSTNAME,KEY_LASTNAME,KEY_EMAIL,KEY_CELLNUMBER,KEY_CONTACTADDREESS,KEY_IMAGEURI},
                KEY_ID +"= ?",new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor!=null)
            cursor.moveToFirst();
        AddressContacts contact = new AddressContacts(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),
                                                                   cursor.getString(4),cursor.getString(5), Uri.parse(cursor.getString(6)));
        db.close();
        cursor.close();
        return  contact;

    }

    public void deleteContact(AddressContacts contact){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CONTACTS,KEY_ID +"=?",new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public int getContactCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FRPM " + TABLE_CONTACTS, null);
        int count= cursor.getCount();

        db.close();
        cursor.close();

        return count;

    }

    public  int updateContact(AddressContacts contact){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_FIRSTNAME, contact.get_firstName());
        values.put(KEY_LASTNAME, contact.get_lastName());
        values.put(KEY_EMAIL, contact.get_emailAddress());
        values.put(KEY_CELLNUMBER, contact.get_cellNumber());
        values.put(KEY_CONTACTADDREESS, contact.get_contactAddress());
        values.put(KEY_IMAGEURI,contact.get_imageUri().toString());
        int rowsaffected =  db.update(TABLE_CONTACTS,values,KEY_ID + "=?",new String[]{String.valueOf(contact.getId())});
        db.close();
        return rowsaffected;
    }
    public List<AddressContacts> getAllContacts(){


          List<AddressContacts> contacts = new ArrayList<AddressContacts>();
          SQLiteDatabase db = getWritableDatabase();
          Cursor cursor = db.rawQuery("SELECT * FROM" + TABLE_CONTACTS, null);

          if (cursor.moveToFirst()) {
              AddressContacts contact;
              do {
                  contacts.add(new AddressContacts(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                          cursor.getString(4), cursor.getString(5), Uri.parse(cursor.getString(6))));

              } while (cursor.moveToNext());
          }
          cursor.close();
          db.close();
          return contacts;


    }
}