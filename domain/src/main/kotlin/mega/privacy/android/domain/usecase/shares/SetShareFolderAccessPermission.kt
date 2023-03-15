package mega.privacy.android.domain.usecase.shares

import mega.privacy.android.domain.entity.node.FolderNode
import mega.privacy.android.domain.entity.shares.AccessPermission
import mega.privacy.android.domain.exception.ShareAccessNotSetException

/**
 * Add, update or remove(access == UNKNOWN) a folder's outgoing shared access for one or several users
 */
fun interface SetShareFolderAccessPermission {
    /**
     * Add, update or remove(access == UNKNOWN) a folder's outgoing shared access for one or several users
     * @param folderNode the [FolderNode] we want to change permission
     * @param accessPermission [AccessPermission] that will be set
     * @param emails of the users we want to set the permission for this node
     * @throws [ShareAccessNotSetException] with the proper information of how many users have errors
     */
    suspend operator fun invoke(
        folderNode: FolderNode,
        accessPermission: AccessPermission,
        vararg emails: String,
    )
}