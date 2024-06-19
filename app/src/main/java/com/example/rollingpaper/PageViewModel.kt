import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rollingpaper.Page
import com.example.rollingpaper.Repository
import kotlinx.coroutines.launch

class PageViewModel(private val repository: Repository) : ViewModel() {

    fun insertPage(page: Page) {
        viewModelScope.launch {
            repository.insertPage(page)
        }
    }


}

class PageViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
