package mega.privacy.android.feature.sync.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import mega.privacy.android.feature.sync.domain.entity.StalledIssue
import mega.privacy.android.feature.sync.domain.repository.SyncRepository
import javax.inject.Inject

internal class MonitorSyncStalledIssuesUseCase @Inject constructor(
    private val syncRepository: SyncRepository,
) {

    suspend operator fun invoke(): Flow<List<StalledIssue>> =
        syncRepository
            .monitorSyncChanges()
            .map { syncRepository.getSyncStalledIssues() }
            .onStart { emit(syncRepository.getSyncStalledIssues()) }
}