package com.example.instagram.adapter

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.R
import com.example.instagram.interfac.PersonalVideo
import com.example.instagram.model.UploadVideo

class PersonalVideoAdapter(val context: Context, private val items: ArrayList<UploadVideo>, val onClick: PersonalVideo):
    RecyclerView.Adapter<PersonalVideoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val imageView : ImageView = itemView.findViewById(R.id.personalVideoShow)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.personal_video_adapter, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p0 = items[position]

        Glide.with(context).load(p0.videoUri).into(holder.imageView)

        holder.imageView.setOnClickListener {

            val dialog = AlertDialog.Builder(context)
            val layout = LayoutInflater.from(context).inflate(R.layout.video_view_reels, null, false)
            dialog.setView(layout)
            val create = dialog.create()
            create.show()
            val videoView : VideoView = layout.findViewById(R.id.videoView)
            val username : TextView = layout.findViewById(R.id.videoName)
            val postTime: TextView = layout.findViewById(R.id.postTimeShow)
            val moreVert: ImageView = layout.findViewById(R.id.moreVertOnVideo)
            val likeIcon: ImageView = layout.findViewById(R.id.likeVideo)
            val likeCount: TextView = layout.findViewById(R.id.countLikeTextV)
            val commentIcon: ImageView = layout.findViewById(R.id.commentVideo)
            val countComment: TextView = layout.findViewById(R.id.countCommentsTxtV)
            val desc: TextView = layout.findViewById(R.id.showVideoDesc)

            desc.text = p0.description

            //set video view
            videoView.setVideoURI(Uri.parse(p0.videoUri))
            videoView.requestFocus()
            videoView.setOnPreparedListener {
                it.start()
            }
            postTime.text = postTime.text.toString() + p0.time.toString()
            username.text = p0.username

        }

        holder.imageView.setOnLongClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Alert!")
            dialog.setMessage("sure do you want to delete this video permanently!")
            dialog.setNegativeButton("Cancel"){_,_ ->}
            dialog.setPositiveButton("Delete"){_,_ ->

                if (p0.videoImageName != null && p0.id != null){
                    onClick.postDeleteFun(p0.videoImageName,p0.id)
                }
            }
            dialog.setNeutralButton("Edit Video"){_,_ ->
                onClick.postEditFun(p0.videoImageName.toString(), p0.videoUri.toString(), p0.description.toString(), p0.username.toString(), p0.time.toString(), p0.id.toString())
            }
            val create = dialog.create()
            create.show()
            true
        }
    }
}