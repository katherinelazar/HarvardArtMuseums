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
import hu.bme.aut.bottomnavfragmentsdemo.adapter.FavoritesAdapter
import hu.bme.aut.bottomnavfragmentsdemo.adapter.GalleryAdapter
import hu.bme.aut.bottomnavfragmentsdemo.data.ArtPiece
import hu.bme.aut.bottomnavfragmentsdemo.data.Base
import hu.bme.aut.bottomnavfragmentsdemo.network.ArtMuseumsAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentFavorites: Fragment() {

    companion object {
        const val TAG="FragmentOne"
    }

    lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView=inflater.inflate(R.layout.fragment_favorites,container,false)

        val activity = activity as Context
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerFavorites)
        val layoutManager= StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        recyclerView.layoutManager = layoutManager

        favoritesAdapter = FavoritesAdapter(activity,
            FirebaseAuth.getInstance().currentUser!!.uid)
        recyclerView.adapter = favoritesAdapter

        initRecyclerview()

        return rootView
    }

    private fun initRecyclerview() {
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("favorites")
            .whereEqualTo("uid", FirebaseAuth.getInstance().currentUser!!.uid)
            .whereEqualTo("user", FirebaseAuth.getInstance().currentUser!!.email!!)

        snapshotListener(query)
    }

    private fun snapshotListener(query: Query) {
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
                            favoritesAdapter.addPost(artPiece, docChange.document.id)
                        } else if (docChange.type == DocumentChange.Type.REMOVED) {

                            favoritesAdapter.removePostByKey(docChange.document.id)

                        } else if (docChange.type == DocumentChange.Type.MODIFIED) {

                        }
                    }
                }
            }
        )
    }
}