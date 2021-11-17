package ar.edu.itba.hci.android.ui.home

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.itba.hci.android.MainApplication
import ar.edu.itba.hci.android.api.model.PagedList
import ar.edu.itba.hci.android.api.model.Routine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val app: MainApplication) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val userRepository = app.userRepository

    val routines:LiveData<PagedList<Routine>> by lazy {
        MutableLiveData<PagedList<Routine>>().also {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    it.postValue(userRepository.getCurrentUserRoutines())
                }
                catch (ex:Exception) {
                    println("Error al cargar rutinas ${ex.stackTrace}")
                }
            }
        }
    }

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
