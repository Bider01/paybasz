package hu.schbme.paybasz.station.config;

import hu.schbme.paybasz.station.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@SuppressWarnings("SpellCheckingInspection")
@Profile("test")
@Configuration
public class TestConfig {

    @Autowired
    private TransactionService system;

    @PostConstruct
    public void init() {
        system.createTestAccount("Test Name", "test@test.com", "+36301234567", "AABBCCDD00110", 2500, 0, false);
        system.createTestAccount("Another test", "another@test.com", "+36307654321", "AABBCCEE214234", 100, -1000, true);

        system.createTestItem("Napi menü", "3dl", "103", "Napi menu 30", 750, false);
        system.createTestItem("Napi menü", "5dl", "105", "Napi menu 50", 750, true);
        system.createTestItem("Jäger meister", "2cl", "202", "Jager 2", 300, true);
        system.createTestItem("Jäger meister", "4cl", "204", "Jager 4", 600, true);
        system.createTestItem("Jäger meister", "8cl", "208", "Jager 8", 1200, false);
        system.createTestItem("Unicum", "2cl", "302", "Unicum 2", 320, true);
        system.createTestItem("Unicum", "4cl", "304", "Unicum 4", 640, false);
        system.createTestItem("Unicum", "8cl", "308", "Unicum 8", 1280, true);

        system.createTestItem("Unicum duplicate", "2cl", "302", "Unicum 2", 2320, true);
        system.createTestItem("Unicum duplicate", "4cl", "304", "Unicum 2", 2640, false);
        system.createTestItem("Unicum duplicate", "8cl", "308", "Unicum 2", 21280, true);

    }

}
