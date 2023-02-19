package ru.academit.phonebook;

import ru.academit.phonebook.converter.ContactConverter;
import ru.academit.phonebook.converter.ContactValidationConverter;
import ru.academit.phonebook.converter.DeleteResponseConverter;
import ru.academit.phonebook.dao.ContactsDao;
import ru.academit.phonebook.service.ContactsService;

public class PhoneBook {
    public static ContactsDao contactsDao = new ContactsDao();

    public static ContactsService phoneBookService = new ContactsService();

    public static ContactConverter contactConverter = new ContactConverter();

    public static DeleteResponseConverter deleteResponseConverter = new DeleteResponseConverter();

    public static ContactValidationConverter contactValidationConverter = new ContactValidationConverter();
}