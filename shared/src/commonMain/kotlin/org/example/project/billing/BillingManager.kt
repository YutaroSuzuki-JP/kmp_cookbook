package org.example.project.billing

import kotlinx.coroutines.flow.Flow

data class ProductInfo(
    val productId: String,
    val title: String,
    val priceText: String,
    val rawPrice: Double
)

sealed interface PurchaseResult {
    data class Success(val productId: String, val purchaseToken: String) : PurchaseResult
    data object UserCancelled : PurchaseResult
    data class Failure(val errorMessage: String) : PurchaseResult
}

interface BillingManager {
    val purchaseEvents: Flow<PurchaseResult>
    suspend fun initialize()
    suspend fun getProducts(productIds: Set<String>): List<ProductInfo>
    suspend fun launchBillingFlow(product: ProductInfo): Result<Unit>
}
