package pjatk.mas.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.mas.dto.request.RegistrationRequestDTO;
import pjatk.mas.models.Client;
import pjatk.mas.models.Person;
import pjatk.mas.repositories.ClientRepository;
import pjatk.mas.repositories.PersonRepository;

import java.util.Optional;

@Service
@Transactional
public class AuthenticationService {

    private final PersonRepository personRepository;
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationService(PersonRepository personRepository, ClientRepository clientRepository, BCryptPasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<Person> login(String username, String rawPassword) {
        return personRepository.findByUsername(username).filter(person -> passwordEncoder.matches(rawPassword, person.getPassword()));
    }

    public Client register(RegistrationRequestDTO form) {
        if (personRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("Username '" + form.getUsername() + "' is already taken");
        }
        if (!form.passwordsMatch()) {
            throw new IllegalArgumentException("Passwords don't match");
        }
        if (clientRepository.findByEmailAddress(form.getEmailAddress()).isPresent()) {
            throw new IllegalArgumentException("An account with this email address already exists");
        }
        if (form.getName() == null || form.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (form.getSurname() == null || form.getSurname().isBlank()) {
            throw new IllegalArgumentException("Surname is required");
        }
        if (form.getPhoneNumber() == null || form.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        Client client = new Client(form.getName(), form.getSurname(), form.getPhoneNumber(), form.getEmailAddress());
        client.setUsername(form.getUsername());
        client.setPassword(passwordEncoder.encode(form.getPassword()));
        return clientRepository.save(client);
    }
}