package sebner.dev.swdestinyutilitykotlin.ui.damage

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_damage.*
import org.koin.android.architecture.ext.viewModel
import sebner.dev.swdestinyutilitykotlin.R
import sebner.dev.swdestinyutilitykotlin.model.Card

class DamageFragment : Fragment() {

    private lateinit var damageAdapter: DamageAdapter
    private val viewModel by viewModel<DamageViewModel>()
    private var currentlyDisplayed: MutableList<Card> = arrayListOf(Card())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_damage, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerview_damage.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerview_damage.setHasFixedSize(true)

        damageAdapter = DamageAdapter(context, currentlyDisplayed, viewModel)
        recyclerview_damage.adapter = damageAdapter

        damage_btn_doroll.setOnClickListener { damage_result.text = viewModel.doCalculation() }
        initObservers()
        initSwipeToDelete()
    }

    private fun initObservers() {
        viewModel.getFullListOfCards().observe(activity as LifecycleOwner, Observer {
            t: List<Card>? -> t?.let { damageAdapter.updateSpinnerCards(it) }
        })

        viewModel.getShownCards().observe(activity as LifecycleOwner, Observer {
            t: List<Card>? -> t?.let { damageAdapter.replaceData(it) }
        })
    }

    private fun initSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView?,
                                          viewHolder: RecyclerView.ViewHolder?): Int {
                return if (viewHolder?.adapterPosition == 0) {
                    0
                } else {
                    makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
                }
            }

            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?,
                                target: RecyclerView.ViewHolder?): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                viewHolder?.let { viewModel.removeCardShown(it.adapterPosition) }
            }

        }).attachToRecyclerView(recyclerview_damage)
    }
}