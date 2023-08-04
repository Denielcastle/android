package mega.privacy.android.app.main.megachat;


import mega.privacy.android.domain.entity.chat.PendingMessage;
import mega.privacy.android.domain.entity.contacts.ContactLink;
import nz.mega.sdk.MegaChatMessage;

public class AndroidMegaChatMessage {

    final public static int CHAT_ADAPTER_SHOW_TIME = 1;
    final public static int CHAT_ADAPTER_SHOW_NOTHING = 0;
    final public static int CHAT_ADAPTER_SHOW_ALL = 2;

    MegaChatMessage message;
    PendingMessage pendingMessage;
    int infoToShow=-1;
    boolean showAvatar = true;
    boolean uploading = false;

    AndroidMegaRichLinkMessage richLinkMessage = null;
    ContactLink contactLinkResult;

    public AndroidMegaChatMessage(MegaChatMessage message) {
        this.message = message;
    }

    public AndroidMegaChatMessage(PendingMessage pendingMessage, boolean uploading) {
        this.pendingMessage = pendingMessage;
        this.uploading = uploading;
    }

    public int getInfoToShow() {
        return infoToShow;
    }

    public void setInfoToShow(int infoToShow) {
        this.infoToShow = infoToShow;
    }

    public MegaChatMessage getMessage() {

        return message;
    }

    public void setMessage(MegaChatMessage message) {
        this.message = message;
    }

    public boolean isUploading() {
        return uploading;
    }

    public void setUploading(boolean uploading) {
        this.uploading = uploading;
    }

    public PendingMessage getPendingMessage() {
        return pendingMessage;
    }

    public void setPendingMessage(PendingMessage pendingMessage) {
        this.pendingMessage = pendingMessage;
    }

    public boolean isShowAvatar() {
        return showAvatar;
    }

    public void setShowAvatar(boolean showAvatar) {
        this.showAvatar = showAvatar;
    }

    public AndroidMegaRichLinkMessage getRichLinkMessage() {
        return richLinkMessage;
    }

    public void setRichLinkMessage(AndroidMegaRichLinkMessage richLinkMessage) {
        this.richLinkMessage = richLinkMessage;
    }

    public ContactLink getContactLinkResult() {
        return contactLinkResult;
    }

    public void setContactLinkResult(ContactLink contactLinkResult) {
        this.contactLinkResult = contactLinkResult;
    }
}
