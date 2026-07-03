package pjatk.mas.session;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class OrderWizardSession implements Serializable {

    private Long clientId;
    private Map<Long, Integer> selectedItems = new LinkedHashMap<>();
    private boolean pickupAtRestaurant;
    private String street;
    private String buildingNumber;
    private Integer apartmentNumber;
    private LocalTime hourOfPickup;

    public void addItem(Long menuItemId, int quantity) {
        selectedItems.merge(menuItemId, quantity, Integer::sum);
    }

    public void removeItem(Long menuItemId) {
        selectedItems.remove(menuItemId);
    }

    public void updateQuantity(Long menuItemId, int quantity) {
        if (quantity <= 0) removeItem(menuItemId);
        else selectedItems.put(menuItemId, quantity);
    }

    public void clearItems() {
        selectedItems.clear();
    }

    public boolean hasItems() {
        return !selectedItems.isEmpty();
    }

    public void reset() {
        selectedItems.clear();
        pickupAtRestaurant = false;
        street = null;
        buildingNumber = null;
        apartmentNumber = null;
        hourOfPickup = null;
    }
}