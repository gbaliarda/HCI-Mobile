package ar.edu.itba.hci.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class Ordering {
    NONE, DATE, SCORE, DIFFICULTY
}

class SharedViewModel : ViewModel() {

    private var _ordering = MutableLiveData(Ordering.NONE)
    val ordering: LiveData<Ordering> = _ordering
    private var _onlyFavorite = MutableLiveData(false)
    val onlyFavorite: LiveData<Boolean> = _onlyFavorite

    fun saveOrder(newOrder: Ordering) {
        _ordering.value = newOrder
    }

    fun toggleFavorite() {
        _onlyFavorite.value = !_onlyFavorite.value!!
    }

}