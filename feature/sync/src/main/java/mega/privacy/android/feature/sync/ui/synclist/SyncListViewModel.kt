package mega.privacy.android.feature.sync.ui.synclist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mega.privacy.android.feature.sync.domain.usecase.GetFolderPairsUseCase
import mega.privacy.android.feature.sync.ui.mapper.SyncUiItemMapper
import mega.privacy.android.feature.sync.ui.synclist.SyncListAction.CardExpanded
import javax.inject.Inject

@HiltViewModel
internal class SyncListViewModel @Inject constructor(
    private val getFolderPairsUseCase: GetFolderPairsUseCase,
    private val syncUiItemMapper: SyncUiItemMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(SyncListState(emptyList()))
    val state: StateFlow<SyncListState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val folders = getFolderPairsUseCase()
                .let(syncUiItemMapper::invoke)

            _state.value = SyncListState(folders)
        }
    }

    fun handleAction(action: SyncListAction) {
        when (action) {
            is CardExpanded -> {
                val syncUiItem = action.syncUiItem
                val expanded = action.expanded

                _state.value = _state.value.copy(
                    syncUiItems = _state.value.syncUiItems.map {
                        if (it.id == syncUiItem.id) {
                            it.copy(expanded = expanded)
                        } else {
                            it
                        }
                    }
                )
            }

            is SyncListAction.RemoveFolderClicked -> {
                // will be implemented later
            }
        }
    }
}