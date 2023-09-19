package com.example.instagram.adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.R
import com.example.instagram.interfac.Comment
import com.example.instagram.model.UploadVideo
import com.example.instagram.model.UserPostData
import com.google.android.material.bottomsheet.BottomSheetDialog

class UserPostAdapter (private val cxt: Context, private val items : ArrayList<UserPostData>, private val comment: Comment, val fireItem: ArrayList<UploadVideo>) : RecyclerView.Adapter<UserPostAdapter.MyViewHolder>(){

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val shortProfileIcon: ImageView = itemView.findViewById(R.id.shortProfileIcon)
        val username: TextView = itemView.findViewById(R.id.userName)
        val userPostImage: ImageView = itemView.findViewById(R.id.userPostImage)
        val showLikes: TextView = itemView.findViewById(R.id.showLikes)
        val showDesc: TextView = itemView.findViewById(R.id.showDesc)
        val showComments: TextView = itemView.findViewById(R.id.showComments)
        val showPostTime: TextView = itemView.findViewById(R.id.showPostTime)
        val commentBtn: ImageView = itemView.findViewById(R.id.commentBtn)
        val shareBtn : ImageView = itemView.findViewById(R.id.sharePostBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val layout = LayoutInflater.from(parent.context).inflate(R.layout.user_post_list_view, parent, false)
        return MyViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        items[position].shorProfileIcon?.let { holder.shortProfileIcon.setImageResource(it) }
        holder.username.text = items[position].username
        items[position].userPostImage?.let { holder.userPostImage.setImageResource(it) }
        holder.showLikes.text = items[position].showLikes
        holder.showDesc.text = items[position].showDescription
        holder.showComments.text = items[position].showFixedComment
        holder.showPostTime.text = items[position].showPostTime

    // firebase image set start
//        Glide.with(cxt).load(fireItem)
    // firebase image set end
        //popup Menu
        val moreVert = holder.itemView.findViewById<ImageView>(R.id.moreVert)
        moreVert.setOnClickListener {

            val popupMenu = PopupMenu(holder.itemView.context, moreVert)
            popupMenu.menuInflater.inflate(R.menu.option_menu, popupMenu.menu)
            popupMenu.show()

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {

                    R.id.block -> Toast.makeText(holder.itemView.context, "${items[position].username} Blocked", Toast.LENGTH_SHORT).show()

                    R.id.follow -> Toast.makeText(holder.itemView.context, "${items[position].username} Followed", Toast.LENGTH_SHORT).show()

                    R.id.report -> Toast.makeText(holder.itemView.context, "${items[position].username} Reported", Toast.LENGTH_SHORT).show()
                }
                true
            }
        }

        //comments
        holder.commentBtn.setOnClickListener {
            val bottomSheetLayout = LayoutInflater.from(holder.itemView.context).inflate(R.layout.activity_button_sheet_comments,null)
            val bottomSheetDialog = BottomSheetDialog(holder.itemView.context)

            bottomSheetDialog.setContentView(bottomSheetLayout)
            bottomSheetDialog.setTitle("Comments")

            val message = bottomSheetLayout.findViewById<EditText>(R.id.editTextComment)
            val send = bottomSheetLayout.findViewById<ImageButton>(R.id.sendImgBtn)

            send.setOnClickListener {

                if (message.text.isEmpty()){
                    Toast.makeText(holder.itemView.context, "Please add a comment", Toast.LENGTH_SHORT).show()
                }else{
                    comment.commentText(message.text.toString())
                }
            }
            bottomSheetDialog.show()
        }

        // post share
        holder.shareBtn.setOnClickListener {
            items[position].userPostImage?.let { it1 -> comment.share(it1) }
        }

    }
}