package cz.scholz.kafka.addressbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class AddressBook {
    private Map addressBook = new HashMap<String, Address>();

    public List<Address> getAll() {
        return new ArrayList<Address>(addressBook.values());
    }

    public Address get(String id)   {
        return (Address)addressBook.getOrDefault(id, null);
    }

    public void put(Address address)    {
        addressBook.put(address.getId(), address);
    }

    public boolean exists(String id)   {
        return addressBook.containsKey(id);
    }

    public void delete(String id)   {
        if (exists(id)) {
            addressBook.remove(id);
        }
    }
}
