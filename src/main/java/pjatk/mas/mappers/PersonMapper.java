package pjatk.mas.mappers;

import org.springframework.stereotype.Component;
import pjatk.mas.dto.response.PersonDTO;
import pjatk.mas.dto.response.ClientDTO;
import pjatk.mas.dto.response.EmployeeSummaryDTO;
import pjatk.mas.dto.response.EmployeeDetailDTO;
import pjatk.mas.models.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonMapper {

    public PersonDTO toPersonDTO(Person person) {
        return new PersonDTO(
                person.getId(),
                person.getName(),
                person.getSurname(),
                person.getRole()
        );
    }

    public ClientDTO toClientDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getName(),
                client.getSurname(),
                client.getPhoneNumber(),
                client.getEmailAddress()
        );
    }

    public EmployeeSummaryDTO toEmployeeSummaryDTO(Employee employee) {
        return new EmployeeSummaryDTO(
                employee.getId(),
                employee.getName(),
                employee.getSurname(),
                employee.getRole(),
                employee.getSalaryPerHour()
        );
    }

    public EmployeeDetailDTO toEmployeeDetailDTO(Employee employee) {
        List<String> extraInfo = new ArrayList<>();

        if (employee instanceof Chef chef) {
            extraInfo.addAll(chef.getCookingCourses());
        } else if (employee instanceof DeliveryDriver driver) {
            extraInfo.add("License: " + driver.getDrivingLicenseNumber());
            driver.getVehicles().forEach(v -> extraInfo.add("Vehicle: " + v.getLicensePlate() + " (" + v.getType() + ")"));
        } else if (employee instanceof Waiter waiter) {
            waiter.getDiningTables().forEach(t -> extraInfo.add("Table #" + t.getId() + " (max " + t.getMaxPeople() + " people)"));
        }

        return new EmployeeDetailDTO(
                employee.getId(),
                employee.getName(),
                employee.getSurname(),
                employee.getRole(),
                employee.getSalaryPerHour(),
                extraInfo
        );
    }
}