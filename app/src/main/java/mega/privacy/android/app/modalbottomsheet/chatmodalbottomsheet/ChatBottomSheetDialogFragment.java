package mega.privacy.android.app.modalbottomsheet.chatmodalbottomsheet;

import static mega.privacy.android.app.utils.AvatarUtil.getAvatarBitmap;
import static mega.privacy.android.app.utils.AvatarUtil.getColorAvatar;
import static mega.privacy.android.app.utils.AvatarUtil.getDefaultAvatar;
import static mega.privacy.android.app.utils.AvatarUtil.getSpecificAvatarColor;
import static mega.privacy.android.app.utils.ChatUtil.StatusIconLocation;
import static mega.privacy.android.app.utils.ChatUtil.createMuteNotificationsAlertDialogOfAChat;
import static mega.privacy.android.app.utils.ChatUtil.getTitleChat;
import static mega.privacy.android.app.utils.ChatUtil.isEnableChatNotifications;
import static mega.privacy.android.app.utils.ChatUtil.setContactStatus;
import static mega.privacy.android.app.utils.ChatUtil.shouldMuteOrUnmuteOptionsBeShown;
import static mega.privacy.android.app.utils.ChatUtil.showConfirmationClearChat;
import static mega.privacy.android.app.utils.ChatUtil.showConfirmationLeaveChat;
import static mega.privacy.android.app.utils.Constants.AVATAR_GROUP_CHAT_COLOR;
import static mega.privacy.android.app.utils.Constants.AVATAR_SIZE;
import static mega.privacy.android.app.utils.Constants.CHAT_ID;
import static mega.privacy.android.app.utils.Constants.HANDLE;
import static mega.privacy.android.app.utils.Constants.MAX_WIDTH_BOTTOM_SHEET_DIALOG_LAND;
import static mega.privacy.android.app.utils.Constants.MAX_WIDTH_BOTTOM_SHEET_DIALOG_PORT;
import static mega.privacy.android.app.utils.Constants.NOTIFICATIONS_ENABLED;
import static mega.privacy.android.app.utils.TextUtil.isTextEmpty;
import static mega.privacy.android.app.utils.Util.dp2px;
import static mega.privacy.android.app.utils.Util.isScreenInPortrait;
import static mega.privacy.android.app.utils.Util.scaleHeightPx;
import static mega.privacy.android.app.utils.Util.scaleWidthPx;
import static nz.mega.sdk.MegaApiJava.INVALID_HANDLE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import mega.privacy.android.app.MegaApplication;
import mega.privacy.android.app.R;
import mega.privacy.android.app.components.RoundedImageView;
import mega.privacy.android.app.components.twemoji.EmojiTextView;
import mega.privacy.android.app.main.ContactInfoActivity;
import mega.privacy.android.app.main.ManagerActivity;
import mega.privacy.android.app.main.controllers.ChatController;
import mega.privacy.android.app.main.megachat.ArchivedChatsActivity;
import mega.privacy.android.app.main.megachat.ChatItemPreferences;
import mega.privacy.android.app.main.megachat.GroupChatInfoActivity;
import mega.privacy.android.app.modalbottomsheet.BaseBottomSheetDialogFragment;
import nz.mega.sdk.MegaApiJava;
import nz.mega.sdk.MegaChatListItem;
import nz.mega.sdk.MegaChatMessage;
import nz.mega.sdk.MegaChatRoom;
import nz.mega.sdk.MegaUser;
import timber.log.Timber;

public class ChatBottomSheetDialogFragment extends BaseBottomSheetDialogFragment implements View.OnClickListener {

    private ChatController chatC;
    private MegaChatListItem chat = null;
    private long chatId;
    private RoundedImageView chatImageView;
    private TextView optionMuteChatText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = View.inflate(getContext(), R.layout.chat_item_bottom_sheet, null);
        itemsLayout = contentView.findViewById(R.id.items_layout);

        if (savedInstanceState != null) {
            chatId = savedInstanceState.getLong(CHAT_ID, INVALID_HANDLE);
        } else if (requireActivity() instanceof ManagerActivity) {
            chatId = ((ManagerActivity) requireActivity()).selectedChatItemId;
        } else if (requireActivity() instanceof ArchivedChatsActivity) {
            chatId = ((ArchivedChatsActivity) requireActivity()).selectedChatItemId;
        }

        if (chatId != INVALID_HANDLE) {
            chat = megaChatApi.getChatListItem(chatId);
        }

        chatC = new ChatController(requireActivity());

        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView iconStateChatPanel = contentView.findViewById(R.id.chat_list_contact_state);
        iconStateChatPanel.setMaxWidth(scaleWidthPx(6, getResources().getDisplayMetrics()));
        iconStateChatPanel.setMaxHeight(scaleHeightPx(6, getResources().getDisplayMetrics()));

