package it.guesser.algashop.ordering.domain.entity;

import static java.util.Objects.requireNonNull;

import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Product;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import it.guesser.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

public class ShoppingCartItem {

    private final ShoppingCartItemId id;
    private final ShoppingCartId shoppingCartId;
    private Product product;
    private Quantity quantity = Quantity.ZERO;
    private Money totalAmount = Money.ZERO;

    private ShoppingCartItem(ShoppingCartItemId id, ShoppingCartId shoppingCartId, Product product, Quantity quantity,
            Money totalAmount) {
        this.id = requireNonNull(id);
        this.shoppingCartId = requireNonNull(shoppingCartId);
        this.product = requireNonNull(product);
        this.quantity = requireNonNull(quantity);
        this.totalAmount = requireNonNull(totalAmount);
    }

    public static ShoppingCartItem brandNew(ShoppingCartId shoppingCartId, Product product, Quantity quantity) {
        return new ShoppingCartItem(
                new ShoppingCartItemId(),
                shoppingCartId,
                product,
                quantity,
                product.price().multiply(quantity));

    }

    public ShoppingCartItemId getId() {
        return id;
    }

    public ShoppingCartId getShoppingCartId() {
        return shoppingCartId;
    }

    public Product getProduct() {
        return product;
    }

    private void setProduct(Product product) {
        this.product = requireNonNull(product);
    }

    public Quantity getQuantity() {
        return quantity;
    }

    private void setQuantity(Quantity quantity) {
        this.quantity = requireNonNull(quantity);
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    private void setTotalAmount(Money totalAmount) {
        this.totalAmount = requireNonNull(totalAmount);
    }

    public void refresh(Product product) {
        requireNonNull(product);

        setProduct(product);

        recalculateTotal();
    }

    private void recalculateTotal() {
        setTotalAmount(getProduct().price().multiply(getQuantity()));
    }

    public void changeQuantity(Quantity newQuantity) {
        requireNonNull(newQuantity);

        setQuantity(newQuantity);

        recalculateTotal();
    }

}
