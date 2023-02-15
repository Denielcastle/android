package mega.privacy.android.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import mega.privacy.android.domain.repository.NodeRepository
import mega.privacy.android.domain.usecase.IsNodeInInbox
import mega.privacy.android.domain.usecase.filenode.GetFileHistoryNumVersions

/**
 * module to provide FileNode modules
 */
@Module
@DisableInstallInCheck
object InternalFileNodeModule {
    /**
     * provides [GetFileHistoryNumVersions]
     */
    @Provides
    fun providesGetFileHistoryNumVersions(nodeRepository: NodeRepository): GetFileHistoryNumVersions =
        GetFileHistoryNumVersions(nodeRepository::getNodeHistoryNumVersions)

    /**
     * provides [IsNodeInInbox]
     */
    @Provides
    fun provideIsNodeInInbox(nodeRepository: NodeRepository): IsNodeInInbox =
        IsNodeInInbox(nodeRepository::isNodeInInbox)
}