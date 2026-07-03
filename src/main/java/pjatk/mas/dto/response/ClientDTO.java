package pjatk.mas.dto.response;

public record ClientDTO(
        Long id,
        String name,
        String surname,
        String phoneNumber,
        String emailAddress
) {}