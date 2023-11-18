package com.example.lab7.entities;

import java.util.ArrayList;
import java.util.List;

public class Gestor {


    private String correo;
    private String ussername;
    private List<String> serviceList = new ArrayList<>();

    public Gestor(String correo, String ussername, List<String> serviceList) {
        this.correo = correo;
        this.ussername = ussername;
        this.serviceList = serviceList;
    }

    public Gestor(){

    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUssername() {
        return ussername;
    }

    public void setUssername(String ussername) {
        this.ussername = ussername;
    }

    public List<String> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<String> serviceList) {
        this.serviceList = serviceList;
    }
}
