package hu.bme.aut.bottomnavfragmentsdemo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import hu.bme.aut.bottomnavfragmentsdemo.ImageDetailsActivity
import hu.bme.aut.bottomnavfragmentsdemo.ImageDetailsActivityNoHeart
import hu.bme.aut.bottomnavfragmentsdemo.R
import hu.bme.aut.bottomnavfragmentsdemo.data.ArtPiece
import kotlinx.android.synthetic.main.gallery_image.view.*


class FriendsAdapter : RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    var context: Context
    var galleryList = mutableListOf<ArtPiece>()
    var galleryKeys = mutableListOf<String>()

    var currentUid: String

    constructor(context: Context, uid: String) : super() {
        this.context = context
        this.currentUid = uid
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.gallery_image, parent, false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return galleryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var image = galleryList.get(holder.adapterPosition)

        if (image.imgUrl!!.isNotEmpty()) {
            holder.galleryImage.visibility = View.VISIBLE
            Glide.with(context).load(image.imgUrl).into(holder.galleryImage)
        } else {
            holder.galleryImage.visibility = View.GONE
        }

        holder.galleryImage.setOnClickListener {

            val intent = Intent(context, ImageDetailsActivityNoHeart::class.java).apply {
                putExtra("URL_KEY", image.imgUrl)
                putExtra("MEDIUM_KEY", image.medium)
                putExtra("CULTURE_KEY", image.culture)
                putExtra("CENTURY_KEY", image.century)
            }
            context.startActivity(intent)
        }
    }

    fun addPost(post: ArtPiece, key: String) {
        galleryList.add(post)
        galleryKeys.add(key)
        notifyDataSetChanged()
    }

    private fun removePost(index: Int) {
        FirebaseFirestore.getInstance().collection("art_pieces").document(
            galleryKeys[index]
        ).delete()

        galleryList.removeAt(index)
        galleryKeys.removeAt(index)
        notifyItemRemoved(index)
    }


    fun removePostByKey(key: String) {
        var index = galleryKeys.indexOf(key)
        if (index != -1) {
            galleryList.removeAt(index)
            galleryKeys.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var galleryImage = itemView.galleryImage
    }
}