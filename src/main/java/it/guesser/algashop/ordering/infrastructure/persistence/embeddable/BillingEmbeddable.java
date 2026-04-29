package it.guesser.algashop.ordering.infrastructure.persistence.embeddable;


import it.guesser.algashop.ordering.domain.valueobject.*;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BillingEmbeddable {

    private String firstName;
    private String lastName;
    private String document;
    private String phone;
    @Embedded
    private AddressEmbeddable address;
    private String email;


}
