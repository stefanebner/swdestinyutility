package sebner.dev.swdestinyutilitykotlin.ui.cards

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import sebner.dev.swdestinyutilitykotlin.data.CardRepository

@Suppress("UNCHECKED_CAST")
class CardListViewModelFactory(private val cardRepository: CardRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardListViewModel(cardRepository) as T
    }
}