import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        shoppingCart = new ShoppingCart();
    }

    @Test
    void testAddItem_validItem() {
        shoppingCart.addItem("item1", 2);
        assertEquals(2, shoppingCart.getItemQuantity("item1"));
    }

    @Test
    void testAddItem_invalidQuantity() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCart.addItem("item1", -1);
        });
        assertEquals("Quantity must be positive", exception.getMessage());
    }

    @Test
    void testAddItem_duplicateItemAddsQuantity() {
        shoppingCart.addItem("item1", 2);
        shoppingCart.addItem("item1", 3);
        assertEquals(5, shoppingCart.getItemQuantity("item1"));
    }

    @Test
    void testRemoveItem_existingItem() {
        shoppingCart.addItem("item1", 2);
        shoppingCart.removeItem("item1");
        assertEquals(0, shoppingCart.getItemQuantity("item1"));
    }

    @Test
    void testRemoveItem_nonExistingItem() {
        assertDoesNotThrow(() -> shoppingCart.removeItem("item1"));
    }

    @Test
    void testGetTotal_noItems() {
        assertEquals(0, shoppingCart.getTotal(), 0.001);
    }

    @Test
    void testGetTotal_withItems() {
        shoppingCart.addItem("item1", 2); // Assuming price per item = 10
        shoppingCart.addItem("item2", 1); // Assuming price per item = 20
        assertEquals(40, shoppingCart.getTotal(), 0.001); // (2*10) + (1*20)
    }

    @Test
    void testApplyDiscount_validDiscount() {
        shoppingCart.addItem("item1", 3); // Assuming price per item = 10
        shoppingCart.applyDiscount(10);  // 10% discount
        assertEquals(27, shoppingCart.getTotal(), 0.001); // Total after discount: 30 - (30 * 0.1)
    }

    @Test
    void testApplyDiscount_zeroDiscount() {
        shoppingCart.addItem("item1", 3); // Assuming price per item = 10
        shoppingCart.applyDiscount(0);
        assertEquals(30, shoppingCart.getTotal(), 0.001);
    }

    @Test
    void testApplyDiscount_fullDiscount() {
        shoppingCart.addItem("item1", 3); // Assuming price per item = 10
        shoppingCart.applyDiscount(100);
        assertEquals(0, shoppingCart.getTotal(), 0.001);
    }

    @Test
    void testApplyDiscount_negativeDiscount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCart.applyDiscount(-5);
        });
        assertEquals("Discount percentage must be between 0 and 100", exception.getMessage());
    }

    @Test
    void testApplyDiscount_over100Discount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCart.applyDiscount(105);
        });
        assertEquals("Discount percentage must be between 0 and 100", exception.getMessage());
    }

    @Test
    void testEdgeCase_emptyCartDiscount() {
        shoppingCart.applyDiscount(10);
        assertEquals(0, shoppingCart.getTotal(), 0.001);
    }

    @Test
    void testEdgeCase_removeItemReducesTotal() {
        shoppingCart.addItem("item1", 2); // Assuming price per item = 10
        shoppingCart.addItem("item2", 1); // Assuming price per item = 20
        shoppingCart.removeItem("item1");
        assertEquals(20, shoppingCart.getTotal(), 0.001); // Only item2 remains
    }
}
