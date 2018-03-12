package sebner.dev.swdestinyutilitykotlin.ui.reroll

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sebner.dev.swdestinyutilitykotlin.R
import sebner.dev.swdestinyutilitykotlin.model.Card
import sebner.dev.swdestinyutilitykotlin.ui.damage.DamageViewModel

class RerollAdapter(
    private val context: Context?,
    private var cardsDisplayed: List<Card>,
    private val viewModel: DamageViewModel
): RecyclerView.Adapter<RerollAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CardViewHolder(LayoutInflater.from(context).inflate(R.layout.item_roll, parent, false))

    override fun onBindViewHolder(holder: RerollAdapter.CardViewHolder, position: Int) =
            holder.bind(cardsDisplayed[position]) { viewModel.addCard(it) }

    override fun getItemCount() = cardsDisplayed.size

    inner class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(card: Card, listener: (Card) -> Unit) = with(itemView) {
            if (card.code.isEmpty()) {

            } else {

            }
        }
    }
}
