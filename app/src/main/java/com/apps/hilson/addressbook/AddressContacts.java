package com.apps.hilson.addressbook;
import android.net.*;
/**
 * Created by Hilson on 8/23/2014.
 */
public class AddressContacts {

    private int _id;
    private String _firstName, _lastName, _emailAddress,_cellNumber, _contactAddress;
    private Uri _imageUri;



    public AddressContacts(int id,String firstName,String lastName, String emailAddress,String cellNumber,String contactAddress, Uri imageUri)
    {
        _id = id;
        _firstName = firstName;
        _lastName = lastName;
        _emailAddress = emailAddress;
        _cellNumber = cellNumber;
        _contactAddress = contactAddress;
        _imageUri = imageUri;
    }

    public int getId()
    { return _id;
    }
    public String get_firstName()
    {
        return _firstName;
    }

    public String get_lastName()
    {
        return _lastName;
    }

    public String get_emailAddress()
    {
        return _emailAddress;
    }

    public String get_cellNumber()
    {
        return _cellNumber;
    }
    public String get_contactAddress()
    {
        return _contactAddress;
    }

    public Uri get_imageUri() {
        return _imageUri;
    }
}
