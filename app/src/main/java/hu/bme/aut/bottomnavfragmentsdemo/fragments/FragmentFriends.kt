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
import hu.bme.aut.bottomnavfragmentsdemo.adapter.FriendsAdapter
import hu.bme.aut.bottomnavfragmentsdemo.data.ArtPiece


class FragmentFriends: Fragment() {

    companion object {
        const val TAG="FragmentThree"
    }

    lateinit var friendsAdapter: FriendsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView=inflater.inflate(R.layout.fragment_friends,container,false)

        val activity = activity as Context
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerFriends)
        val layoutManager= StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        recyclerView.layoutManager = layoutManager

        friendsAdapter = FriendsAdapter(activity,
            FirebaseAuth.getInstance().currentUser!!.uid)
        recyclerView.adapter = friendsAdapter

        initRecyclerview()

        return rootView
    }

    private fun initRecyclerview() {
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("favorites")
            .whereLessThan("uid", FirebaseAuth.getInstance().currentUser!!.uid )
//            .whereGreaterThan("uid", FirebaseAuth.getInstance().currentUser!!.uid )
//            .whereLessThan("user", FirebaseAuth.getInstance().currentUser!!.email!!)
//            .whereGreaterThan("user", FirebaseAuth.getInstance().currentUser!!.email!!)

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
                            friendsAdapter.addPost(artPiece, docChange.document.id)
                        } else if (docChange.type == DocumentChange.Type.REMOVED) {
                            friendsAdapter.removePostByKey(docChange.document.id)

                        } else if (docChange.type == DocumentChange.Type.MODIFIED) {

                        }
                    }
                }
            }
        )
    }
}