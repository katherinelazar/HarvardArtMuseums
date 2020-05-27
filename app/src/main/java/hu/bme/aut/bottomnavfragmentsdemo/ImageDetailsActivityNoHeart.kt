package hu.bme.aut.bottomnavfragmentsdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import hu.bme.aut.bottomnavfragmentsdemo.data.Favorite
import kotlinx.android.synthetic.main.activity_image_details.*

class ImageDetailsActivityNoHeart : AppCompatActivity() {

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


        insertValues()
    }

    private fun insertValues() {

        tvMedium.text = medium
        tvCentury.text = century
        tvCulture.text = culture

        Glide.with(this@ImageDetailsActivityNoHeart)
            .load(imgUrl)
            .into(photoImageView)
    }
}


