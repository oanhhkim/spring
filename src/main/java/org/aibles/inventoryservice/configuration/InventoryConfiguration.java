package org.aibles.inventoryservice.configuration;

import org.aibles.inventoryservice.repository.InventoryRepository;
import org.aibles.inventoryservice.service.InventoryService;
import org.aibles.inventoryservice.service.impl.InventoryServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.trainingjava.core.api.exception.configuration.EnableCoreApiException;
import org.trainingjava.coreresttemplate.configuration.EnableRestTemplate;

@Configuration
@EnableCoreApiException
@EnableRestTemplate
@EnableJpaRepositories(basePackages = {"org.aibles.inventoryservice.repository"})
@ComponentScan(basePackages = {"org.aibles.inventoryservice.repository"})
public class InventoryConfiguration {
  @Bean
  public InventoryService inventoryService(InventoryRepository repository){
    return new InventoryServiceImpl(repository);
  }
}
