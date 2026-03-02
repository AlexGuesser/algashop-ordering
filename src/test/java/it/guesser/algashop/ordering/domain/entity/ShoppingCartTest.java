package it.guesser.algashop.ordering.domain.entity;

import static it.guesser.algashop.ordering.domain.valueobject.ProductDataTestBuilder.aProductInStock;
import static it.guesser.algashop.ordering.domain.valueobject.ProductDataTestBuilder.aProductOutOfStock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.exceptions.ProductOutOfStockException;
import it.guesser.algashop.ordering.domain.exceptions.ShoppingCartItemNotFoundException;
import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Product;
import it.guesser.algashop.ordering.domain.valueobject.ProductName;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;
import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;
import it.guesser.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

class ShoppingCartTest {

    @Test
    void givenCustomerId_whenStartShopping_thenShoppingCartIsInitializedCorrectly() {
        CustomerId customerId = new CustomerId();

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        assertThat(shoppingCart).satisfies(cart -> {
            assertThat(cart.getId()).isNotNull();
            assertThat(cart.getCustomerId()).isEqualTo(customerId);
            assertThat(cart.getTotalAmount()).isEqualTo(Money.ZERO);
            assertThat(cart.getTotalItems()).isEqualTo(Quantity.ZERO);
            assertThat(cart.getCreatedAt()).isZero();
            assertThat(cart.getItems()).isNotNull();
            assertThat(cart.getItems()).isEmpty();
            assertThat(cart.isEmpty()).isTrue();
        });
    }

    @Test
    void givenNullCustomerId_whenStartShopping_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> ShoppingCart.startShopping(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldGenerateException_whenTryingToAdjustImmutableSetOfShoppingCartItems() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Set<ShoppingCartItem> items = shoppingCart.getItems();

        assertThatThrownBy(items::clear)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void givenProductOutOfStock_whenAddItem_thenThrowsProductOutOfStockException() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product outOfStockProduct = aProductOutOfStock();

        assertThatThrownBy(() -> shoppingCart.addItem(outOfStockProduct, new Quantity(1)))
                .isInstanceOf(ProductOutOfStockException.class);
    }

    @Test
    void givenValidProductAndQuantity_whenAddItem_thenShoppingCartAndItemAreUpdatedCorrectly() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Quantity quantity = new Quantity(2);

        Product product = aProductInStock();
        Money price = product.price();

        shoppingCart.addItem(product, quantity);

        assertThat(shoppingCart.getItems()).hasSize(1);
        assertThat(shoppingCart.getTotalItems()).isEqualTo(quantity);
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(price.multiply(quantity));
        assertThat(shoppingCart.isEmpty()).isFalse();

