package org.example.project.billing

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.time.Clock

class MockBillingManager : BillingManager {
    private val _purchaseEvents = MutableSharedFlow<PurchaseResult>()
    override val purchaseEvents: Flow<PurchaseResult> = _purchaseEvents.asSharedFlow()

    override suspend fun initialize() {
        delay(500)
    }

    override suspend fun getProducts(productIds: Set<String>): List<ProductInfo> {
        delay(300)
        return listOf(
            ProductInfo(
                productId = "premium_upgrade",
                title = "Premium Upgrade (Mock)",
                priceText = "$4.99",
                rawPrice = 4.99
            ),
            ProductInfo(
                productId = "support_developer",
                title = "Support Creator (Mock)",
                priceText = "$0.99",
                rawPrice = 0.99
            )
        ).filter { productIds.contains(it.productId) }
    }

    override suspend fun launchBillingFlow(product: ProductInfo): Result<Unit> {
        // Simulating network transaction delay
        delay(800)
        
        val currentTime = Clock.System.now().toEpochMilliseconds()
        _purchaseEvents.emit(
            PurchaseResult.Success(
                productId = product.productId,
                purchaseToken = "mock-token-${product.productId}-$currentTime"
            )
        )
        return Result.success(Unit)
    }
}
