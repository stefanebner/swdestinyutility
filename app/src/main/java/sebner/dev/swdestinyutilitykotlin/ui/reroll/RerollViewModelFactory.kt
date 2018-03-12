package sebner.dev.swdestinyutilitykotlin.ui.reroll

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import sebner.dev.swdestinyutilitykotlin.data.CardRepository

class RerollViewModelFactory(private val cardRepository: CardRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RerollViewModel(cardRepository) as T
    }
}
