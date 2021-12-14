package com.example.tallyandreport;

public class UploadEntry {
    String date;
    String mode;
    String source_name;
    String sub_source_name;
    String description;
    String credit;
    String debit;
    String addedBy;

    public UploadEntry() {
    }

    public UploadEntry(String date, String mode, String source_name, String sub_source_name,
                       String description, String credit, String debit, String addedBy) {
        this.date = date;
        this.mode = mode;
        this.source_name = source_name;
        this.sub_source_name = sub_source_name;
        this.description = description;
        this.credit = credit;
        this.debit = debit;
        this.addedBy = addedBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getSub_source_name() {
        return sub_source_name;
    }

    public void setSub_source_name(String sub_source_name) {
        this.sub_source_name = sub_source_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }
}
