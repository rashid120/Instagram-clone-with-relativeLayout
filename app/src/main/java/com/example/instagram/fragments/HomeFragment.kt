package com.example.instagram.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.ButtonSheetComments
import com.example.instagram.R
import com.example.instagram.adapter.UserPostAdapter
import com.example.instagram.interfac.Comment
import com.example.instagram.model.UploadVideo
import com.example.instagram.model.UserPostData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(), Comment {

    private lateinit var postItem: ArrayList<UserPostData>
    private lateinit var postAdapter: UserPostAdapter

    private lateinit var localStorageForComment: SharedPreferences
    private lateinit var itemDbArray: ArrayList<UploadVideo>

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val like = view.findViewById<ImageView>(R.id.likesIcon)

        like.setOnClickListener{

            startActivity(Intent(requireActivity(), ButtonSheetComments::class.java))
            Toast.makeText(requireActivity(), "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        val postRecyclerView: RecyclerView = view.findViewById(R.id.postRecyclerView)
        postRecyclerView.layoutManager = GridLayoutManager(activity,1)

        itemDbArray = arrayListOf()
        postItem = arrayListOf()
        getDataFun()

        postItem.add(UserPostData(
                R.drawable.erdugrul,
                "erdugul_ghazi",
                R.drawable.erdugrul,
                "Liked by halima_sultan and 2,054,704 others",
                "erdugrul_ghazi कबीले के लिए सरदार तलवार के ज़ोर पर नहीं,बल्कि अपनी जु़बान की बुनियाद पर मुन्तखिब;किया जाता है।-Sulayeman Shah....more","View all 1,53,987 comments", "1200 years ago"))
        postItem.add(
            UserPostData(
                R.drawable.bansi,
                "banshi_alp",
                R.drawable.bansi,
                "Liked by turgut_alp and 3,124,404 others",
                "banshi_alp ईमान की ताकत के आगे,फौलाद भी कमज़ोर पड़ जाता है।...more",
                "View all 2,00,438 comments",
                "1210 years ago"
            )
        )
        postItem.add(
            UserPostData(
                R.drawable.turgut,
                "turgut_alp",
                R.drawable.turgut,
                "Liked by banshi_alp and 3,824,316 others",
                "turgut_alp ❤️अल्लाह अजीज़ भी है और मुन्तकिल भी।💖...more",
                "View all 18,680 comments",
                "1209 years ago"
            )
        )
        postItem.add(
            UserPostData(
                R.drawable.malhun_po,
                "malhun_khatoon",
                R.drawable.malhun,
                "Liked by bala_khatoon and 3,237,309 others",
                "malhun_khatoon ❤️‍🧕हमें जब आप जैसे दोस्तों का साथ मुयस्सर है,तो दुश्मन हम पर कैसे खौफदारी कर सकता है। - malhun khatoon ...more",
                "View all 4,00,593 comments",
                "1150 years ago"
            )
        )
        postItem.add(
            UserPostData(
                R.drawable.osman,
                "osman_ghazi",
                R.drawable.osman,
                "Liked by bala_khatoon and 93,968,879 others",
                "osman_ghazi ⚔️ एक सरदार का फर्ज़ है किफितनों को उबरने से पहले ही दबा दे।  -Sulayeman Shah...more",
                "View all 7,97,865 comments",
                "985 years ago"
            )
        )
        postItem.add(
            UserPostData(
                R.drawable.bala,
                "bala_khatoon",
                R.drawable.bala,
                "Liked by malhun_khatoon and 3,458,239 others",
                "bala_khatoon इंसाफ करने के लिए तकलीफें भी उठानी पड़ती हैं। -Hayma Hatun....more",
                "View all 4,53,353 comments",
                "960 years ago"
            )
        )
        postItem.add(
            UserPostData(
                R.drawable.orhan,
                "orhan_ghazi",
                R.drawable.orhan,
                "Liked by osman_ghazi and 5,34,654,404 others",
                "orhan_ghazi जो माफ नहीं करता,उसे भी माफ नहीं किया जाता।-Hayma Hatun....more",
                "View all 5,30,938 comments",
                "940 years ago"
            )
        )
        postItem.add(
            UserPostData(
                R.drawable.konur,
                "konur_alp",
                R.drawable.konur,
                "Liked by gokduk_alp and 6,753,632 others",
                "konur_alp दुश्मनों की ताकत का अन्दाज़ालगाना एहमकों का काम है।-Titush...more",
                "View all 90,876 comments",
                "980 years ago"
            )
        )
        postItem.add(
            UserPostData(
                R.drawable.gokduk,
                "gokduk_alp",
                R.drawable.gokduk,
                "Liked by osman_ghazi and 2,054,704 others",
                "gokduk_alp ❤आबे हयात सिर्फ उसे मिलेगा,जिसे ईमान की तलब होगी। - Ibn-ul-Arabi...more",
                "View all 2,50,740 comments",
                "990 years ago"
            )
        )
        postItem.add(
            UserPostData(
                R.drawable.konurr,
                "konur_alp2",
                R.drawable.konurr,
                "Liked by orhan_ghazi and 3,139,304 others",
                "konur_alp2 ❤ बड़े ख्वाबों के लिए बड़ी कुर्बानियाँ देनी पड़ती हैं। - Ibn-ul-Arabi...more",
                "View all 2,00,210 comments",
                "950 years ago"
            )
        )
        postItem.add(
            UserPostData(
                R.drawable.fatih,
                "fatih_sultan_mehmed",
                R.drawable.fatih,
                "Liked by rashid_ekbal and 29,43,484,834 others",
                "fatih_sultan_mehmed इम्तिहान के दौरान तरह-तरह की मुश्किलें भी झेलनी पड़ती हैं, तभी सबसे बड़े बहादुर कहलाओगे। - Ibn-ul-Arabi....more",
                "View all 73,69,987 comments",
                "585 years ago"
            )
        )
        postItem.add(
            UserPostData(
                R.drawable.rashid,
                "fatih_sultan_mehmed",
                R.drawable.rashid2,
                "Liked by narend_modi and 2,054,704 others",
                "rashid_ekbal ❤️❤️❤️💖💖..........more",
                "View all 10,680 comments",
                "4 years ago"
            )
        )

        postAdapter = UserPostAdapter(requireActivity(),postItem, this, itemDbArray)
        postRecyclerView.adapter = postAdapter

        localStorageForComment = requireActivity().getSharedPreferences("comments", Context.MODE_PRIVATE)

        // Inflate the layout for this fragment
        return view
    }

    override fun commentText(text: String) {

        val addComment = localStorageForComment.edit()
        addComment.putString("firstComment", text).apply()
    }

    override fun share(position: Int) {
        startActivity(Intent(Intent.ACTION_VIEW))
    }

    //get data from real time database
    private fun getDataFun(){

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("all_images")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Toast.makeText(requireActivity(), "find successfully", Toast.LENGTH_SHORT).show()
                for (dataSnapshot in snapshot.children){

                    val model = dataSnapshot.getValue(UploadVideo::class.java)
                    if (model != null){
                        itemDbArray.add(model)
                        postAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}