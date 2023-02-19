package ru.academit.phonebook.service;

import java.util.List;

public class DeleteResponse {
    private boolean isError;
    private String message;
    private List<Integer> removedContactsIds;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Integer> getRemovedContactsIds() {
        return removedContactsIds;
    }

    public void setRemovedContactsIds(List<Integer> removedContactsIds) {
        this.removedContactsIds = removedContactsIds;
    }
}
