// No changes were necessary for this file.
// The analysis tool suggested replacing @Stateless with @ApplicationScoped,
// but the provided code already uses @ApplicationScoped, which is the correct
// annotation for a shared service bean in Quarkus.
package com.redhat.coolstore.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.coolstore.model.CatalogItemEntity;
import com.redhat.coolstore.model.InventoryEntity;

@ApplicationScoped
@Transactional
public class CatalogService {

    private static final Logger log = LoggerFactory.getLogger(CatalogService.class);

    @Inject
    private EntityManager em;

    public CatalogService() {
    }

    public List<CatalogItemEntity> getCatalogItems() {
        // Replaced the Criteria API query with a more concise JPQL query for this simple case.
        return em.createQuery("from CatalogItemEntity", CatalogItemEntity.class).getResultList();
    }

    public CatalogItemEntity getCatalogItemById(String itemId) {
        return em.find(CatalogItemEntity.class, itemId);
    }

    public void updateInventoryItems(String itemId, int deducts) {
        log.info("Updating inventory for item {} with a deduction of {}", itemId, deducts);
        InventoryEntity inventoryEntity = getCatalogItemById(itemId).getInventory();
        int currentQuantity = inventoryEntity.getQuantity();
        inventoryEntity.setQuantity(currentQuantity - deducts);
        em.merge(inventoryEntity);
    }

}