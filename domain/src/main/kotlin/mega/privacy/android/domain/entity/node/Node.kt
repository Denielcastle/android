package mega.privacy.android.domain.entity.node


/**
 * Node
 */
interface Node {

    /**
     * Id
     */
    val id: NodeId

    /**
     * Name
     */
    val name: String

    /**
     * Parent id
     */
    val parentId: NodeId

    /**
     * Base64id
     */
    val base64Id: String

    /**
     * Label
     */
    val label: Int

    /**
     * Has version
     */
    val hasVersion: Boolean

    /**
     * Is favourite
     */
    val isFavourite: Boolean

    /**
     * Exported data if the node is exported (shared with public link), null otherwise
     */
    val exportedData: ExportedData?

    /**
     * Is taken down
     */
    val isTakenDown: Boolean

    /**
     * Is the node an incoming share
     */
    val isIncomingShare: Boolean

    /**
     * Is node key decrypted by verification from owner
     */
    val isNodeKeyDecrypted: Boolean

    /**
     * Creation time
     */
    val creationTime: Long

    /**
     * Serialized string of the node
     * This value should be set only if required and not always for all the nodes
     */
    val serializedData: String?
}

