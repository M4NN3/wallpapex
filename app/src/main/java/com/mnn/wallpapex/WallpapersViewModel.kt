package com.mnn.wallpapex

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot

class WallpapersViewModel : ViewModel(){
    private val firebaseRepository: FirebaseRepository= FirebaseRepository()

    private val wallpapersList: MutableLiveData<List<WallpapersModel>> by lazy {
        MutableLiveData<List<WallpapersModel>>().also {
            loadWallpapersData()
        }
    }

    fun getWallpapersList(): LiveData<List<WallpapersModel>>{
        return wallpapersList
    }

    fun loadWallpapersData(){
        //Query data from repo
        firebaseRepository.queryWallpapers().addOnCompleteListener{
            if(it.isSuccessful){
                val result = it.result
                if (result!!.isEmpty){
                    //no more result to load. Reached at bottom of page
                } else {
                    //results are ready to load
                    if (wallpapersList.value==null){
                        //loading first page
                        wallpapersList.value = result.toObjects(WallpapersModel::class.java)
                    }
                    else{
                        //loading next page
                        wallpapersList.value = wallpapersList.value!!.plus(result.toObjects(WallpapersModel::class.java))
                    }
                    //get last document
                    val lastItem: DocumentSnapshot = result.documents[result.size()-1]
                    firebaseRepository.lastVisible = lastItem
                }
            }else{
                //error
                Log.d("VIEW_MODEL_LOG", "Error : ${it.exception!!.message}")
            }
        }
    }

}