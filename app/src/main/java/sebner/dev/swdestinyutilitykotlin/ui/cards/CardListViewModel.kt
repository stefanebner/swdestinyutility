package sebner.dev.swdestinyutilitykotlin.ui.cards

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import sebner.dev.swdestinyutilitykotlin.data.CardRepository
import sebner.dev.swdestinyutilitykotlin.model.Card

class CardListViewModel(private val repository: CardRepository): ViewModel() {

    private val filter = HashSet<String>()
    private var comparator = Card().defaultComparator
    private var searchTerm = ""
    private val observer: Observer<List<Card>>
    private val repositoryCards: LiveData<List<Card>> = repository.getAll()
    private val cards = MutableLiveData<List<Card>>()

    init {
        observer = Observer { cards.postValue(it?.sortedWith(comparator)) }
        repositoryCards.observeForever(observer)
    }

    override fun onCleared() {
        super.onCleared()
        repositoryCards.removeObserver(observer)
    }

    fun getCards(): LiveData<List<Card>> = cards

    fun filterButtonPressed(button: String) {
        val text = button.toLowerCase()
        if (filter.contains(text)) filter.remove(text) else filter.add(text)
        updateDatabase()
    }

    fun cardsInRepository() = repositoryCards.value?.isNotEmpty() ?: false

    fun searchForText(term: String) {
        searchTerm = term
        updateDatabase()
    }

    private fun updateDatabase() {
        cards.postValue(repository.getCardsWith(searchTerm, filter).sortedWith(comparator))
    }

    fun sortByName() {
        comparator = Card().nameComparator
        cards.value = cards.value?.sortedWith(comparator)
    }

    fun sortByColor() {
        comparator = Card().colorComparator
        cards.value = cards.value?.sortedWith(comparator)
    }

    fun sortByDefault() {
        comparator = Card().defaultComparator
        cards.value = cards.value?.sortedWith(comparator)
    }
}