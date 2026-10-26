    package com.polarisbank.customer.entity;

    import com.polarisbank.customer.StatusCustomer;
    import org.junit.jupiter.api.Test;

    import java.time.Instant;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;

    import static org.junit.jupiter.api.Assertions.*;

    public class CustomerTest {

        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        private LocalDate parseBirthDate(String date) {
            return LocalDate.parse(date, DATE_FORMATTER);
        }

        @Test
        void shouldCreateCustomerWithActiveStatus() {
            var customer = new Customer("Jefferson Gomes", "52998224725", "jefferson@example.com", parseBirthDate("20/05/1995"));
            assertEquals(StatusCustomer.ACTIVE, customer.getStatusCustomer());
        }

        @Test
        void shouldCreateCustomerWithInitializedAuditTimestamps() {
            var customer = new Customer("Jefferson Gomes", "52998224725", "jefferson@example.com", parseBirthDate("20/05/1995"));

            assertNotNull(customer.getCreatedAt());
            assertNotNull(customer.getUpdatedAt());
            assertEquals(customer.getCreatedAt(), customer.getUpdatedAt());
        }

        @Test
        void shouldCreateCustomerWithProvidedData() {
            var birthDate = parseBirthDate("20/05/1995");
            var customer = new Customer("Jefferson Gomes", "52998224725", "jefferson@example.com", birthDate);

            assertEquals("Jefferson Gomes", customer.getFullName());
            assertEquals("52998224725", customer.getCpf());
            assertEquals("jefferson@example.com", customer.getEmail());
            assertEquals(birthDate, customer.getBirthDate());
        }

        @Test
        void shouldChangeCustomerStatus() {
            Customer customer = new Customer("Jefferson Gomes", "52998224725", "jefferson@example.com", parseBirthDate("20/05/1995"));

            customer.changeStatus(StatusCustomer.INACTIVE);

            assertEquals(StatusCustomer.INACTIVE, customer.getStatusCustomer());
            assertNotNull(customer.getUpdatedAt());
        }

        @Test
        void shouldUpdateTimestampWhenCustomerStatusChanges() throws InterruptedException {
            Customer customer = new Customer("Jefferson Gomes", "52998224725", "jefferson@example.com", parseBirthDate("20/05/1995"));
            Instant originalUpdatedAt = customer.getUpdatedAt();

            Thread.sleep(2);
            customer.changeStatus(StatusCustomer.INACTIVE);

            assertTrue(customer.getUpdatedAt().isAfter(originalUpdatedAt));
        }

    }

