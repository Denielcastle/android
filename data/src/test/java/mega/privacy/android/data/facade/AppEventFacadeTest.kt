package mega.privacy.android.data.facade

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import mega.privacy.android.data.gateway.AppEventGateway
import mega.privacy.android.domain.entity.transfer.CompletedTransfer
import nz.mega.sdk.MegaTransfer
import org.junit.Before
import org.junit.Test
import kotlin.contracts.ExperimentalContracts

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalContracts
class AppEventFacadeTest {

    private lateinit var underTest: AppEventGateway
    private lateinit var coroutineScope: CoroutineScope

    @Before
    fun setUp() {
        coroutineScope = TestScope(UnconfinedTestDispatcher())
        underTest = AppEventFacade(
            appScope = coroutineScope
        )
    }

    @Test
    fun `test that broadcast upload pause state fires an event`() = runTest {
        underTest.monitorCameraUploadPauseState.test {
            underTest.broadcastUploadPauseState()
            assertThat(awaitItem()).isTrue()
        }
    }

    @Test
    fun `test that broadcast camera upload progress fires an event`() = runTest {
        val expected = Pair(50, 25)
        underTest.monitorCameraUploadProgress.test {
            underTest.broadcastCameraUploadProgress(expected.first, expected.second)

            val actual = awaitItem()
            assertThat(actual).isEqualTo(actual)
        }
    }

    @Test
    fun `test that set SMS Verification Shown set the correct state`() = runTest {
        underTest.setSMSVerificationShown(true)
        assertThat(underTest.isSMSVerificationShown()).isTrue()
        underTest.setSMSVerificationShown(false)
        assertThat(underTest.isSMSVerificationShown()).isFalse()
    }

    @Test
    fun `test that is SMSVerification Shown default value is the correct one`() = runTest {
        assertThat(underTest.isSMSVerificationShown()).isFalse()
    }

    @Test
    fun `test that broadcast completed transfer fires an event`() = runTest {
        val expected = CompletedTransfer(
            fileName = "",
            type = MegaTransfer.TYPE_UPLOAD,
            state = MegaTransfer.STATE_COMPLETED,
            size = "",
            handle = 0L,
            isOffline = false,
            path = "",
            timestamp = 0L,
            error = "",
            originalPath = "",
            parentHandle = 0L
        )
        underTest.monitorCompletedTransfer.test {
            underTest.broadcastCompletedTransfer(expected)

            val actual = awaitItem()
            assertThat(actual).isEqualTo(expected)
        }
    }

}
