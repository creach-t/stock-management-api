package com.inventory.stockmanagementapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Profile("dev") // Only run this in development mode
public class DataResetScheduler {

    private final DataInitializer dataInitializer;

    @Autowired
    public DataResetScheduler(DataInitializer dataInitializer) {
        this.dataInitializer = dataInitializer;
    }

    /**
     * Réinitialise les données toutes les 5 minutes
     * La méthode est exécutée toutes les 5 minutes (300 000 millisecondes)
     */
    @Scheduled(fixedRate = 300000)
    public void resetDataPeriodically() {
        System.out.println("Réinitialisation programmée des données en cours...");
        dataInitializer.initializeData();
        System.out.println("Réinitialisation des données terminée avec succès!");
    }
}
