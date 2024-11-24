import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    private ShoppingCart cart;
    private static final String ITEM_ID_1 = "ITEM001";
    private static final String ITEM_ID_2 = "ITEM002";
    private static final double ITEM_PRICE_1 = 10.0;
    private static final double ITEM_PRICE_2 = 20.0;
    private static final double DELTA = 0.001;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
    }

    @Test
    @DisplayName("Should add item with valid quantity")
    void addItem_ValidQuantity_Success() {
        cart.addItem(ITEM_ID_1, 2);
        assertEquals(ITEM_PRICE_1 * 2, cart.getTotal(), DELTA);
    }

    @Test
    @DisplayName("Should throw exception when adding item with zero quantity")
    void addItem_ZeroQuantity_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            cart.addItem(ITEM_ID_1, 0)
        );
        assertEquals("Quantity must be greater than zero", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when adding item with negative quantity")
    void addItem_NegativeQuantity_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            cart.addItem(ITEM_ID_1, -1)
        );
        assertEquals("Quantity must be greater than zero", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when adding null item ID")
    void addItem_NullItemId_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
            cart.addItem(null, 1)
        );
    }

    @Test
    @DisplayName("Should update quantity when adding existing item")
    void addItem_ExistingItem_UpdatesQuantity() {
        cart.addItem(ITEM_ID_1, 1);
        cart.addItem(ITEM_ID_1, 2);
        assertEquals(ITEM_PRICE_1 * 3, cart.getTotal(), DELTA);
    }

    @Test
    @DisplayName("Should remove existing item")
    void removeItem_ExistingItem_Success() {
        cart.addItem(ITEM_ID_1, 1);
        cart.removeItem(ITEM_ID_1);
        assertEquals(0.0, cart.getTotal(), DELTA);
    }

    @Test
    @DisplayName("Should throw exception when removing non-existent item")
    void removeItem_NonExistentItem_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            cart.removeItem("NONEXISTENT")
        );
    }

    @Test
    @DisplayName("Should throw exception when removing null item ID")
    void removeItem_NullItemId_ThrowsException() {
        assertThrows(NullPointerException.class, () ->
            cart.removeItem(null)
        );
    }

    @Test
    @DisplayName("Should calculate correct total for multiple items")
    void getTotal_MultipleItems_CorrectTotal() {
        cart.addItem(ITEM_ID_1, 2); // 2 * 10.0 = 20.0
        cart.addItem(ITEM_ID_2, 1); // 1 * 20.0 = 20.0
        assertEquals(40.0, cart.getTotal(), DELTA);
    }

    @Test
    @DisplayName("Should return zero total for empty cart")
    void getTotal_EmptyCart_ReturnsZero() {
        assertEquals(0.0, cart.getTotal(), DELTA);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 50.0, 100.0})
    @DisplayName("Should apply valid discount percentages")
    void applyDiscount_ValidPercentage_Success(double discount) {
        cart.addItem(ITEM_ID_1, 1); // 10.0
        cart.applyDiscount(discount);
        double expectedTotal = ITEM_PRICE_1 * (1 - discount/100);
        assertEquals(expectedTotal, cart.getTotal(), DELTA);
    }

    @Test
    @DisplayName("Should throw exception for negative discount")
    void applyDiscount_NegativePercentage_ThrowsException() {
        cart.addItem(ITEM_ID_1, 1);
        assertThrows(IllegalArgumentException.class, () ->
            cart.applyDiscount(-10.0)
        );
    }

    @Test
    @DisplayName("Should throw exception for discount over 100%")
    void applyDiscount_OverHundredPercentage_ThrowsException() {
        cart.addItem(ITEM_ID_1, 1);
        assertThrows(IllegalArgumentException.class, () ->
            cart.applyDiscount(110.0)
        );
    }

    @Test
    @DisplayName("Should handle applying discount to empty cart")
    void applyDiscount_EmptyCart_Success() {
        cart.applyDiscount(10.0);
        assertEquals(0.0, cart.getTotal(), DELTA);
    }

    @Test
    @DisplayName("Should maintain correct total after multiple operations")
    void complexScenario_MultipleOperations_CorrectTotal() {
        cart.addItem(ITEM_ID_1, 2);  // 20.0
        cart.addItem(ITEM_ID_2, 1);  // +20.0 = 40.0
        cart.removeItem(ITEM_ID_1);   // -20.0 = 20.0
        cart.applyDiscount(10.0);     // *0.9 = 18.0
        assertEquals(18.0, cart.getTotal(), DELTA);
    }
}
