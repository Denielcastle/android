package mega.privacy.android.domain.usecase

/**
 * The use case of getting local folder for folder link from mega api folder
 */
fun interface GetLocalFolderLinkFromMegaApiFolder {

    /**
     * Get local folder for folder link from mega api folder
     *
     * @param handle mega handle of current item
     * @return folder link
     */
    suspend operator fun invoke(handle: Long): String?
}