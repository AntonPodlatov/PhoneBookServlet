package ru.academit.phonebook.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.academit.phonebook.PhoneBook;
import ru.academit.phonebook.converter.ContactConverter;
import ru.academit.phonebook.converter.ContactValidationConverter;
import ru.academit.phonebook.model.Contact;
import ru.academit.phonebook.service.ContactValidation;
import ru.academit.phonebook.service.ContactsService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class AddContactServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 4885998722348457512L;

    private final ContactsService phoneBookService = PhoneBook.phoneBookService;
    private final ContactConverter contactConverter = PhoneBook.contactConverter;
    private final ContactValidationConverter validationConverter = PhoneBook.contactValidationConverter;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        try (BufferedReader reader = req.getReader();
             OutputStream responseStream = res.getOutputStream()
        ) {
            String json = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));

            Contact contact = contactConverter.fromJson(json);
            ContactValidation validation = phoneBookService.addContact(contact);

            if (!validation.isValid()) {
                res.setStatus(500);
            }

            String contactValidationJson = validationConverter.toJson(validation);
            responseStream.write(contactValidationJson.getBytes(StandardCharsets.UTF_8));
            responseStream.flush();
        } catch (IOException e) {
            System.out.println("error in AddContactServlet");
            e.printStackTrace();
        }
    }
}