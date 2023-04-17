package mega.privacy.android.app.presentation.meeting

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mega.privacy.android.app.activities.PasscodeActivity
import mega.privacy.android.app.interfaces.SnackbarShower
import mega.privacy.android.app.presentation.extensions.changeStatusBarColor
import mega.privacy.android.app.presentation.extensions.isDarkMode
import mega.privacy.android.app.presentation.meeting.model.ScheduleMeetingAction
import mega.privacy.android.app.presentation.security.PasscodeCheck
import mega.privacy.android.domain.entity.ThemeMode
import mega.privacy.android.domain.usecase.GetThemeMode
import mega.privacy.android.core.ui.theme.AndroidTheme
import timber.log.Timber
import javax.inject.Inject
import mega.privacy.android.app.presentation.meeting.view.ScheduleMeetingView

/**
 * Activity which shows scheduled meeting info screen.
 *
 * @property passCodeFacade [PasscodeCheck]
 * @property getThemeMode   [GetThemeMode]
 */
@AndroidEntryPoint
class ScheduleMeetingActivity : PasscodeActivity(), SnackbarShower {

    @Inject
    lateinit var passCodeFacade: PasscodeCheck

    @Inject
    lateinit var getThemeMode: GetThemeMode

    private val viewModel by viewModels<ScheduleMeetingViewModel>()

    /**
     * Perform Activity initialization
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                }
            }
        }

        setContent { ScheduleMeetingComposeView() }
    }

    @Composable
    private fun ScheduleMeetingComposeView() {
        val themeMode by getThemeMode().collectAsState(initial = ThemeMode.System)
        val isDark = themeMode.isDarkMode()
        val uiState by viewModel.state.collectAsState()
        AndroidTheme(isDark = isDark) {
            ScheduleMeetingView(
                state = uiState,
                onButtonClicked = ::onActionTap,
                onDiscardClicked = { viewModel.onDiscardMeetingTap() },
                onAcceptClicked = { },
                onStartTimeClicked = { },
                onStartDateClicked = { },
                onEndTimeClicked = { },
                onEndDateClicked = { },
                onScrollChange = { scrolled -> this.changeStatusBarColor(scrolled, isDark) },
                onDismiss = { viewModel.dismissDialog() },
                onSnackbarShown = {},
                onDiscardMeetingDialog = { finish() },
            )
        }
    }

    /**
     * Tap in a button action
     */
    private fun onActionTap(action: ScheduleMeetingAction) {
        when (action) {
            ScheduleMeetingAction.Recurrence -> Timber.d("Recurrence option")
            ScheduleMeetingAction.MeetingLink -> viewModel.onMeetingLinkTap()
            ScheduleMeetingAction.AddParticipants -> Timber.d("Add participants option")
            ScheduleMeetingAction.SendCalendarInvite -> Timber.d("Send calendar invite option")
            ScheduleMeetingAction.AllowNonHostAddParticipants -> viewModel.onAllowNonHostAddParticipantsTap()
            ScheduleMeetingAction.AddDescription -> Timber.d("Add description option")
        }
    }
}