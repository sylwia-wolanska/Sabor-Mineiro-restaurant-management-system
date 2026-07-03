package pjatk.mas.session;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReservationWizardSession implements Serializable {

    private Long clientId;
    private LocalDateTime dateTime;
    private Integer numberOfPeople;
    private Long foundTableId;

    public void reset() {
        dateTime = null;
        numberOfPeople = null;
        foundTableId = null;
    }
}