package ru.academit.phonebook.dao;

import ru.academit.phonebook.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class ContactsDao {
    private final List<Contact> contacts = new ArrayList<>();
    private final AtomicInteger idSequence = new AtomicInteger(0);

    public ContactsDao() {
        contacts.addAll(List.of(
                new Contact(getNewId(), "Leonid", "Stepanov", "9823236723"),
                new Contact(getNewId(), "Massimo", "Magrini", "1239812312"),
                new Contact(getNewId(), "Marcos", "Ortega", "9143236723"),
                new Contact(getNewId(), "Rodger", "Williams", "12348934555"),
                new Contact(getNewId(), "Gomer", "Gram", "12354093344")));
    }

    private int getNewId() {
        return idSequence.addAndGet(1);
    }

    public List<Contact> getAll() {
        return contacts;
    }

    public List<Integer> delete(List<Integer> ids) {
        List<Integer> removedContactsIds = new ArrayList<>();

        contacts.removeIf(contact -> {
            if (ids.contains(contact.getId())) {
                removedContactsIds.add(contact.getId());
                return true;
            }
            return false;
        });

        return removedContactsIds;
    }

    public List<Contact> getByTerm(String term) {
        if (term.length() == 0) {
            return contacts;
        }

        Predicate<Contact> contactAnyFieldContainsTerm = contact ->
                        contact.getFirstName().toLowerCase().contains(term.toLowerCase()) ||
                        contact.getLastName().toLowerCase().contains(term.toLowerCase()) ||
                        contact.getPhone().toLowerCase().contains(term.toLowerCase());

        return contacts.stream().filter(contactAnyFieldContainsTerm).toList();
    }

    public void addOne(Contact contact) {
        contact.setId(getNewId());
        contacts.add(contact);
    }
}