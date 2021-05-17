package openfoodfacts.github.scrachx.openfood.models.entities.allergen

import openfoodfacts.github.scrachx.openfood.models.Product
import openfoodfacts.github.scrachx.openfood.network.ApiFields
import org.jetbrains.annotations.Contract
import java.util.*

object AllergenHelper {
    @Contract(" -> new")
    private fun createEmpty() = Data(false, emptyList())

    fun computeUserAllergen(product: Product, userAllergens: List<AllergenName>): Data {
        if (userAllergens.isEmpty()) return createEmpty()

        if (ApiFields.StateTags.INGREDIENTS_COMPLETED !in product.statesTags)
            return Data(true, emptyList())

        val productAllergens = HashSet(product.allergensHierarchy)
            .also { it += product.tracesTags }

        val allergenMatch = TreeSet<String?>()
        userAllergens.filter { productAllergens.contains(it.allergenTag) }
            .mapTo(allergenMatch) { it.name }

        return Data(false, allergenMatch.toList())
    }

    data class Data(val incomplete: Boolean, val allergens: List<String?>) {
        fun isEmpty() = !incomplete && allergens.isEmpty()
    }
}