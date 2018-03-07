package sebner.dev.swdestinyutilitykotlin.ui.damage

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_roll.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import sebner.dev.swdestinyutilitykotlin.R
import sebner.dev.swdestinyutilitykotlin.model.Card
import sebner.dev.swdestinyutilitykotlin.utils.loadImage
import sebner.dev.swdestinyutilitykotlin.utils.setInvisible
import sebner.dev.swdestinyutilitykotlin.utils.setVisible

class DamageAdapter(
        private val context: Context?,
        private var cardsDisplayed: List<Card>,
        private val viewModel: DamageViewModel
): RecyclerView.Adapter<DamageAdapter.CardViewHolder>() {

    private val spinnerAdapter: ArrayAdapter<Card> by lazy {
        ArrayAdapter<Card>(context, android.R.layout.simple_spinner_item, getInitialCardAsList()) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CardViewHolder(LayoutInflater.from(context).inflate(R.layout.item_roll, parent, false))

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) =
            holder.bind(cardsDisplayed[position]) { viewModel.addCard(it) }

    override fun getItemCount() = cardsDisplayed.size

    private fun getInitialCardAsList(): List<Card> {
        val c = Card()
        c.name = "Please choose a card to add"
        return arrayListOf(c)
    }

    inner class CardViewHolder(itemView: View): ViewHolder(itemView) {
        fun bind(card: Card, listener: (Card) -> Unit) = with(itemView) {
            if(card.code.isEmpty()) {
                spinner_card_selection.setVisible()
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_card_selection.adapter = spinnerAdapter
                spinner_card_selection.setSelection(0, false)
                spinner_card_selection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(view: AdapterView<*>?) {
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                        val c = parent?.getItemAtPosition(pos) as Card
                        if(c.code.isNotEmpty()) {
                            listener(c)
                        }
                    }
                }
            } else {
                spinner_card_selection.setInvisible()
                setValues(card)
            }
        }

        private fun setValues(card: Card) = with(itemView){
            card_name.text = card.name
            card_affiliation.text = card.affiliation_name
            card_die_sides.text = card.sides

            when(card.type_code) {
                "character" -> {
                    card_health.text = card.health.toString()
                    card_cost.text  = card.getPoints()
                    if(card.unique) {
                        card_checkbox_elite.setVisible()
                        card_checkbox_elite.isChecked = card.isElite
                        card_checkbox_elite.onClick { card.isElite = !card.isElite }
                    }
                }
                "fragment_battlefield" -> {
                    card_cost.text = ""
                    card_health.text = ""
                }
                else  -> {
                    card_cost.text = card.cost.toString()
                    card_health.text = ""
                }
            }

            when(card.faction_code) {
                "red" -> { card_color.setBackgroundColor(Color.RED) }
                "blue" -> { card_color.setBackgroundColor(Color.BLUE) }
                "yellow" -> { card_color.setBackgroundColor(Color.YELLOW) }
                else -> { card_color.setBackgroundColor(Color.GRAY) }
            }

            if(card.imagesrc.isEmpty()) {
                card_picture.setBackgroundColor(Color.WHITE)
            } else {
                card_picture.loadImage(card.imagesrc)
            }
        }
    }

    fun updateSpinnerCards(cards: List<Card>) {
        spinnerAdapter.clear()
        spinnerAdapter.addAll(getInitialCardAsList())
        spinnerAdapter.addAll(cards)
    }

    fun replaceData(cards: List<Card>) {
        this.cardsDisplayed = cards
        notifyDataSetChanged()
    }
}
