function Contact(firstName, lastName, phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.checked = false;
    this.shown = true;
}

new Vue({
    el: "#app",

    data: {
        validation: false,
        serverValidation: false,
        serverError: "",
        firstName: "",
        lastName: "",
        phone: "",
        rows: [],
        filterValue: "",
        isAllChecked: false,
        contactsForRemove: [],
        warningText: ""
    },

    methods: {
        checkAllCheckboxes() {
            var isAllChecked = this.isAllChecked;
            this.rows.forEach(row => row.checked = isAllChecked);
        },

        contactToString(contact) {
            return contact.firstName + " " + contact.lastName + ", " + contact.phone;
        },

        convertContactList(contactListFromServer) {
            return contactListFromServer.map((contact, i) => ({
                id: contact.id,
                firstName: contact.firstName,
                lastName: contact.lastName,
                phone: contact.phone,
                checked: false,
                shown: true,
                number: i + 1
            }));
        },

        addContact() {
            if (this.hasError) {
                this.validation = true;
                this.serverValidation = false;
                return;
            }

            var self = this;
            var contact = new Contact(this.firstName, this.lastName, this.phone);

            $.ajax({
                type: "POST", url: "/phonebook/contacts/add", data: JSON.stringify(contact)
            }).done(function () {
                self.serverValidation = false;
            }).fail(function (ajaxRequest) {
                var contactValidation = JSON.parse(ajaxRequest.responseText);
                self.serverError = contactValidation.error;
                self.serverValidation = true;
            }).always(function () {
                self.loadData();
            });

            self.firstName = "";
            self.lastName = "";
            self.phone = "";
            self.validation = false;
        },

        loadData(applyFilter = false) {
            var path = "/phonebook/contacts";

            if (applyFilter) {
                path += "?filter=" + this.filterValue;
            }

            $.get(path).done(response =>
                this.rows = this.convertContactList(JSON.parse(response)));
        },

        filter() {
            this.loadData(true);
        },

        resetFilter() {
            this.filterValue = "";
            this.loadData();
        },

        remove() {
            var contactsIdsForRemove = this.contactsForRemove
                .map(checkedRow => checkedRow.id);

            $.ajax({
                type: "POST",
                url: "/phonebook/contacts/delete",
                data: JSON.stringify(contactsIdsForRemove)
            }).done(ajaxRequest => {
                    var response = JSON.parse(ajaxRequest);
                    if (response.message) {
                        this.showWarning(response.message);
                    }
                    return this.rows = this.rows.filter(row =>
                        !response.removedContactsIds.includes(row.id));
                }
            ).fail(ajaxRequest => {
                var response = JSON.parse(ajaxRequest.responseText);
                if (response.message) {
                    this.showWarning(response.message);
                }
            }).always(() => this.contactsForRemove = [])
        },

        showRemovingDialog(row = null) {
            if (row === null) {
                var checkedRows = this.rows.filter(row => row.checked === true);

                if (checkedRows.length === 0) {
                    this.showWarning("Не выбрано ни одного контакта");
                    return;
                }

                this.contactsForRemove = checkedRows;
            } else {
                this.contactsForRemove = [row];
            }

            var modal = new bootstrap.Modal(this.$refs.modalWindow, {backdrop: true, keyboard: true, focus: true});
            modal.show();
        },

        showWarning(warningTest) {
            this.warningText = warningTest;
            console.log(this.warningText)
            var warning = new bootstrap.Modal(this.$refs.warningWindow, {backdrop: true, keyboard: true, focus: true});
            warning.show();
        },

        clearWarningText() {
            this.warningText = "";
        },

        closeAndUpdate() {
            this.loadData(false);
            this.clearWarningText();
        }
    },

    computed: {
        contactAlreadyExists() {
            return this.rows.some(c => c.phone === this.phone);
        },

        firstNameError() {
            return this.firstName ? {
                message: "",
                error: false
            } : {
                message: "Поле Имя должно быть заполнено.",
                error: true
            };
        },

        lastNameError() {
            return !this.lastName ? {
                message: "Поле Фамилия должно быть заполнено.",
                error: true
            } : {
                message: "",
                error: false
            };
        },

        phoneError() {
            return !this.phone ? {
                message: "Поле Телефон должно быть заполнено.",
                error: true
            } : this.contactAlreadyExists ? {
                message: "Номер телефона не должен дублировать другие номера в телефонной книге.",
                error: true
            } : {
                message: "",
                error: false
            };
        },

        hasError() {
            return this.lastNameError.error || this.firstNameError.error || this.phoneError.error;
        }
    },

    created() {
        this.loadData();
    }
});