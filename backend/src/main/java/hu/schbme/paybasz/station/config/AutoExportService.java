package hu.schbme.paybasz.station.config;

import hu.schbme.paybasz.station.service.GatewayService;
import hu.schbme.paybasz.station.service.LoggingService;
import hu.schbme.paybasz.station.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Configuration
@EnableScheduling
public class AutoExportService implements CommandLineRunner {

    @Autowired
    private TransactionService system;

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private LoggingService logger;

    @Value("${server.port}")
    private int port;

    @PostConstruct
    public void onStarted() {
        logger.serverInfo("A backend szolgáltatás elindult. Az esemény napló mindig az aktuális futást mutatja.");
    }

    @Override
    public void run(String... args) {
        System.out.println("|");
        System.out.println("| Admin panel available on: http://127.0.0.1:" + port + "/admin/");
        System.out.println("|");
    }

    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void autoSave10m() {
        save("10m");
    }

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void autoSave1h() {
        save("1h");
    }

    @PreDestroy
    public void autoSaveOnStop() {
        save("stop-at-" + AppUtil.DATE_TIME_FILE_FORMATTER.format(System.currentTimeMillis()));
    }

    private void save(String tag) {
        File saves = new File("saves");
        saves.mkdir();
        try {
            Files.writeString(Path.of("saves/autosave-" + tag + "-accounts.csv"), system.exportAccounts());
            Files.writeString(Path.of("saves/autosave-" + tag + "-transactions.csv"), system.exportTransactions());
            Files.writeString(Path.of("saves/autosave-" + tag + "-logs.csv"), system.exportTransactions());
        } catch (IOException e) {
            log.error("Exception happened during " + tag + " auto-save", e);
        }

        log.info("Auto (" + tag + ") log saved to '" + saves.getAbsolutePath() + "' folder");
    }

}
