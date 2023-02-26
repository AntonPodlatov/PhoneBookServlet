package ru.academit.phonebook.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.academit.phonebook.service.ContactValidation;

public class ContactValidationConverter {
    private final Gson gson = new GsonBuilder().create();

    public String toJson(ContactValidation validation) {
        return gson.toJson(validation);
    }
}