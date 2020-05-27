package hu.bme.aut.bottomnavfragmentsdemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import hu.bme.aut.bottomnavfragmentsdemo.adapter.ImageDetailsAdapter
import hu.bme.aut.bottomnavfragmentsdemo.data.Favorite
import kotlinx.android.synthetic.main.activity_image_details.*
import kotlinx.android.synthetic.main.activity_image_details.view.*
import kotlin.random.Random

class ImageDetailsActivity : AppCompatActivity() {


//    lateinit var detailsAdapter: ImageDetailsAdapter

    private var favorited: Boolean = false
    private var imgUrl: String? = null
    private var medium: String? = null
    private var century: String? = null
    private var culture: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)


        val extras = intent.extras
        if (extras != null) {
            imgUrl = extras["URL_KEY"].toString()
            medium = extras["MEDIUM_KEY"].toString()
            century = extras["CENTURY_KEY"].toString()
            culture = extras["CULTURE_KEY"].toString()
        }

        heartIcon.setImageResource(R.drawable.ic_favorite_border_red_48dp)

        heartIcon.setOnClickListener {
            uploadFavorite()
        }

//        val info = arrayListOf<String?>(medium, century, culture)
//        detailsAdapter = ImageDetailsAdapter(this, info)
//        recyclerImageDetails.adapter = detailsAdapter

        insertValues()
    }

    private fun insertValues() {

        tvMedium.text = medium
        tvCentury.text = century
        tvCulture.text = culture

        Glide.with(this@ImageDetailsActivity)
            .load(imgUrl)
            .into(photoImageView)
    }

    private fun uploadFavorite() {

        var documentID = FirebaseAuth.getInstance().currentUser!!.uid + imgUrl!!.substring(imgUrl!!.length - 15, imgUrl!!.length - 1)
//            java.util.UUID.randomUUID().toString()

        val newItem = Favorite(
            FirebaseAuth.getInstance().currentUser!!.uid,
            FirebaseAuth.getInstance().currentUser!!.email!!,
            imgUrl.toString(),
            century.toString(),
            culture.toString(),
            medium.toString(),
            documentID
        )

        var favorites = FirebaseFirestore.getInstance().collection(
            "favorites")

        if (!favorited) {
            favorites.document(documentID)
                .set(newItem, SetOptions.merge())
                .addOnSuccessListener {
                }
                .addOnFailureListener{
                    Toast.makeText(this@ImageDetailsActivity,
                        "Error ${it.message}", Toast.LENGTH_LONG).show()
                }

            heartIcon.setImageResource(R.drawable.ic_favorite_red_48dp)
            favorited = true
        } else {
            favorites.document(documentID).delete()
            heartIcon.setImageResource(R.drawable.ic_favorite_border_red_48dp)
            favorited = false
        }

    }
}
