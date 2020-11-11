package com.silexsecure.arusdriver.model

class ProductRequest(

        var id: String? = null,
        var customerName: String? = null,
        var customerPhone: String? = null,
        var customerImage: String? = null,
        var driver: String? = null,
        var product: String? = null,
        var pricePerLitre: Double? = null,
        var pricePerKg: Double? = null,
        var quantityInKg: Double? = null,
        var gasCylinder: String? = null,
        var quantityInLitres: String? = null,
        var amount: Double? = null,
        var requestKey : String?  = null,
        var paymentType: String? = null,
        var address: String? = null,
        var latitude: String? = null,
        var longitude: String? = null,
        var paymentStatus: String? = null,
        var deliveryStatus: String? = null,
        var orderTime: String? = null,
        var gasInt: Int? = null,
        var quantityInt : Int ? = null
) {
}
