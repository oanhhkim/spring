package org.aibles.inventoryservice.constant;

import static org.aibles.inventoryservice.constant.InventoryApiConstant.ApiConstant.API_PREFIX;
import static org.aibles.inventoryservice.constant.InventoryApiConstant.ApiConstant.API_VERSION;
import static org.aibles.inventoryservice.constant.InventoryApiConstant.ResourceConstant.INVENTORY;

public class InventoryApiConstant {
  public static class ApiConstant {
    public static final String API_PREFIX = "/api";
    public static final String API_VERSION = "/v1";
  }

  public static class ResourceConstant {
    public static final String INVENTORY = "/inventories";
  }

  public static class BaseUrl {

    public static final String INVENTORY_URL = API_PREFIX + API_VERSION + INVENTORY;

  }
}
