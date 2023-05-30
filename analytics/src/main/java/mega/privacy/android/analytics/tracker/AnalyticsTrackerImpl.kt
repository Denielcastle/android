package mega.privacy.android.analytics.tracker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mega.privacy.android.analytics.event.ScreenView
import mega.privacy.android.analytics.event.TabSelected
import mega.privacy.android.domain.entity.analytics.ScreenViewEventIdentifier
import mega.privacy.android.domain.entity.analytics.TabSelectedEvent
import mega.privacy.android.domain.entity.analytics.TabSelectedEventIdentifier
import mega.privacy.android.domain.qualifier.ApplicationScope
import mega.privacy.android.domain.usecase.analytics.GetViewIdUseCase
import mega.privacy.android.domain.usecase.analytics.TrackEventUseCase
import mega.privacy.android.domain.usecase.analytics.TrackScreenViewUseCase
import timber.log.Timber
import javax.inject.Inject

/**
 * Analytics tracker impl
 *
 * @property appScope
 * @property trackScreenViewUseCase
 */
class AnalyticsTrackerImpl @Inject constructor(
    @ApplicationScope private val appScope: CoroutineScope,
    private val trackScreenViewUseCase: TrackScreenViewUseCase,
    private val trackEventUseCase: TrackEventUseCase,
    private val getViewIdUseCase: GetViewIdUseCase,
) : AnalyticsTracker {

    @Volatile
    private var currentViewId: String? = null

    override fun trackScreenView(screen: ScreenView) {
        appScope.launch {
            val identifier = ScreenViewEventIdentifier(
                name = screen.name,
                uniqueIdentifier = screen.uniqueIdentifier
            )
            val latestViewId = currentViewId
            trackScreenViewUseCase(identifier).also {
                synchronized(this@AnalyticsTrackerImpl) {
                    if (currentViewId == latestViewId) {
                        currentViewId = it
                    }
                }
            }
        }
    }

    override fun trackTabSelected(tab: TabSelected) {
        appScope.launch {

            val identifier = TabSelectedEventIdentifier(
                screenName = tab.screenView.name,
                tabName = tab.name,
                uniqueIdentifier = tab.uniqueIdentifier
            )

            if (currentViewId == null) {
                val newId = getViewIdUseCase()
                synchronized(this@AnalyticsTrackerImpl) {
                    if (currentViewId == null) {
                        currentViewId = newId
                    }
                }
            }
            val latestViewId = currentViewId

            if (latestViewId == null) {
                Timber.w("Tab selected analytics not sent due to null view ID")
            } else {
                trackEventUseCase(TabSelectedEvent(identifier, latestViewId))
            }
        }
    }

}