        EmojiTextView titleNameContactChatPanel = contentView.findViewById(R.id.chat_list_chat_name_text);
        TextView titleMailContactChatPanel = contentView.findViewById(R.id.chat_list_chat_mail_text);
        chatImageView = contentView.findViewById(R.id.sliding_chat_list_thumbnail);
        TextView infoChatText = contentView.findViewById(R.id.chat_list_info_chat_text);
        LinearLayout optionInfoChat = contentView.findViewById(R.id.chat_list_info_chat_layout);
        LinearLayout optionLeaveChat = contentView.findViewById(R.id.chat_list_leave_chat_layout);
        TextView optionLeaveText = contentView.findViewById(R.id.chat_list_leave_chat_text);
        LinearLayout optionClearHistory = contentView.findViewById(R.id.chat_list_clear_history_chat_layout);
        LinearLayout optionMuteChat = contentView.findViewById(R.id.chat_list_mute_chat_layout);
        ImageView optionMuteChatIcon = contentView.findViewById(R.id.chat_list_mute_chat_image);
        optionMuteChatText = contentView.findViewById(R.id.chat_list_mute_chat_text);
        LinearLayout optionArchiveChat = contentView.findViewById(R.id.chat_list_archive_chat_layout);
        TextView archiveChatText = contentView.findViewById(R.id.chat_list_archive_chat_text);
        ImageView archiveChatIcon = contentView.findViewById(R.id.file_archive_chat_image);

        if (isScreenInPortrait(requireContext())) {
            titleNameContactChatPanel.setMaxWidthEmojis(dp2px(MAX_WIDTH_BOTTOM_SHEET_DIALOG_PORT));
            titleMailContactChatPanel.setMaxWidth(dp2px(MAX_WIDTH_BOTTOM_SHEET_DIALOG_PORT));
        } else {
            titleNameContactChatPanel.setMaxWidthEmojis(dp2px(MAX_WIDTH_BOTTOM_SHEET_DIALOG_LAND));
            titleMailContactChatPanel.setMaxWidth(dp2px(MAX_WIDTH_BOTTOM_SHEET_DIALOG_LAND));
        }

        optionInfoChat.setOnClickListener(this);
        optionMuteChat.setOnClickListener(this);
        optionLeaveChat.setOnClickListener(this);
        optionClearHistory.setOnClickListener(this);
        optionArchiveChat.setOnClickListener(this);

        LinearLayout separatorInfo = contentView.findViewById(R.id.separator_info);

        titleNameContactChatPanel.setText(getTitleChat(chat));

        if (!shouldMuteOrUnmuteOptionsBeShown(requireActivity(), megaChatApi.getChatRoom(chat.getChatId()))) {
            optionMuteChat.setVisibility(View.GONE);
        }

