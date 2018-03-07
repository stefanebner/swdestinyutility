package sebner.dev.swdestinyutilitykotlin.ui.damage

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import sebner.dev.swdestinyutilitykotlin.data.CardRepository
import sebner.dev.swdestinyutilitykotlin.model.Card
import sebner.dev.swdestinyutilitykotlin.model.Die
import sebner.dev.swdestinyutilitykotlin.utils.DiceCalculation

class DamageViewModel(private val repository: CardRepository): ViewModel() {

    private val filter = HashSet<String>()
    private var comparator = Card().defaultComparator
    private var searchTerm = ""
    private val observer: Observer<List<Card>>
    private val repositoryCards: LiveData<List<Card>> = repository.getAllCardsWithDice()
    private val cards = MutableLiveData<List<Card>>()
    private val shownCards = MutableLiveData<List<Card>>()
    private var adapterList = mutableListOf(Card())

    init {
        observer = Observer { cards.postValue(it?.sortedWith(comparator)) }
        repositoryCards.observeForever(observer)
        shownCards.postValue(adapterList)
    }

    override fun onCleared() {
        super.onCleared()
        repositoryCards.removeObserver(observer)
    }

    fun getFullListOfCards(): LiveData<List<Card>> = cards

    fun getShownCards(): LiveData<List<Card>> = shownCards

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

    fun doCalculation(): String {
        val diceList: MutableList<Card> = adapterList.filter { it.code.isNotEmpty() }.toMutableList()
        adapterList.forEach { card -> if (card.isElite) diceList.add(card)}
        val calc = DiceCalculation(diceList.map { Die(it.sides) })
        return calc.getDamageRoll().toString()
    }

    fun removeCardShown(position: Int) {
        if (position > 0) {
            adapterList.removeAt(position)
            shownCards.postValue(adapterList)
        }
    }

    fun addCard(card: Card) {
        adapterList.add(card)
        shownCards.postValue(adapterList)
    }
}