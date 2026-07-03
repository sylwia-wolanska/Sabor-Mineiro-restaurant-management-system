package pjatk.mas.services;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.mas.dto.response.IngredientDTO;
import pjatk.mas.dto.response.MenuItemDTO;
import pjatk.mas.dto.response.MenuItemDetailDTO;
import pjatk.mas.mappers.MenuMapper;
import pjatk.mas.models.*;
import pjatk.mas.repositories.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
public class MenuService {

    private final EntityManager entityManager;
    private final MenuItemRepository menuItemRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientQuantityRepository ingredientQuantityRepository;
    private final MenuMapper menuMapper;

    public MenuService(MenuItemRepository menuItemRepository, IngredientRepository ingredientRepository, IngredientQuantityRepository ingredientQuantityRepository,
                       EntityManager entityManager, MenuMapper menuMapper) {
        this.menuItemRepository = menuItemRepository;
        this.ingredientRepository = ingredientRepository;
        this.ingredientQuantityRepository = ingredientQuantityRepository;
        this.entityManager = entityManager;
        this.menuMapper = menuMapper;
    }

    @Transactional(readOnly = true)
    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream().map(menuMapper::toMenuItemDTO).toList();
    }

    @Transactional(readOnly = true)
    public MenuItemDetailDTO getMenuItemDetail(Long id) {
        MenuItem item = menuItemRepository.findById(id).orElseThrow(() -> new RuntimeException("MenuItem not found: " + id));
        return menuMapper.toMenuItemDetailDTO(item);
    }

    @Transactional(readOnly = true)
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id).orElseThrow(() -> new RuntimeException("MenuItem not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<IngredientDTO> getAllIngredients() {
        return ingredientRepository.findAll().stream().map(menuMapper::toIngredientDTO).toList();
    }

    public void addIngredient(Long menuItemId, Long ingredientId, BigDecimal quantity) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new RuntimeException("MenuItem not found: " + menuItemId));
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientId));

        boolean alreadyExists = menuItem.getIngredientQuantities().stream().anyMatch(iq -> iq.getIngredient().getId().equals(ingredient.getId()));
        if (alreadyExists) {
            throw new IllegalArgumentException("Ingredient '" + ingredient.getName() + "' is already in this menu item");
        }

        IngredientQuantity iq = new IngredientQuantity(quantity, menuItem, ingredient);
        ingredientQuantityRepository.save(iq);
        entityManager.flush();
        entityManager.refresh(menuItem);
    }

    public void updateIngredientQuantity(Long ingredientQuantityId, BigDecimal newQuantity) {
        IngredientQuantity iq = ingredientQuantityRepository.findById(ingredientQuantityId).orElseThrow(() -> new RuntimeException("IngredientQuantity not found"));
        iq.setQuantity(newQuantity);
        ingredientQuantityRepository.save(iq);
    }

    public void removeIngredient(Long menuItemId, Long ingredientQuantityId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new RuntimeException("MenuItem not found: " + menuItemId));
        menuItem.getIngredientQuantities().removeIf(iq -> iq.getId().equals(ingredientQuantityId));
        menuItemRepository.save(menuItem);
    }
}