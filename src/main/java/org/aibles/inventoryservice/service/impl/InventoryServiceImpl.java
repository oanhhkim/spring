package org.aibles.inventoryservice.service.impl;

import org.aibles.inventoryservice.repository.InventoryRepository;
import org.aibles.inventoryservice.service.InventoryService;

public class InventoryServiceImpl implements InventoryService {
  private final InventoryRepository repository;

  public InventoryServiceImpl(InventoryRepository repository) {
    this.repository = repository;
  }
}
