package mega.privacy.android.app.meeting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import mega.privacy.android.app.MegaApplication
import mega.privacy.android.app.R
import mega.privacy.android.app.databinding.ItemAssignModeratorBinding
import mega.privacy.android.app.meeting.fragments.InMeetingViewModel
import mega.privacy.android.app.utils.CallUtil

class AssignParticipantsAdapter(
    private val inMeetingViewModel: InMeetingViewModel,
    private val select: ((Int) -> Unit)
) :
    ListAdapter<Participant, AssignParticipantsAdapter.AssignParticipantViewHolder>(
        AssignParticipantDiffCallback()
    ) {
    override fun onBindViewHolder(holder: AssignParticipantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignParticipantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AssignParticipantViewHolder(
            ItemAssignModeratorBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    fun updateList(list: MutableList<Participant>?) {
        list?.let {
            val newList = mutableListOf<Participant>()
            newList.addAll(it)
            submitList(newList)
        }
    }

    inner class AssignParticipantViewHolder(private val binding: ItemAssignModeratorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(participant: Participant) {
            binding.name.text = participant.name

            if (participant.isChosenForAssign) {
                binding.avatar.setActualImageResource(R.drawable.ic_select_folder)
            } else {
                // Set actual avatar
                initAvatar(participant, binding.avatar)
            }

            binding.assignLayout.setOnClickListener {
                select.invoke(adapterPosition)
            }
        }
    }

    private fun initAvatar(participant: Participant, avatarView: SimpleDraweeView) {
        inMeetingViewModel.getChat().let {
            var avatar = CallUtil.getImageAvatarCall(it, participant.peerId)
            if (avatar == null) {
                avatar = CallUtil.getDefaultAvatarCall(
                    MegaApplication.getInstance().applicationContext,
                    participant.peerId
                )
            }

            avatarView.setImageBitmap(avatar)
        }
    }
}


class AssignParticipantDiffCallback : DiffUtil.ItemCallback<Participant>() {
    override fun areItemsTheSame(oldItem: Participant, newItem: Participant): Boolean =
        (oldItem.peerId == newItem.peerId) && (oldItem.isChosenForAssign == newItem.isChosenForAssign)

    override fun areContentsTheSame(oldItem: Participant, newItem: Participant): Boolean =
        oldItem == newItem
}
