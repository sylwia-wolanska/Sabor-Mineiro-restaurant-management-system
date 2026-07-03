package pjatk.mas.dto.response;

public record DiningTableDTO(
        Long id,
        Integer maxPeople,
        boolean occupied
) {}