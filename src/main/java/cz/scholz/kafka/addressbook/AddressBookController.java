package cz.scholz.kafka.addressbook;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/addressbook")
public class AddressBookController {
    final Logger LOG = LoggerFactory.getLogger(AddressBookController.class);

    @Autowired
    AddressBook addressBook;

    @Autowired
    KafkaProducer kafkaProducer;

    @GetMapping
    public List<Address> getAll(){
        LOG.info("Received GET for all");
        return addressBook.getAll();
    }

    @GetMapping("/{id}")
    public Address get(@PathVariable String id){
        LOG.info("Received GET for {}", id);

        if (addressBook.exists(id)) {
            return addressBook.get(id);
        }
        else {
            throw new ResourceNotFoundException();
        }
    }

    @PostMapping
    public Address post(@RequestBody Address address){
        LOG.info("Received POST request {}", address);
        //addressBook.put(address);
        kafkaProducer.sendMessage(address);
        return address;
    }

    @PutMapping("/{id}")
    public Address put(@PathVariable String id, @RequestBody Address address){
        if (!addressBook.exists(id))    {
            throw new ResourceNotFoundException();
        } else if (address.getId().equals(id)) {
            //addressBook.put(address);
            kafkaProducer.sendMessage(address);
            return address;
        } else    {
            throw new ConflictException();
        }
    }

    @DeleteMapping("/{id}")
    public Address delete(@PathVariable String id){
        if (!addressBook.exists(id))    {
            throw new ResourceNotFoundException();
        } else    {
            Address address = addressBook.get(id);
            //addressBook.delete(id);
            kafkaProducer.deleteMessage(id);
            return address;
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    public class ConflictException extends RuntimeException {
    }
}
