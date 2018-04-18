package sebner.dev.swdestinyutilitykotlin.ui.cards

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_card.view.*
import sebner.dev.swdestinyutilitykotlin.R
import sebner.dev.swdestinyutilitykotlin.model.Card
import sebner.dev.swdestinyutilitykotlin.utils.loadImage

class CardAdapter(
        private var cards: List<Card>,
        private val context: Context?,
        private val listener: (String) -> Unit
): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CardViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card, parent, false))

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) = holder.bind(cards[position], listener)

    override fun getItemCount() = cards.size

    inner class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(card: Card, listener: (String) -> Unit) = with(itemView) {
            card_name.text = card.name
            card_affiliation.text = card.affiliation_name
            card_subtitle.text = card.subtitle
            card_rarity.text = card.rarity_name

            when(card.type_code) {
                "character" -> {
                    card_health.text = card.health.toString()
                    card_cost.text  = card.getPoints()
                }
                "fragment_battlefield" -> {
                    card_cost.text = ""
                    card_health.text = ""
                }
                else -> {
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

            setOnClickListener { listener(card.imagesrc) }
        }
    }

    fun updateCards(updatedCards: List<Card>) {
        cards = updatedCards
        notifyDataSetChanged()
    }
}