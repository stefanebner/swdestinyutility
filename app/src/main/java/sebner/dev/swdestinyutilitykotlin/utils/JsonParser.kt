package sebner.dev.swdestinyutilitykotlin.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.GsonBuilder
import sebner.dev.swdestinyutilitykotlin.model.Card
import sebner.dev.swdestinyutilitykotlin.model.Set

/*
 * Json parsing utility to read sets and cards from the swdestinydb.com site
 */
class JsonParser {

    fun parseCardsFrom(json: String): Array<Card> {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Card::class.java, CardsDeserializer())
        val cardType = object : TypeToken<ArrayList<Card>>() {}.type
        val cards = builder.create().fromJson<ArrayList<Card>>(json, cardType)
        return cards.toTypedArray()
    }

    fun parseSetsFrom(json: String): Array<Set> {
        val listType = object : TypeToken<ArrayList<Set>>() {}.type
        val sets = Gson().fromJson<ArrayList<Set>>(json, listType)
        return sets.toTypedArray()
    }

    fun parseCardsTo(cards: Array<Card?>): String {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Card::class.java, CardsDeserializer())
        val cardType = object : TypeToken<ArrayList<Card>>() {}.type
        return builder.create().toJson(cards, cardType)
    }
}