package org.example.project.ads

interface AdManager {
    fun loadInterstitial(adUnitId: String)
    fun showInterstitial(onAdDismissed: () -> Unit)
}
