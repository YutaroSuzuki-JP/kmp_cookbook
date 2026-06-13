package org.example.project.ads

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockAdManager : AdManager {
    private val _isAdLoaded = MutableStateFlow(false)
    val isAdLoaded: StateFlow<Boolean> = _isAdLoaded.asStateFlow()

    private var currentAdUnitId: String? = null
    private var pendingOnAdDismissed: (() -> Unit)? = null

    private val _showAdRequest = MutableStateFlow<String?>(null)
    val showAdRequest: StateFlow<String?> = _showAdRequest.asStateFlow()

    override fun loadInterstitial(adUnitId: String) {
        currentAdUnitId = adUnitId
        _isAdLoaded.value = true
    }

    override fun showInterstitial(onAdDismissed: () -> Unit) {
        if (_isAdLoaded.value) {
            pendingOnAdDismissed = onAdDismissed
            _showAdRequest.value = currentAdUnitId ?: "test-ad-unit-id"
        } else {
            onAdDismissed()
        }
    }

    fun dismissAd() {
        _isAdLoaded.value = false
        _showAdRequest.value = null
        pendingOnAdDismissed?.invoke()
        pendingOnAdDismissed = null
    }
}