        ShoppingCartItem item = shoppingCart.getItems().iterator().next();
        assertThat(item.getShoppingCartId()).isEqualTo(shoppingCart.getId());
        assertThat(item.getProduct()).isEqualTo(product);
        assertThat(item.getQuantity()).isEqualTo(quantity);
        assertThat(item.getTotalAmount()).isEqualTo(price.multiply(quantity));
    }

    @Test
    void givenSameProductAddedTwice_whenAddItem_thenQuantityAndTotalsAreAccumulated() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product product = aProductInStock();
        Money price = product.price();

        shoppingCart.addItem(product, new Quantity(1));
        shoppingCart.addItem(product, new Quantity(2));

        Quantity expectedQuantity = new Quantity(3);
        Money expectedTotalAmount = price.multiply(expectedQuantity);

        assertThat(shoppingCart.getItems()).hasSize(1);
        assertThat(shoppingCart.getTotalItems()).isEqualTo(expectedQuantity);
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(expectedTotalAmount);

        ShoppingCartItem item = shoppingCart.getItems().iterator().next();
        assertThat(item.getQuantity()).isEqualTo(expectedQuantity);
        assertThat(item.getTotalAmount()).isEqualTo(expectedTotalAmount);
    }

    @Test
    void givenNullProduct_whenAddItem_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        assertThatThrownBy(() -> shoppingCart.addItem(null, new Quantity(1)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullQuantity_whenAddItem_thenThrowsNullPointerException() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);
        Product product = aProductInStock();

        assertThatThrownBy(() -> shoppingCart.addItem(product, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNonEmptyShoppingCart_whenClear_thenItemsAndTotalsAreReset() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product product = aProductInStock();
        shoppingCart.addItem(product, new Quantity(2));

        assertThat(shoppingCart.getItems()).isNotEmpty();
        assertThat(shoppingCart.getTotalItems()).isNotEqualTo(Quantity.ZERO);
        assertThat(shoppingCart.getTotalAmount()).isNotEqualTo(Money.ZERO);

        shoppingCart.clear();

        assertThat(shoppingCart.getItems()).isEmpty();
        assertThat(shoppingCart.getTotalItems()).isEqualTo(Quantity.ZERO);
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(Money.ZERO);
        assertThat(shoppingCart.isEmpty()).isTrue();
    }

    @Test
    void givenExistingItem_whenRemoveItem_thenItemIsRemovedAndTotalsAreRecalculated() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product product1 = aProductInStock();
        Product product2 = new Product(new ProductId(), new ProductName("Product 2"), new Money("5.00"), true);

        shoppingCart.addItem(product1, new Quantity(2));
        shoppingCart.addItem(product2, new Quantity(1));

        Money totalAmountBefore = shoppingCart.getTotalAmount();
        Quantity totalItemsBefore = shoppingCart.getTotalItems();

        ShoppingCartItem itemToRemove = shoppingCart.getItems().iterator().next();

        shoppingCart.removeItem(itemToRemove.getId());

        assertThat(shoppingCart.getItems()).doesNotContain(itemToRemove);
        assertThat(shoppingCart.getTotalItems().value()).isLessThan(totalItemsBefore.value());
        assertThat(shoppingCart.getTotalAmount().value()).isLessThan(totalAmountBefore.value());
    }

    @Test
    void givenUnknownItemId_whenRemoveItem_thenThrowsShoppingCartItemNotFoundException() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product product = aProductInStock();
        shoppingCart.addItem(product, new Quantity(1));

        ShoppingCartItemId unknownId = new ShoppingCartItemId();

        assertThatThrownBy(() -> shoppingCart.removeItem(unknownId))
                .isInstanceOf(ShoppingCartItemNotFoundException.class);
    }

    @Test
    void givenExistingItem_whenChangeItemQuantity_thenQuantityAndTotalsAreRecalculated() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product product = aProductInStock();
        Money price = product.price();

        shoppingCart.addItem(product, new Quantity(1));
        ShoppingCartItem existingItem = shoppingCart.getItems().iterator().next();

        shoppingCart.changeItemQuantity(existingItem.getId(), new Quantity(3));

        assertThat(existingItem.getQuantity()).isEqualTo(new Quantity(3));
        assertThat(existingItem.getTotalAmount()).isEqualTo(price.multiply(new Quantity(3)));
        assertThat(shoppingCart.getTotalItems()).isEqualTo(new Quantity(3));
        assertThat(shoppingCart.getTotalAmount()).isEqualTo(price.multiply(new Quantity(3)));
    }

    @Test
    void givenUnknownItemId_whenChangeItemQuantity_thenThrowsShoppingCartItemNotFoundException() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product product = aProductInStock();
        shoppingCart.addItem(product, new Quantity(1));

        ShoppingCartItemId unknownId = new ShoppingCartItemId();

        assertThatThrownBy(() -> shoppingCart.changeItemQuantity(unknownId, new Quantity(2)))
                .isInstanceOf(ShoppingCartItemNotFoundException.class);
    }

    @Test
    void givenCartWithItems_whenRefreshItem_thenTotalsAreRecalculated() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product product = aProductInStock();
        Quantity quantity = new Quantity(2);

        shoppingCart.addItem(product, quantity);
        Money totalAmountBefore = shoppingCart.getTotalAmount();

        // using the same product instance; refresh should keep totals consistent
        shoppingCart.refreshItem(product);

        assertThat(shoppingCart.getTotalAmount()).isEqualTo(totalAmountBefore);
        assertThat(shoppingCart.getTotalItems()).isEqualTo(quantity);
    }

    @Test
    void givenEmptyCart_whenContainsUnavailableItems_thenReturnsFalse() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        assertThat(shoppingCart.containsUnavailableItems()).isFalse();
    }

    @Test
    void givenCartWithInStockItems_whenContainsUnavailableItems_thenReturnsFalse() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product product = aProductInStock();
        shoppingCart.addItem(product, new Quantity(1));

        assertThat(shoppingCart.containsUnavailableItems()).isFalse();
    }

    @Test
    void givenEmptyCart_whenIsEmpty_thenReturnsTrue() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        assertThat(shoppingCart.isEmpty()).isTrue();
    }

    @Test
    void givenCartWithItems_whenIsEmpty_thenReturnsFalse() {
        CustomerId customerId = new CustomerId();
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        Product product = aProductInStock();
        shoppingCart.addItem(product, new Quantity(1));

        assertThat(shoppingCart.isEmpty()).isFalse();
    }

}

