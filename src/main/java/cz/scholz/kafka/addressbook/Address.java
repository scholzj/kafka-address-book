package cz.scholz.kafka.addressbook;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Address {
    private String id;
    private String name;
    private String address;

    public Address(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Address(String name, String address) throws UnsupportedEncodingException {
        this.id = getIdFromHash(name);
        this.name = name;
        this.address = address;
    }

    public Address() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) throws UnsupportedEncodingException {
        this.name = name;
        this.id = getIdFromHash(name);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String getIdFromHash(String id) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(name.getBytes("UTF-8"));
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
