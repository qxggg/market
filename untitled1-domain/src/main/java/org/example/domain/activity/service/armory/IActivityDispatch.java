package org.example.domain.activity.service.armory;

import java.util.Date;

public interface IActivityDispatch {
    boolean subtractionActivitySkuCount(Long sku, Date endDateTime);
    public void cacheActivityStockSku(Long sku, Integer stockCount);
}
