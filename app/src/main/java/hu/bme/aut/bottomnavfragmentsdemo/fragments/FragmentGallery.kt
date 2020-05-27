package hu.bme.aut.bottomnavfragmentsdemo.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import hu.bme.aut.bottomnavfragmentsdemo.R
import hu.bme.aut.bottomnavfragmentsdemo.adapter.GalleryAdapter
import hu.bme.aut.bottomnavfragmentsdemo.data.ArtPiece
import hu.bme.aut.bottomnavfragmentsdemo.data.Base
import hu.bme.aut.bottomnavfragmentsdemo.network.ArtMuseumsAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentGallery: Fragment() {

    lateinit var galleryAdapter: GalleryAdapter
    private var dataReceived: Boolean = false

    companion object {
        const val TAG = "FragmentGallery"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView= inflater.inflate(R.layout.fragement_gallery,container,false)

        val activity = activity as Context
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerGallery)
        val layoutManage = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        layoutManage.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        recyclerView.layoutManager = layoutManage

        galleryAdapter = GalleryAdapter(activity,
            FirebaseAuth.getInstance().currentUser!!.uid)
        recyclerView.adapter = galleryAdapter

        initRecyclerview()

        return rootView
    }

    override fun onResume() {
        super.onResume()

        if (!dataReceived) {
            getData()
            dataReceived = true
        }
    }

    private fun getData() {
        var retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var museumsAPI = retrofit.create(ArtMuseumsAPI::class.java)

        val call = museumsAPI.getArtDetails(
            getString(R.string.harvard_art_museums_api_key)
        )

        call.enqueue(object : Callback<Base> {
            override fun onResponse(call: Call<Base>, response: Response<Base>) {
                processJSON(response)
            }
            override fun onFailure(call: Call<Base>, t: Throwable) {
                Toast.makeText(activity, "Error: ${t.message}",
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    fun processJSON(response: Response<Base>) {

        var museumsResult = response.body()

        if (response.code() != 404) {

            val imageObjects = ArrayList<ArtPiece>()
            museumsResult?.records?.forEach {
                val tempArtPiece = ArtPiece()
                if (it != null) {
                    tempArtPiece.century = "Century: " + it.century
                    tempArtPiece.culture = "Culture: " + it.culture
                    tempArtPiece.medium = "Medium: " + it.medium
                    it.images?.forEach {
                        if (it.baseimageurl != null && it.baseimageurl.length > 10) {
                            tempArtPiece.imgUrl = it.baseimageurl
                        }
                    }
                }
                imageObjects.add(tempArtPiece)
            }

             uploadPosts(imageObjects)
        }
    }


    fun uploadPosts(imageObjects: ArrayList<ArtPiece>) {

        var imagesCollection = FirebaseFirestore.getInstance().collection(
            "art_pieces")

        for (artPiece in imageObjects) {

            if (artPiece.imgUrl != null && !artPiece.imgUrl.equals("")) {
                var documentID = artPiece.imgUrl!!.substring(artPiece.imgUrl!!.length - 10, artPiece.imgUrl!!.length - 1).toString() + artPiece.century.toString() + artPiece.medium.toString() + artPiece.culture.toString()

                imagesCollection.document(documentID)
                    .set(artPiece)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener{
                        Toast.makeText(activity, "Error ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    private fun initRecyclerview() {
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("art_pieces")

        snapshotListener(query)
    }

    private fun snapshotListener(query: CollectionReference) {
        query.addSnapshotListener(
            object : EventListener<QuerySnapshot> {
                override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                    if (e!=null) {
                        Toast.makeText(activity, "Error: ${e.message}",
                            Toast.LENGTH_LONG).show()
                        return
                    }

                    for (docChange in querySnapshot?.getDocumentChanges()!!) {
                        if (docChange.type == DocumentChange.Type.ADDED) {
                            val artPiece = docChange.document.toObject(ArtPiece::class.java)

                            //
                            galleryAdapter.addPost(artPiece, docChange.document.id)
                        } else if (docChange.type == DocumentChange.Type.REMOVED) {

                            galleryAdapter.removePostByKey(docChange.document.id)

                        } else if (docChange.type == DocumentChange.Type.MODIFIED) {

                        }
                    }
                }
            }
        )
    }
}