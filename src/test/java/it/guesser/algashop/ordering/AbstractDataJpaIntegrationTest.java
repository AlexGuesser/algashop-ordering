package it.guesser.algashop.ordering;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import it.guesser.algashop.ordering.infrastructure.persistence.config.HibernateConfiguration;

@DataJpaTest
@Import(HibernateConfiguration.class)
public abstract class AbstractDataJpaIntegrationTest {
}
