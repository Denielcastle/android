package mega.privacy.android.domain.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import mega.privacy.android.domain.repository.NodeRepository
import mega.privacy.android.domain.usecase.DefaultGetFolderTreeInfo
import mega.privacy.android.domain.usecase.DefaultGetNodeById
import mega.privacy.android.domain.usecase.DefaultMonitorChildrenUpdates
import mega.privacy.android.domain.usecase.DefaultMonitorNodeUpdatesById
import mega.privacy.android.domain.usecase.GetFolderTreeInfo
import mega.privacy.android.domain.usecase.GetNodeById
import mega.privacy.android.domain.usecase.IsNodeInInbox
import mega.privacy.android.domain.usecase.MonitorChildrenUpdates
import mega.privacy.android.domain.usecase.MonitorNodeUpdatesById
import mega.privacy.android.domain.usecase.filenode.GetFileHistoryNumVersions
import mega.privacy.android.domain.usecase.filenode.GetNodeVersionsByHandle
import mega.privacy.android.domain.usecase.filenode.MonitorSecurityUpgrade
import mega.privacy.android.domain.usecase.filenode.SetSecurityUpgrade

/**
 * module to provide FileNode modules
 */
@Module
@DisableInstallInCheck
abstract class InternalNodeModule {

    /**
     * provides default GetFolderInfo
     */
    @Binds
    abstract fun bindGetFolderVersionInfoByHandle(implementation: DefaultGetFolderTreeInfo): GetFolderTreeInfo

    /**
     * Provide implementation for [GetNodeById]
     */
    @Binds
    abstract fun bindGetNodeById(implementation: DefaultGetNodeById): GetNodeById

    /**
     * Provide implementation for [MonitorNodeUpdatesById]
     */
    @Binds
    abstract fun bindMonitorNodeUpdatesById(implementation: DefaultMonitorNodeUpdatesById): MonitorNodeUpdatesById

    /**
     * Provide implementation for [MonitorChildrenUpdates]
     */
    @Binds
    abstract fun bindMonitorFolderChildrenUpdates(implementation: DefaultMonitorChildrenUpdates): MonitorChildrenUpdates

    companion object {
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

        /**
         * provides [GetNodeVersionsByHandle]
         */
        @Provides
        fun provideGetNodeVersionsByHandle(nodeRepository: NodeRepository): GetNodeVersionsByHandle =
            GetNodeVersionsByHandle(nodeRepository::getNodeHistoryVersions)

        @Provides
        fun provideMonitorSecurityUpgrade(nodeRepository: NodeRepository): MonitorSecurityUpgrade =
            MonitorSecurityUpgrade(nodeRepository::monitorSecurityUpgrade)

        @Provides
        fun provideSetSecurityUpgrade(nodeRepository: NodeRepository): SetSecurityUpgrade =
            SetSecurityUpgrade(nodeRepository::setUpgradeSecurity)
    }
}