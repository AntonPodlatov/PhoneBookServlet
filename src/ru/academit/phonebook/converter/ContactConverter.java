package ru.academit.phonebook.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.academit.phonebook.model.Contact;

import java.util.List;


public class ContactConverter {
    private final Gson gson = new GsonBuilder().create();

    public String toJson(List<Contact> contacts) {
        return gson.toJson(contacts);
    }

    public Contact fromJson(String contactJson) {
        return gson.fromJson(contactJson, Contact.class);
    }
}
