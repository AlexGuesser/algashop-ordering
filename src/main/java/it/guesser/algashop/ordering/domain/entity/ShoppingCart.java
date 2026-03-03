package it.guesser.algashop.ordering.domain.entity;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import it.guesser.algashop.ordering.domain.exceptions.ProductOutOfStockException;
import it.guesser.algashop.ordering.domain.exceptions.ShoppingCartItemNotFoundException;
import it.guesser.algashop.ordering.domain.valueobject.Money;
import it.guesser.algashop.ordering.domain.valueobject.Product;
import it.guesser.algashop.ordering.domain.valueobject.Quantity;
import it.guesser.algashop.ordering.domain.valueobject.id.CustomerId;
import it.guesser.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import it.guesser.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

public class ShoppingCart implements AggregateRoot<ShoppingCartId>{

    private final ShoppingCartId id;
    private final CustomerId customerId;
    private Money totalAmount = Money.ZERO;
    private Quantity totalItems = Quantity.ZERO;
    private long createdAt;
    private Set<ShoppingCartItem> items = new HashSet<>();

    private ShoppingCart(ShoppingCartId id, CustomerId customerId) {
        this.id = requireNonNull(id);
        this.customerId = requireNonNull(customerId);
    }

    public static ShoppingCart startShopping(CustomerId customerId) {
        return new ShoppingCart(new ShoppingCartId(), customerId);
    }

    public ShoppingCartId getId() {
        return id;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public Quantity getTotalItems() {
        return totalItems;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public Set<ShoppingCartItem> getItems() {
        return Set.copyOf(items);
    }

    private void setTotalItems(Quantity newTotalItems) {
        this.totalItems = requireNonNull(newTotalItems);
    }

    private void setTotalAmount(Money newTotalAmount) {
        this.totalAmount = requireNonNull(newTotalAmount);
    }

    public void clear() {
        this.items.clear();
        recalculateTotals();
    }

    private void recalculateTotals() {
        Quantity newTotalItems = getItems().stream()
                .map(sci -> sci.getQuantity())
                .reduce(Quantity.ZERO, Quantity::add);
        Money newTotalAmount = getItems().stream()
                .map(sci -> sci.getTotalAmount())
                .reduce(Money.ZERO, Money::add);

        setTotalItems(newTotalItems);
        setTotalAmount(newTotalAmount);
    }

    public void addItem(Product product, Quantity quantity) {
        requireNonNull(product);
        requireNonNull(quantity);
        verifyIfProductInStock(product);

        Optional<ShoppingCartItem> cartItemByProduct = findByProduct(product);
        cartItemByProduct.ifPresentOrElse(
                // UPDATES QUANTITY OF EXISTITING SHOPPING CART ITEM AND REFRESH PRODUCT VALUES
                shoppingCartItem -> {
                    shoppingCartItem.changeQuantity(shoppingCartItem.getQuantity().add(quantity));
                    shoppingCartItem.refresh(product);
                }, () -> {
                    // ADDS NEW SHOPPING CART ITEM
                    items.add(ShoppingCartItem.brandNew(getId(), product, quantity));
                });

        recalculateTotals();
    }

    public void removeItem(ShoppingCartItemId shoppingCartItemId) {
        ShoppingCartItem shoppingCartItem = getByShoppingCartItemId(shoppingCartItemId);

        this.items.remove(shoppingCartItem);

        recalculateTotals();
    }

    public void refreshItem(Product product) {
        findByProduct(product).ifPresent(
                sci -> sci.refresh(product));

        recalculateTotals();
    }

    public void changeItemQuantity(ShoppingCartItemId shoppingCartItemId, Quantity quantity) {
        ShoppingCartItem shoppingCartItem = getByShoppingCartItemId(shoppingCartItemId);
        shoppingCartItem.changeQuantity(quantity);

        recalculateTotals();
    }

    public boolean containsUnavailableItems() {
        return getItems().stream()
                .map(sci -> sci.getProduct())
                .anyMatch(product -> Boolean.FALSE.equals(product.inStock()));
    }

    public boolean isEmpty() {
        return getItems().isEmpty();
    }

    private ShoppingCartItem getByShoppingCartItemId(ShoppingCartItemId shoppingCartItemId) {
        return getItems().stream()
                .filter(sci -> sci.getId().equals(shoppingCartItemId)).findFirst()
                .orElseThrow(() -> new ShoppingCartItemNotFoundException(shoppingCartItemId, getId()));
    }

    private Optional<ShoppingCartItem> findByProduct(Product product) {
        return getItems().stream()
                .filter(sci -> sci.getProduct().equals(product))
                .findFirst();
    }

    private void verifyIfProductInStock(Product product) {
        if (!product.inStock()) {
            throw new ProductOutOfStockException(product.id());
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShoppingCart other = (ShoppingCart) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
