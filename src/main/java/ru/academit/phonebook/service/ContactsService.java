package ru.academit.phonebook.service;

import ru.academit.phonebook.PhoneBook;
import ru.academit.phonebook.dao.ContactsDao;
import ru.academit.phonebook.model.Contact;

import java.util.List;

public class ContactsService {
    private final ContactsDao dao = PhoneBook.contactsDao;

    private boolean isContactWithPhoneExists(String phone) {
        return dao.getAll().stream()
                .anyMatch(contact -> contact.getPhone().equals(phone));
    }

    public ContactValidation validateContact(Contact contact) {
        ContactValidation validation = new ContactValidation();
        validation.setValid(true);

        if (contact.getFirstName().isEmpty()) {
            validation.setValid(false);
            validation.setErrorMessage("Поле Имя должно быть заполнено");
            return validation;
        }

        if (contact.getLastName().isEmpty()) {
            validation.setValid(false);
            validation.setErrorMessage("Поле Фамилия должно быть заполнено");
            return validation;
        }

        if (contact.getPhone().isEmpty()) {
            validation.setValid(false);
            validation.setErrorMessage("Поле Телефон должно быть заполнено");
            return validation;
        }

        if (isContactWithPhoneExists(contact.getPhone())) {
            validation.setValid(false);
            validation.setErrorMessage("Номер телефона не должен дублировать другие номера в телефонной книге.");
            return validation;
        }

        return validation;
    }

    public ContactValidation addContact(Contact contact) {
        ContactValidation validation = validateContact(contact);
        if (validation.isValid()) {
            dao.addOne(contact);
        }
        return validation;
    }

    public List<Contact> getAllContacts() {
        return dao.getAll();
    }

    public List<Contact> getByTerm(String term) {
        return dao.getByTerm(term);
    }

    public DeleteResponse deleteById(List<Integer> ids) {
        DeleteResponse deleteValidation = new DeleteResponse();

        if (ids == null) {
            deleteValidation.setError(true);
            deleteValidation.setMessage("Нужно передать список id для удаления");
            return deleteValidation;
        }

        if (ids.size() == 0) {
            deleteValidation.setError(true);
            deleteValidation.setMessage("Переданный список пуст");
            return deleteValidation;
        }

        List<Integer> removedContactsIds = dao.delete(ids);

        if (removedContactsIds.size() == 0) {
            deleteValidation.setError(true);
            deleteValidation.setMessage("Не удалено ничего. Таких контактов уже нет.");
            return deleteValidation;
        }

        if (removedContactsIds.size() != ids.size()) {
            deleteValidation.setMessage("Удалены не все контакты. Некоторых из списка нe уже было.");
        }

        deleteValidation.setRemovedContactsIds(removedContactsIds);
        deleteValidation.setError(false);
        return deleteValidation;
    }
}