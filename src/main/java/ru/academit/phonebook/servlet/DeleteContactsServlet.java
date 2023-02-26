package ru.academit.phonebook.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.academit.phonebook.PhoneBook;
import ru.academit.phonebook.converter.DeleteResponseConverter;
import ru.academit.phonebook.service.ContactsService;
import ru.academit.phonebook.service.DeleteResponse;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteContactsServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 12357545322331L;

    private final ContactsService phoneBookService = PhoneBook.phoneBookService;
    private final DeleteResponseConverter deleteResponseConverter = PhoneBook.deleteResponseConverter;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        try (BufferedReader reader = req.getReader();
             OutputStream responseStream = res.getOutputStream()
        ) {
            String json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            List<Integer> ids = deleteResponseConverter.integersListFromJson(json);
            DeleteResponse deleteValidation = phoneBookService.deleteById(ids);

            if (deleteValidation.isError()) {
                res.setStatus(500);
                System.out.println(deleteValidation.getMessage());
            }

            String deleteValidationJson = deleteResponseConverter.deleteValidationToJson(deleteValidation);
            responseStream.write(deleteValidationJson.getBytes(StandardCharsets.UTF_8));
            responseStream.flush();
        } catch (Exception e) {
            System.out.println("error in DeleteContactsServlet POST: ");
            e.printStackTrace();
        }
    }
}
