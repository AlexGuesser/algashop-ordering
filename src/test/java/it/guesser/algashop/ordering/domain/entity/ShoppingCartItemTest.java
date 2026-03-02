package it.guesser.algashop.ordering.domain.entity;

import static it.guesser.algashop.ordering.domain.valueobject.ProductDataTestBuilder.aProductInStock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Product;
import it.guesser.algashop.ordering.domain.valueobject.ProductName;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.id.ProductId;
import it.guesser.algashop.ordering.domain.valueobject.id.ShoppingCartId;

class ShoppingCartItemTest {

    @Test
    void givenShoppingCartIdProductAndQuantity_whenBrandNew_thenItemIsInitializedCorrectly() {
        ShoppingCartId shoppingCartId = new ShoppingCartId();
        Quantity quantity = new Quantity(2);
        Product product = aProductInStock();
        Money price = product.price();

        ShoppingCartItem item = ShoppingCartItem.brandNew(shoppingCartId, product, quantity);

        assertThat(item).satisfies(i -> {
            assertThat(i.getId()).isNotNull();
            assertThat(i.getShoppingCartId()).isEqualTo(shoppingCartId);
            assertThat(i.getProduct()).isEqualTo(product);
            assertThat(i.getQuantity()).isEqualTo(quantity);
            assertThat(i.getTotalAmount()).isEqualTo(price.multiply(quantity));
        });
    }

    @Test
    void givenNullShoppingCartId_whenBrandNew_thenThrowsNullPointerException() {
        Product product = aProductInStock();
        Quantity quantity = new Quantity(1);

        assertThatThrownBy(() -> ShoppingCartItem.brandNew(null, product, quantity))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullProduct_whenBrandNew_thenThrowsNullPointerException() {
        ShoppingCartId shoppingCartId = new ShoppingCartId();
        Quantity quantity = new Quantity(1);

        assertThatThrownBy(() -> ShoppingCartItem.brandNew(shoppingCartId, null, quantity))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenNullQuantity_whenBrandNew_thenThrowsNullPointerException() {
        ShoppingCartId shoppingCartId = new ShoppingCartId();
        Product product = aProductInStock();

        assertThatThrownBy(() -> ShoppingCartItem.brandNew(shoppingCartId, product, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenExistingItem_whenRefreshWithNewProduct_thenProductAndTotalAmountAreUpdated() {
        ShoppingCartId shoppingCartId = new ShoppingCartId();
        ProductId productId = new ProductId();
        ProductName productName = new ProductName("Product 1");
        Money originalPrice = new Money("10.00");
        Quantity quantity = new Quantity(2);
        Product originalProduct = new Product(productId, productName, originalPrice, true);
        ShoppingCartItem item = ShoppingCartItem.brandNew(shoppingCartId, originalProduct, quantity);

        Money updatedPrice = new Money("15.00");
        Product updatedProduct = new Product(productId, productName, updatedPrice, true);

        item.refresh(updatedProduct);

        assertThat(item.getProduct()).isEqualTo(updatedProduct);
        assertThat(item.getQuantity()).isEqualTo(quantity);
        assertThat(item.getTotalAmount()).isEqualTo(updatedPrice.multiply(quantity));
    }

    @Test
    void givenNullProduct_whenRefresh_thenThrowsNullPointerException() {
        ShoppingCartId shoppingCartId = new ShoppingCartId();
        Product product = aProductInStock();
        Quantity quantity = new Quantity(1);
        ShoppingCartItem item = ShoppingCartItem.brandNew(shoppingCartId, product, quantity);

        assertThatThrownBy(() -> item.refresh(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenExistingItem_whenChangeQuantity_thenQuantityAndTotalAmountAreUpdated() {
        ShoppingCartId shoppingCartId = new ShoppingCartId();
        Product product = aProductInStock();
        Money price = product.price();
        Quantity originalQuantity = new Quantity(1);
        ShoppingCartItem item = ShoppingCartItem.brandNew(shoppingCartId, product, originalQuantity);

        Quantity newQuantity = new Quantity(3);

        item.changeQuantity(newQuantity);

        assertThat(item.getQuantity()).isEqualTo(newQuantity);
        assertThat(item.getTotalAmount()).isEqualTo(price.multiply(newQuantity));
    }

    @Test
    void givenNullQuantity_whenChangeQuantity_thenThrowsNullPointerException() {
        ShoppingCartId shoppingCartId = new ShoppingCartId();
        Product product = aProductInStock();
        Quantity quantity = new Quantity(1);
        ShoppingCartItem item = ShoppingCartItem.brandNew(shoppingCartId, product, quantity);

        assertThatThrownBy(() -> item.changeQuantity(null))
                .isInstanceOf(NullPointerException.class);
    }

}