        if (chat.isPreview()) {
            titleMailContactChatPanel.setText(getString(R.string.group_chat_label));
            iconStateChatPanel.setVisibility(View.GONE);
            addAvatarChatPanel(null, chat);

            infoChatText.setText(getString(R.string.general_info));

            if (megaApi != null && megaApi.getRootNode() != null) {
                optionInfoChat.setVisibility(View.VISIBLE);
                separatorInfo.setVisibility(View.VISIBLE);
            } else {
                optionInfoChat.setVisibility(View.GONE);
                separatorInfo.setVisibility(View.GONE);
            }

            optionLeaveChat.setVisibility(View.VISIBLE);
            optionLeaveText.setText("Remove preview");
            optionClearHistory.setVisibility(View.GONE);
            optionArchiveChat.setVisibility(View.GONE);
        } else {
            if (chat.isGroup()) {
                titleMailContactChatPanel.setText(getString(R.string.group_chat_label));
                iconStateChatPanel.setVisibility(View.GONE);
                addAvatarChatPanel(null, chat);

                infoChatText.setText(getString(R.string.general_info));
                optionInfoChat.setVisibility(View.VISIBLE);

                if (chat.isActive()) {
                    optionLeaveChat.setVisibility(View.VISIBLE);
                } else {
                    optionLeaveChat.setVisibility(View.GONE);
                }

                if ((chat.getLastMessageType() == MegaChatMessage.TYPE_INVALID) || (chat.getLastMessageType() == MegaChatMessage.TYPE_TRUNCATE)) {
                    optionClearHistory.setVisibility(View.GONE);
                } else if (chat.isActive() && chat.getOwnPrivilege() == MegaChatRoom.PRIV_MODERATOR) {
                    optionClearHistory.setVisibility(View.VISIBLE);
                } else {
                    optionClearHistory.setVisibility(View.GONE);
                }
            } else {
                iconStateChatPanel.setVisibility(View.VISIBLE);

                long userHandle = chat.getPeerHandle();
                MegaUser contact = megaApi.getContact(MegaApiJava.userHandleToBase64(userHandle));

                if ((chat.getLastMessageType() == MegaChatMessage.TYPE_INVALID) || (chat.getLastMessageType() == MegaChatMessage.TYPE_TRUNCATE)) {
                    optionClearHistory.setVisibility(View.GONE);
                } else if (chat.isActive()) {
                    optionClearHistory.setVisibility(View.VISIBLE);
                } else {
                    optionClearHistory.setVisibility(View.GONE);
                }

                if (contact != null) {
                    addAvatarChatPanel(contact.getEmail(), chat);

                    if (contact.getVisibility() == MegaUser.VISIBILITY_VISIBLE) {
                        optionInfoChat.setVisibility(View.VISIBLE);
                        infoChatText.setText(getString(R.string.general_info));
                        titleMailContactChatPanel.setText(contact.getEmail());
                    } else {
                        optionInfoChat.setVisibility(View.GONE);
                        optionClearHistory.setVisibility(View.GONE);
                        titleMailContactChatPanel.setVisibility(View.GONE);
                    }
                } else {
                    optionInfoChat.setVisibility(View.GONE);
                    optionClearHistory.setVisibility(View.GONE);
                }

                optionLeaveChat.setVisibility(View.GONE);
                setContactStatus(megaChatApi.getUserOnlineStatus(userHandle), iconStateChatPanel, StatusIconLocation.DRAWER);
            }

            if (isEnableChatNotifications(chat.getChatId())) {
                optionMuteChatIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_mute));
                optionMuteChatText.setText(getString(R.string.general_mute));
            } else {
                optionMuteChatIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_unmute));
                optionMuteChatText.setText(getString(R.string.general_unmute));
            }

            ChatItemPreferences chatPrefs = dbH.findChatPreferencesByHandle(Long.toString(chat.getChatId()));
            if (chatPrefs == null) {
                MegaChatRoom chatRoom = megaChatApi.getChatRoomByUser(chat.getPeerHandle());
                if (chatRoom != null) {
                    String email = chatC.getParticipantEmail(chatRoom.getPeerHandle(0));
                    titleMailContactChatPanel.setText(email);
                    addAvatarChatPanel(email, chat);
                }
            }
            if (chat.isArchived()) {
                archiveChatText.setText(getString(R.string.unarchive_chat_option));
                archiveChatIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_unarchive));
                optionInfoChat.setVisibility(View.GONE);
                optionMuteChat.setVisibility(View.GONE);
                optionLeaveChat.setVisibility(View.GONE);
                optionClearHistory.setVisibility(View.GONE);
            } else {
                archiveChatText.setText(getString(R.string.archive_chat_option));
                archiveChatIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_archive));
            }

            if (optionInfoChat.getVisibility() == View.GONE) {
                separatorInfo.setVisibility(View.GONE);
            }
        }

        super.onViewCreated(view, savedInstanceState);
    }

    private void addAvatarChatPanel(String contactMail, MegaChatListItem chat) {
        Bitmap bitmap = getAvatarBitmap(contactMail);

        if (bitmap != null) {
            chatImageView.setImageBitmap(bitmap);
        } else {
            int color;
            String name = null;

            if (!isTextEmpty(getTitleChat(chat))) {
                name = getTitleChat(chat);
            } else if (!isTextEmpty(contactMail)) {
                name = contactMail;
            }

            if (chat.isGroup()) {
                color = getSpecificAvatarColor(AVATAR_GROUP_CHAT_COLOR);
            } else {
                color = getColorAvatar(megaApi.getContact(contactMail));
            }

            chatImageView.setImageBitmap(getDefaultAvatar(color, name, AVATAR_SIZE, false));
        }
    }

    @Override
    public void onClick(View v) {
        if (chat == null) {
            Timber.d("Selected chat NULL");
            return;
        }

        int id = v.getId();
        if (id == R.id.chat_list_info_chat_layout) {
            if (chat.isGroup()) {
                Intent i = new Intent(requireContext(), GroupChatInfoActivity.class);
                i.putExtra(HANDLE, chat.getChatId());
                startActivity(i);
            } else {
                Intent i = new Intent(requireContext(), ContactInfoActivity.class);
                i.putExtra(HANDLE, chat.getChatId());
                startActivity(i);
            }
        } else if (id == R.id.chat_list_leave_chat_layout) {
            Timber.d("Leave chat - Chat ID: %s", chat.getChatId());

            if (requireActivity() instanceof ManagerActivity) {
                showConfirmationLeaveChat(requireActivity(), chat.getChatId(), ((ManagerActivity) requireActivity()));
            }
        } else if (id == R.id.chat_list_clear_history_chat_layout) {
            Timber.d("Clear chat - Chat ID: %s", chat.getChatId());
            showConfirmationClearChat(((ManagerActivity) requireActivity()), megaChatApi.getChatRoom(chat.getChatId()));
        } else if (id == R.id.chat_list_mute_chat_layout) {
            if (requireActivity() instanceof ManagerActivity) {
                if (optionMuteChatText.getText().equals(getString(R.string.general_mute))) {
                    createMuteNotificationsAlertDialogOfAChat(requireActivity(), chat.getChatId());
                } else {
                    MegaApplication.getPushNotificationSettingManagement().controlMuteNotificationsOfAChat(requireActivity(), NOTIFICATIONS_ENABLED, chat.getChatId());
                }
            }
        } else if (id == R.id.chat_list_archive_chat_layout) {
            chatC.archiveChat(chat);
        }

        setStateBottomSheetBehaviorHidden();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(CHAT_ID, chatId);
    }
}
