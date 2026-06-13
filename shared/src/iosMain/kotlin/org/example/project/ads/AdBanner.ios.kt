package org.example.project.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UILabel
import platform.UIKit.UIColor
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UIView
import platform.UIKit.NSLayoutConstraint

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun AdBanner(adUnitId: String, modifier: Modifier) {
    UIKitView(
        factory = {
            val container = UIView().apply {
                setBackgroundColor(UIColor.colorWithRed(0.9, 0.66, 0.23, 1.0)) // Gold
            }
            val label = UILabel().apply {
                text = "iOS Native AdBanner Simulator\n[AdUnit: $adUnitId]"
                textColor = UIColor.blackColor
                textAlignment = NSTextAlignmentCenter
                numberOfLines = 0
                setFont(platform.UIKit.UIFont.systemFontOfSize(12.0))
            }
            container.addSubview(label)
            
            label.setTranslatesAutoresizingMaskIntoConstraints(false)
            NSLayoutConstraint.activateConstraints(listOf(
                label.centerXAnchor.constraintEqualToAnchor(container.centerXAnchor),
                label.centerYAnchor.constraintEqualToAnchor(container.centerYAnchor),
                label.leadingAnchor.constraintEqualToAnchor(container.leadingAnchor, constant = 8.0),
                label.trailingAnchor.constraintEqualToAnchor(container.trailingAnchor, constant = -8.0)
            ))
            
            container
        },
        modifier = modifier
    )
}
