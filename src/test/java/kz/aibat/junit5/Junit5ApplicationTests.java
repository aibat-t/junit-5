package kz.aibat.junit5;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.PrintWriter;

@SpringBootTest
class Junit5ApplicationTests {

    public static void main(String[] args) {
        var launcher = LauncherFactory.create();

        var summary = new SummaryGeneratingListener();

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectPackage("kz.aibat.junit5.service"))
                .build();
        launcher.execute(request, summary);
        try (var writer = new PrintWriter(System.out)) {
            summary.getSummary().printTo(writer);
        }
    }
}
