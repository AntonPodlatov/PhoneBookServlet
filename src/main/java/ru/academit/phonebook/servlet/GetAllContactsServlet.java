package ru.academit.phonebook.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.academit.phonebook.PhoneBook;
import ru.academit.phonebook.converter.ContactConverter;
import ru.academit.phonebook.model.Contact;
import ru.academit.phonebook.service.ContactsService;

import java.io.OutputStream;
import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetAllContactsServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 12342323473735433L;

    private final ContactsService phoneBookService = PhoneBook.phoneBookService;
    private final ContactConverter contactConverter = PhoneBook.contactConverter;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        try (OutputStream out = res.getOutputStream()) {
            String filter = req.getParameter("filter");
            List<Contact> contacts;

            if (filter != null) {
                contacts = phoneBookService.getByTerm(filter);
            } else {
                contacts = phoneBookService.getAllContacts();
            }

            String contactsJson = contactConverter.toJson(contacts);
            out.write(contactsJson.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (Exception e) {
            System.out.println("error in GetAllContactsServlet GET: ");
            e.printStackTrace();
        }
    }
}