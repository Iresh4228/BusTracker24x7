package com.example.bustracker24x7;

public class Passenger {
    public void setPas_id(String pas_id) {
        this.pas_id = pas_id;
    }

    public void setPas_name(String pas_name) {
        this.pas_name = pas_name;
    }

    public void setPas_address(String pas_address) {
        this.pas_address = pas_address;
    }

    public void setPas_email(String pas_email) {
        this.pas_email = pas_email;
    }

    public void setPas_phone(String pas_phone) {
        this.pas_phone = pas_phone;
    }

    public String getPas_id() {
        return pas_id;
    }

    public String getPas_name() {
        return pas_name;
    }

    public String getPas_address() {
        return pas_address;
    }

    public String getPas_email() {
        return pas_email;
    }

    public String getPas_phone() {
        return pas_phone;
    }

    private String pas_id;
    private String pas_name;
    private String pas_address;
    private String pas_email;
    private String pas_phone;


    public Passenger() {
    }

    public Passenger(String id, String name, String address, String email, String phone) {
        this.pas_id = id;
        this.pas_name = name;
        this.pas_address = address;
        this.pas_email = email;
        this.pas_phone = phone;
    }


}
