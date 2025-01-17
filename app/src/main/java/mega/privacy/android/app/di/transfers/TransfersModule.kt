package mega.privacy.android.app.di.transfers

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mega.privacy.android.app.presentation.transfers.notification.DefaultDownloadNotificationMapper
import mega.privacy.android.data.mapper.transfer.DownloadNotificationMapper

@Module
@InstallIn(SingletonComponent::class)
abstract class TransfersModule {

    /**
     * Binds [DownloadNotificationMapper] to its default implementation [DefaultDownloadNotificationMapper]
     * @param mapper [DefaultDownloadNotificationMapper]
     * @return default [DownloadNotificationMapper]
     */
    @Binds
    abstract fun bindDownloadNotificationMapper(mapper: DefaultDownloadNotificationMapper): DownloadNotificationMapper
}
