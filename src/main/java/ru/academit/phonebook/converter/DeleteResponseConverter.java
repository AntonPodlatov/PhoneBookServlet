package ru.academit.phonebook.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.academit.phonebook.service.DeleteResponse;

import java.lang.reflect.Type;
import java.util.List;

public class DeleteResponseConverter {
    private final Gson gson = new GsonBuilder().create();

    public String deleteValidationToJson(DeleteResponse deleteResponse) {
        return gson.toJson(deleteResponse);
    }

    public List<Integer> integersListFromJson(String json) {
        Type type = new TypeToken<List<Integer>>() {
        }.getType();

        return gson.fromJson(json, type);
    }
}