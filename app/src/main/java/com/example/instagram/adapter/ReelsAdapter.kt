package com.example.instagram.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.model.UploadVideo

class ReelsAdapter(val context: Context, private val items : ArrayList<UploadVideo>): RecyclerView.Adapter<ReelsAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val videoView : VideoView = itemView.findViewById(R.id.videoView)
        val username : TextView = itemView.findViewById(R.id.videoName)
        val postTime: TextView = itemView.findViewById(R.id.postTimeShow)
        val moreVert: ImageView = itemView.findViewById(R.id.moreVertOnVideo)
        val likeIcon: ImageView = itemView.findViewById(R.id.likeVideo)
        val likeCount: TextView = itemView.findViewById(R.id.countLikeTextV)
        val commentIcon: ImageView = itemView.findViewById(R.id.commentVideo)
        val countComment: TextView = itemView.findViewById(R.id.countCommentsTxtV)
        val description: TextView = itemView.findViewById(R.id.showVideoDesc)
        val showMoreLess: TextView = itemView.findViewById(R.id.showMoreLess)

        fun bind(videoUri: String){

//            val mediaController = MediaController(itemView.context)
//            mediaController.setAnchorView(videoView)
            videoView.setVideoURI(Uri.parse(videoUri))
//            videoView.setMediaController(mediaController)
            videoView.requestFocus()
//            videoView.start()

            videoView.setOnPreparedListener { mp ->
                mp.start()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.video_view_reels, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val p0 = items[position]
        holder.bind(p0.videoUri!!)
        
        holder.videoView.setOnClickListener {

            if (holder.videoView.isPlaying){
                holder.videoView.pause()
            }else{
                holder.videoView.start()
            }
        }

        // change visibility
        var clickOnVideo = false
        holder.videoView.setOnLongClickListener {

            clickOnVideo = !clickOnVideo
            if (clickOnVideo){

                holder.username.visibility = View.INVISIBLE
                holder.postTime.visibility = View.INVISIBLE
                holder.moreVert.visibility = View.INVISIBLE
                holder.likeIcon.visibility = View.INVISIBLE
                holder.likeCount.visibility = View.INVISIBLE
                holder.commentIcon.visibility = View.INVISIBLE
                holder.countComment.visibility = View.INVISIBLE

            }else{

                holder.username.visibility = View.VISIBLE
                holder.postTime.visibility = View.VISIBLE
                holder.moreVert.visibility = View.VISIBLE
                holder.likeIcon.visibility = View.VISIBLE
                holder.likeCount.visibility = View.VISIBLE
                holder.commentIcon.visibility = View.VISIBLE
                holder.countComment.visibility = View.VISIBLE
            }
            false
        }

        var changeLike = false
        holder.likeIcon.setOnClickListener {
            changeLike = !changeLike
            if (changeLike) {
                holder.likeIcon.setImageResource(R.drawable.red_like_icon)
                holder.likeCount.text = (holder.likeCount.text.toString().toInt() + 1).toString()
            }else{
                holder.likeIcon.setImageResource(R.drawable.like_icon)
                holder.likeCount.text = (holder.likeCount.text.toString().toInt() - 1).toString()
            }
        }

        holder.username.text = p0.username // set username
        val timeText = holder.postTime.text.toString()
        holder.postTime.text = timeText + p0.time.toString()

        holder.moreVert.setOnClickListener {

            popupMeu(context, holder.moreVert, p0.videoUri, p0.description)
        }

        holder.description.text = p0.description
        val count = holder.description.text.length
        var descBool = false
        holder.showMoreLess.setOnClickListener {
            descBool = !descBool
            if (descBool) {
                holder.description.maxLines = count
            }else {
                holder.description.maxLines = 1
            }
            holder.showMoreLess.text = if (descBool) "Less" else "More"
        }
    }

    private fun popupMeu(context: Context, moreVert: View, uri: String, desc: String?){

        val popupMenu = PopupMenu(context,moreVert)
        popupMenu.menuInflater.inflate(R.menu.popup_menu_onvideo, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(mItem: MenuItem?): Boolean {
                when(mItem?.itemId){

                    R.id.downloadVideo -> {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(uri)//("https://www.google.com/search?q=$uri")
                        context.startActivity(intent)
                    }
                    R.id.shareVideo ->{
                        val sendIntent = Intent("android.intent.action.SEND")
                        sendIntent.putExtra(Intent.EXTRA_TEXT, desc)
                        sendIntent.putExtra(Intent.EXTRA_STREAM, "content://$uri")
                        sendIntent.type = "video/*"  // Change to "video/*" if sharing a video
                        // Specify the package to ensure it goes to WhatsApp
//                        sendIntent.setPackage("com.whatsapp")
                        context.startActivity(sendIntent)
                    }
                    R.id.reportVideo -> Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show()
                }
                return true
            }
        })
        popupMenu.show()
    }

}