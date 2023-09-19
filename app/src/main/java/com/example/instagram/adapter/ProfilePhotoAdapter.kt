package com.example.instagram.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.R
import com.example.instagram.interfac.PersonalVideo
import com.example.instagram.model.UploadVideo

class ProfilePhotoAdapter(val context: Context, private val items : ArrayList<UploadVideo>, val onClick: PersonalVideo) :RecyclerView.Adapter<ProfilePhotoAdapter.MyViewHolder>(){

    class MyViewHolder(viewItem: View): RecyclerView.ViewHolder(viewItem){
        val imageView: ImageView = viewItem.findViewById(R.id.profilePhotos)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.photos_grid_view, parent, false)
        return MyViewHolder(layout)
    }

    @SuppressLint("MissingInflatedId")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val p0 = items[position]

        Glide.with(context).load(p0.videoUri).into(holder.imageView) // imageUri

        holder.imageView.setOnClickListener {

            val dialog = AlertDialog.Builder(context)

            val layout = LayoutInflater.from(context).inflate(R.layout.dialog_image_on_profile, null, false)
            val image = layout.findViewById<ImageView>(R.id.dialogImageView)
            val characterName: TextView = layout.findViewById(R.id.characterName)
            val desc: TextView = layout.findViewById(R.id.imageDialogDesc)
            val likeIcon: AppCompatImageButton = layout.findViewById(R.id.likePhoto)

            Glide.with(context).load(p0.videoUri).into(image)
            characterName.text = p0.username
            desc.text = p0.description

            var likeIconBool = false
            likeIcon.setOnClickListener {
                likeIconBool = !likeIconBool
                if (likeIconBool){
                    likeIcon.setImageResource(R.drawable.like_green_icon)
                }else{
                    likeIcon.setImageResource(R.drawable.like)
                }
            }
            dialog.setView(layout)
            val create = dialog.create()
            create.show()
        }

        holder.imageView.setOnLongClickListener {

            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Alert Dialog")
            dialog.setMessage("Choose options")

            dialog.setNegativeButton("Cancel"){_,_ ->}
            dialog.setNeutralButton("Edit"){_,_ ->
                onClick.postEditFun(
                    p0.videoImageName.toString(),
                    p0.videoUri.toString(),
                    p0.description.toString(),
                    p0.username.toString(),
                    p0.time.toString(),
                    p0.id.toString()
                )
            }
            dialog.setPositiveButton("Delete"){_,_ ->
                onClick.postDeleteFun(p0.videoImageName.toString(), p0.id.toString())
            }

            val create = dialog.create()
            create.show()
            true
        }
    }

    override fun getItemCount(): Int {

        return items.size
    }
}