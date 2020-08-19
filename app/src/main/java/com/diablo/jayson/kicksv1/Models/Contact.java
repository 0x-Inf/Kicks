package com.diablo.jayson.kicksv1.Models;

public class Contact {

    private String contactId;
    private String contactName;
    private String contactPicUrl;

    public Contact() {
    }

    public Contact(String contactId, String contactName, String contactPicUrl) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactPicUrl = contactPicUrl;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPicUrl() {
        return contactPicUrl;
    }

    public void setContactPicUrl(String contactPicUrl) {
        this.contactPicUrl = contactPicUrl;
    }
}
