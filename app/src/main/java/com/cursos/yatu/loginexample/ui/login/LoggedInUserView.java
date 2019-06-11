package com.cursos.yatu.loginexample.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private String displayName;
    private String firsName;
    private String lastName;
    private long birthdate;
    private String email;
    private String address;
    private String notes;

    LoggedInUserView(String displayName, String firsName, String lastName,
                     long birthdate, String email, String address, String notes) {
        this.displayName = displayName;
        this.firsName = firsName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.address = address;
        this.notes= notes;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFirsName() {
        return firsName;
    }

    public void setFirsName(String firsName) {
        this.firsName = firsName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(long birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
