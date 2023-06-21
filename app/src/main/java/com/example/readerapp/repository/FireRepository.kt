package com.example.readerapp.repository

import com.example.readerapp.data.DataOrException
import com.example.readerapp.model.MBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val queryBook: Query
) {
    private var listenerRegistration: ListenerRegistration? = null

    fun getAllBooksFromDatabase(): DataOrException<List<MBook>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()

        val collectionRef = queryBook.whereEqualTo("user_id",
            FirebaseAuth.getInstance().currentUser?.uid
        )

        listenerRegistration = collectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle error and update the LiveData with the exception
                dataOrException.e = exception
                return@addSnapshotListener
            }

            if (snapshot != null) {
                // Data has changed, update UI
                val data = snapshot.documents.map { documentSnapshot ->
                    documentSnapshot.toObject(MBook::class.java)!!
                }

                dataOrException.data = data
            }

        }

        return dataOrException
    }

    fun removeSnapshotListener() {
        listenerRegistration?.remove()
    }

}