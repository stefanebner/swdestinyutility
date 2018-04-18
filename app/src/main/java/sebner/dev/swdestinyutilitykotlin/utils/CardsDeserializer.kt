package sebner.dev.swdestinyutilitykotlin.utils

import android.text.TextUtils
import com.google.gson.*
import sebner.dev.swdestinyutilitykotlin.model.Card
import java.lang.reflect.Type


class CardsDeserializer : JsonDeserializer<Card> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Card {
        val jsonObject = json.asJsonObject

        val card = Card()

        val cardHasDie = jsonObject.get("has_die").asBoolean
        val cardIsUnique = jsonObject.get("is_unique").asBoolean
        val cardType = jsonObject.get("type_code").asString

        val sb = StringBuilder()
        if (cardHasDie) {
            val jsonSidesArray = jsonObject.get("sides").asJsonArray
            for (i in 0 until jsonSidesArray.size()) {
                sb.append(jsonSidesArray[i].toString().replace("\"", ""))
                if(i < jsonSidesArray.size()-1) {
                    sb.append(' ')
                }
            }
        } else
            sb.append("")

        val die_sides = sb.toString()
        var points = arrayOfNulls<String>(2)
        points[0] = "99"
        points[1] = "99"

        if (cardType == "character") {
            if (cardIsUnique) {
                points = TextUtils.split(if (jsonObject.get("points").isJsonNull)
                    "-1/-1"
                else
                    jsonObject.get("points")
                            .asString, "/")
            } else {
                points[0] = if (jsonObject.get("points").isJsonNull) "-1" else jsonObject.get("points").asString
            }
        }

        card.die_sides = die_sides
        card.set_code = jsonObject.get("set_code").asString
        card.set_name + jsonObject.get("set_name").asString
        card.type_code = cardType
        card.type_name  = jsonObject.get("type_name").asString
        card.faction_code = jsonObject.get("faction_code").asString
        card.faction_name = jsonObject.get("faction_name").asString
        card.affiliation_code = jsonObject.get("affiliation_code").asString
        card.affiliation_name = jsonObject.get("affiliation_name").asString
        card.rarity_code = jsonObject.get("rarity_code").asString
        card.rarity_name = jsonObject.get("rarity_name").asString
        card.position = jsonObject.get("position").asInt
        card.code = jsonObject.get("code").asString
        card.ttscardid = if (jsonObject.get("ttscardid").isJsonNull) "" else jsonObject.get("ttscardid").asString
        card.name = jsonObject.get("name").asString
        card.subtitle = if (jsonObject.get("subtitle").isJsonNull) "" else jsonObject.get("subtitle").asString
        card.cost = if (jsonObject.get("cost").isJsonNull) -1 else jsonObject.get("cost").asInt
        card.health = if (jsonObject.get("health").isJsonNull) -1 else jsonObject.get("health").asInt
        card.points_single = Integer.valueOf(points[0])
        card.points_elite = Integer.valueOf(points[1])
        card.text = if (jsonObject.get("text").isJsonNull) "" else jsonObject.get("text").asString
        card.deck_limit = jsonObject.get("deck_limit").asInt
        card.illustrator =if (jsonObject.get("illustrator").isJsonNull) "" else jsonObject.get("illustrator").asString
        card.unique = cardIsUnique
        card.has_die = cardHasDie
        card.has_errata = jsonObject.get("has_errata").asBoolean
        card.url = jsonObject.get("url").asString
        card.imagesrc = if (jsonObject.get("imagesrc").isJsonNull) "" else jsonObject.get("imagesrc").asString
        card.label = jsonObject.get("label").asString
        card.cp = jsonObject.get("cp").asInt
        card.flavor = if (jsonObject.get("flavor").isJsonNull) "" else jsonObject.get("flavor").asString
        card.subtype_code = jsonObject.getRoomSafeString("subtype_code")
        card.subtype_name = jsonObject.getRoomSafeString("subtype_name")


        return card
    }
}

fun JsonObject.getRoomSafeString(name: String): String {
    return if (!has(name) || get(name).isJsonNull) "" else get(name).asString
}