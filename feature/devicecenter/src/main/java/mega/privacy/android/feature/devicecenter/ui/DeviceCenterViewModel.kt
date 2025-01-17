package mega.privacy.android.feature.devicecenter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mega.privacy.android.domain.usecase.camerauploads.IsCameraUploadsEnabledUseCase
import mega.privacy.android.feature.devicecenter.domain.usecase.GetDevicesUseCase
import mega.privacy.android.feature.devicecenter.ui.mapper.DeviceUINodeListMapper
import mega.privacy.android.feature.devicecenter.ui.model.DeviceCenterState
import mega.privacy.android.feature.devicecenter.ui.model.DeviceCenterUINode
import mega.privacy.android.feature.devicecenter.ui.model.DeviceUINode
import timber.log.Timber
import javax.inject.Inject

/**
 * [ViewModel] for the Device Center Screen
 *
 * @property getDevicesUseCase [GetDevicesUseCase]
 * @property isCameraUploadsEnabledUseCase [IsCameraUploadsEnabledUseCase]
 * @property deviceUINodeListMapper [DeviceUINodeListMapper]
 */
@HiltViewModel
internal class DeviceCenterViewModel @Inject constructor(
    private val getDevicesUseCase: GetDevicesUseCase,
    private val isCameraUploadsEnabledUseCase: IsCameraUploadsEnabledUseCase,
    private val deviceUINodeListMapper: DeviceUINodeListMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(DeviceCenterState())

    /**
     * The State of [DeviceCenterScreen]
     */
    val state: StateFlow<DeviceCenterState> = _state.asStateFlow()

    init {
        retrieveBackupInfo()
    }

    /**
     * Retrieves the User's Backup Information
     */
    private fun retrieveBackupInfo() = viewModelScope.launch {
        runCatching {
            val isCameraUploadsEnabled = isCameraUploadsEnabledUseCase()
            val backupDevices = getDevicesUseCase(isCameraUploadsEnabled = isCameraUploadsEnabled)
            _state.update {
                it.copy(
                    devices = deviceUINodeListMapper(backupDevices),
                    isCameraUploadsEnabled = isCameraUploadsEnabled,
                )
            }
        }.onFailure {
            Timber.w(it)
        }
    }

    /**
     * Shows the Device Folders of a [DeviceUINode]
     *
     * @param deviceUINode The corresponding Device UI Node
     */
    fun showDeviceFolders(deviceUINode: DeviceUINode) = _state.update {
        it.copy(selectedDevice = deviceUINode)
    }

    /**
     * Handles specific Back Press behavior
     */
    fun handleBackPress() {
        _state.update { it.copy(menuIconClickedNode = null) }
        if (_state.value.deviceToRename != null) {
            // The Rename Device feature is shown. Mark deviceToRename as null to dismiss the feature
            _state.update { it.copy(deviceToRename = null) }
        } else {
            if (_state.value.selectedDevice != null) {
                // The User is in Folder View. Mark selectedDevice as null to go back to Device View
                _state.update { it.copy(selectedDevice = null) }
            } else {
                // The User is in Device View. This will cause the User to leave the Device Center
                _state.update { it.copy(exitFeature = triggered) }
            }
        }
    }

    /**
     * Acknowledges that [DeviceCenterState.exitFeature] has been triggered, and therefore resets
     * its value
     */
    fun resetExitFeature() = _state.update { it.copy(exitFeature = consumed) }

    /**
     * Updates the Menu-icon Clicked Node [DeviceCenterState.menuIconClickedNode]
     *
     * @param menuIconClickedNode The Menu-icon Clicked Node
     */
    fun setMenuClickedNode(menuIconClickedNode: DeviceCenterUINode) =
        _state.update { it.copy(menuIconClickedNode = menuIconClickedNode) }

    /**
     * Updates the value of [DeviceCenterState.deviceToRename]
     *
     * @param deviceToRename The Device to be renamed by the User
     */
    fun setDeviceToRename(deviceToRename: DeviceUINode) =
        _state.update { it.copy(deviceToRename = deviceToRename) }

    /**
     * Resets the value of [DeviceCenterState.deviceToRename] back to null
     */
    fun resetDeviceToRename() = _state.update { it.copy(deviceToRename = null) }
}