package mega.privacy.android.app.fetcher

import android.webkit.MimeTypeMap
import coil.ImageLoader
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.request.Options
import mega.privacy.android.domain.usecase.thumbnailpreview.GetThumbnailUseCase
import okio.Path.Companion.toOkioPath

/**
 * Mega thumbnail fetcher to load thumbnails from normal MegaNode
 */
internal class MegaThumbnailFetcher(
    private val request: ThumbnailRequest,
    private val getThumbnailUseCase: dagger.Lazy<GetThumbnailUseCase>,
) : Fetcher {
    override suspend fun fetch(): FetchResult {
        val file = getThumbnailUseCase.get()(request.id.longValue, true)
            ?: throw NullPointerException("Thumbnail file is null")
        return SourceResult(
            source = ImageSource(file = file.toOkioPath()),
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension),
            dataSource = DataSource.DISK
        )
    }

    /**
     * Factory
     */
    class Factory(
        private val getThumbnailUseCase: dagger.Lazy<GetThumbnailUseCase>,
    ) : Fetcher.Factory<ThumbnailRequest> {

        override fun create(
            data: ThumbnailRequest,
            options: Options,
            imageLoader: ImageLoader,
        ): Fetcher? {
            if (!isApplicable(data)) return null
            return MegaThumbnailFetcher(data, getThumbnailUseCase)
        }

        private fun isApplicable(data: ThumbnailRequest): Boolean {
            return data.id.longValue > 0
        }
    }
}