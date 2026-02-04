package it.guesser.algashop.ordering.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class DocumentTest {

    @Test
    void givenValidValue_whenConstructingDocument_thenValueIsSet() {
        Document document = new Document("DOC123");

        assertThat(document.value()).isEqualTo("DOC123");
    }

    @Test
    void givenValueWithSpaces_whenConstructingDocument_thenValueIsTrimmed() {
        Document document = new Document("  DOC123  ");

        assertThat(document.value()).isEqualTo("DOC123");
    }

    @Test
    void givenNullValue_whenConstructingDocument_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Document(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenBlankValue_whenConstructingDocument_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new Document("   "))
                .isInstanceOf(NullPointerException.class);
    }
}
