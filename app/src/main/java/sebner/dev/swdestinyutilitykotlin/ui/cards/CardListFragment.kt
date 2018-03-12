package sebner.dev.swdestinyutilitykotlin.ui.cards

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_cards.*
import sebner.dev.swdestinyutilitykotlin.R
import sebner.dev.swdestinyutilitykotlin.model.Card
import sebner.dev.swdestinyutilitykotlin.ui.FilterFragment
import sebner.dev.swdestinyutilitykotlin.utils.*

class CardListFragment: FilterFragment() {

    private lateinit var cardAdapter: CardAdapter
    private var position = RecyclerView.NO_POSITION

    private lateinit var viewModel: CardListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_cards, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        context?.let {
            val factory = InjectorUtils().provideCardListViewModelFactory(it)
            viewModel = ViewModelProviders.of(this as Fragment, factory)
                    .get(CardListViewModel::class.java) }

        setHasOptionsMenu(true)

        recyclerview_cards.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerview_cards.setHasFixedSize(true)
        cardAdapter = CardAdapter(emptyList(), context) { onCardClicked(it) }
        recyclerview_cards.adapter = cardAdapter

        viewModel.getCards().observe(activity as LifecycleOwner, Observer {
            t: List<Card>? -> kotlin.run {
                t?.let { cardAdapter.updateCards(it) }

                if (position == RecyclerView.NO_POSITION) {
                        position = 0
                }
                recyclerview_cards.smoothScrollToPosition(position)
                triggerView(t != null && t.isNotEmpty())
            }
        })
    }

    private fun onCardClicked(src: String) {
        if (src.isEmpty()) return

        full_image.loadImage(src)
        full_image.visibility = View.VISIBLE
        full_image.setOnClickListener { it.visibility = View.GONE }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        menu?.clear()
        inflater.inflate(R.menu.toolbar_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                AppExecutors.getInstance().diskIO.execute { viewModel.searchForText(query) }
                return true
            }

            override fun onQueryTextSubmit(query: String) = false
        })
        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                AppExecutors.getInstance().diskIO.execute { viewModel.searchForText("") }
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem) = true
        })
    }

    private fun showSortingMenu(view: View?) {
        ifNotNull(context, view, { ctx, v -> run {
                val pop = PopupMenu(ctx, v)
                pop.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_sorting_name -> {
                            viewModel.sortByName()
                            true
                        }
                        R.id.action_sorting_color -> {
                            viewModel.sortByColor()
                            true
                        }
                        else -> {
                            viewModel.sortByDefault()
                            true
                        }
                    }
                }
                pop.inflate(R.menu.popup_sorting)
                pop.show()
            }
        })
    }

    private inline fun consumeMenu(f: () -> Unit): Boolean {
        f()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.action_sorting -> consumeMenu { showSortingMenu(activity?.findViewById(R.id.action_sorting))}
        else -> super.onOptionsItemSelected(item)
    }

    private fun triggerView(dataLoaded: Boolean) {
        when(dataLoaded) {
            true -> {
                loading_indicator_cards.setInvisible()
                recyclerview_cards.setVisible()
            }
            false -> {
                if (!viewModel.cardsInRepository()) {
                    loading_indicator_cards.setVisible()
                }
                recyclerview_cards.setInvisible()
            }
        }
    }

    override fun onFilterButtonPressed(v: View) {
        AppExecutors.getInstance().diskIO.execute({
            viewModel.filterButtonPressed((v as Button).text.toString())
        })
    }
}