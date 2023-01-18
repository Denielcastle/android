package mega.privacy.android.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import mega.privacy.android.domain.repository.FileRepository
import mega.privacy.android.domain.repository.StreamingServerRepository
import mega.privacy.android.domain.usecase.GetLocalFileForNode
import mega.privacy.android.domain.usecase.streaming.GetStreamingUriStringForNode
import mega.privacy.android.domain.usecase.streaming.StartStreamingServer
import mega.privacy.android.domain.usecase.streaming.StopStreamingServer

/**
 * Shared use case module
 *
 * Provides use case is shared with other components
 */
@Module
@DisableInstallInCheck
internal abstract class InternalOpenFileUseCasesModule {

    companion object {
        @Provides
        fun provideGetLocalFileForNode(fileRepository: FileRepository): GetLocalFileForNode =
            GetLocalFileForNode(fileRepository::getLocalFile)

        @Provides
        fun provideStartStreamingServer(repository: StreamingServerRepository): StartStreamingServer =
            StartStreamingServer(repository::startServer)

        @Provides
        fun provideStopStreamingServer(repository: StreamingServerRepository): StopStreamingServer =
            StopStreamingServer(repository::stopServer)

        @Provides
        fun provideGetStreamingUriStringForNode(repository: FileRepository): GetStreamingUriStringForNode {
            return GetStreamingUriStringForNode(repository::getFileStreamingUri)
        }
    }
}