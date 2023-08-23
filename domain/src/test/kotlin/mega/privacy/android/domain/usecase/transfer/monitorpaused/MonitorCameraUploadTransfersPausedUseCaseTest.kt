package mega.privacy.android.domain.usecase.transfer.monitorpaused

import com.google.common.truth.Truth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import mega.privacy.android.domain.entity.transfer.Transfer
import mega.privacy.android.domain.entity.transfer.TransferType
import mega.privacy.android.domain.repository.TransferRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MonitorCameraUploadTransfersPausedUseCaseTest {
    private lateinit var underTest: MonitorCameraUploadTransfersPausedUseCase

    private val transferRepository = mock<TransferRepository>()

    @BeforeAll
    fun setup() {
        underTest = MonitorCameraUploadTransfersPausedUseCase(transferRepository)
    }

    @BeforeEach
    fun resetMocks() {
        reset(transferRepository)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 5, 99])
    fun `test that totalPendingIndividualTransfers returns getNumPendingCameraUploads from repository`(
        value: Int,
    ) = runTest {
        whenever(transferRepository.getNumPendingCameraUploads()).thenReturn(value)
        assertThat(underTest.totalPendingIndividualTransfers()).isEqualTo(value)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 5, 99])
    fun `test that totalPausedIndividualTransfers returns getNumPendingPausedCameraUploads from repository`(
        value: Int,
    ) = runTest {
        whenever(transferRepository.getNumPendingPausedCameraUploads()).thenReturn(value)
        assertThat(underTest.totalPausedIndividualTransfers()).isEqualTo(value)
    }

    @ParameterizedTest
    @MethodSource("getTransfers")
    fun `test that transfers are filtered correctly`(transfer: Transfer, isCorrect: Boolean) {
        assertThat(underTest.isCorrectType(transfer)).isEqualTo(isCorrect)
    }

    private fun getTransfers() = listOf(
        Arguments.of(mockTransfer(TransferType.TYPE_DOWNLOAD), false),
        Arguments.of(mockTransfer(TransferType.TYPE_UPLOAD), true),
        Arguments.of(
            mockTransfer(TransferType.TYPE_UPLOAD, isCameraUpload = false, isChatUpload = true),
            false
        ),
        Arguments.of(mockTransfer(TransferType.TYPE_UPLOAD, isCameraUpload = false), false),
        Arguments.of(mockTransfer(TransferType.NONE), false),
    )

    private fun mockTransfer(
        type: TransferType,
        isChatUpload: Boolean = false,
        isCameraUpload: Boolean = true,
    ) = mock<Transfer> {
        on { it.transferType }.thenReturn(type)
        on { it.isChatUpload() }.thenReturn(isChatUpload)
        on { it.isCUUpload() }.thenReturn(isCameraUpload)
    }

}