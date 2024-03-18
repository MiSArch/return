package org.misarch.returns.service

import org.misarch.returns.event.model.ProductVariantVersionDTO
import org.misarch.returns.persistence.model.ProductVariantVersionEntity
import org.misarch.returns.persistence.repository.ProductVariantVersionRepository
import org.springframework.stereotype.Service

/**
 * Service for [ProductVariantVersionEntity]s
 *
 * @param repository the provided repository
 */
@Service
class ProductVariantVersionService(
    repository: ProductVariantVersionRepository
) : BaseService<ProductVariantVersionEntity, ProductVariantVersionRepository>(repository) {

    /**
     * Registers a product variant version
     *
     * @param productVariantVersionDTO the product variant version to register
     */
    suspend fun registerProductVariantVersion(productVariantVersionDTO: ProductVariantVersionDTO) {
        repository.createProductVariantVersion(
            productVariantVersionDTO.id,
            productVariantVersionDTO.canBeReturnedForDays
        )
    }

}