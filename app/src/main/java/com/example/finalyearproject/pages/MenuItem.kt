data class MenuItem(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val image_path: String = "",
    val basePrice: Double = 0.00,
    val sizes: Map<String, Double> = emptyMap(),
    val category: String = "",
    val pizzaBase: Map<String, Double>? = null, // Optional for non-pizza items
    val toppings: Map<String, Double>? = null, // Optional for non-pizza items
    val complimentaries: List<String>? = null // Optional for items with complementary products

)

// Data class for Order
data class CartItem(
    val productName: String,
    val size: String? = null, // Optional
    val milk: String? = null, // Optional
    val sugar: String? = null, // Optional
    val espressoShots: Int = 0, // Optional
    val base: String? = null, // Optional
    val toppings: List<String> = emptyList(), // Optional
    val flavor: String? = null, // Optional
    val complimentaries: List<String> = emptyList(), // Optional
    val quantity: Int = 0,
    val totalPrice: Double,
    val image_path: String = "",

)

data class Voucher(
    val title: String,
    val description: String,
    val discount: String,
    val code: String,
    val imageRes: Int
)


data class Order(
    val orderId: String = "",
    val userId: String = "",
    val cartItems: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val tax: Double = 0.0,
    val totalPrice: Double = 0.0,
    val timestamp: Long = 0L
)



