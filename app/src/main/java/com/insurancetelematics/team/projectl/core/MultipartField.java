package com.insurancetelematics.team.projectl.core;

import java.io.File;

public class MultipartField {
    private String multipartName;
    private String contentType;
    private String field;
    private File file = null;
    private byte[] byteArray = null;

    public String getMultipartName() {
        return multipartName;
    }

    public MultipartField setMultipartName(String multipartName) {
        this.multipartName = multipartName;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public MultipartField setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getField() {
        return field;
    }

    public MultipartField setField(String field) {
        this.field = field;
        return this;
    }

    public File getFile() {
        return file;
    }

    public MultipartField setFile(File file) {
        this.file = file;
        return this;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public MultipartField setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
        return this;
    }
}