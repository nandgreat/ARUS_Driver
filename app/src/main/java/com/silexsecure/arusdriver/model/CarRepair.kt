package com.silexsecure.arusdriver.model

class CarRepair {


    var requestID: String? = null
    var vehicleType: String? = null
    var carMake: String? = null
    var vehicleColor: String? = null
    var vehicleModel: String? = null
    var regNumber: String? = null
    var carFault: String? = null
    var description: String? = null
    var requestKey: String? = null
    var mechanic: String? = null
    var customerName : String? = null
    var customerPhone : String? = null
    var customerImage : String? = null
    var requestTime: String? = null
    var paymentStatus: String? = null
    var pickupLongitude: String? = null
    var pickupLatitude: String? = null
    var pickupAddress: String? = null
    var paymentAmount: String? = null
    var paymentMethod: String? = null
    var requestStatus: String? = null
    var requestFee: String? = null
    var vehicleYear: String? = null
    var mechanicStatus: String? = null
    var mechanicPhone: String? = null
    var mechanicName: String? = null
    var mechanicImage: String? = null
    var orderTime: String? = null
    var serviceType: String? = null
    var mechanicLatitude: String? = null
    var mechanicLongitude: String? = null
    var mechanicID: String? = null


    constructor()
    constructor(requestID: String?, vehicleType: String?, carMake: String?, vehicleColor: String?, requestKey: String?, vehicleModel: String?, vehicleYear: String?, regNumber: String?, carFault: String?, description: String?, mechanic: String?, requestTime: String?, paymentStatus: String?, longitude: String?, lattitude: String?, address: String?, paymentAmount: String?, paymentMethod: String?, requestStatus: String?, requestFee: String?) {
        this.requestID = requestID
        this.vehicleType = vehicleType
        this.carMake = carMake
        this.vehicleColor = vehicleColor
        this.vehicleModel = vehicleModel
        this.requestKey = requestKey
        this.regNumber = regNumber
        this.carFault = carFault
        this.description = description
        this.mechanic = mechanic
        this.requestTime = requestTime
        this.paymentStatus = paymentStatus
        this.pickupLongitude = longitude
        this.pickupLatitude = lattitude
        this.pickupAddress = address
        this.paymentAmount = paymentAmount
        this.paymentMethod = paymentMethod
        this.requestStatus = requestStatus
        this.requestFee = requestFee
        this.vehicleYear = vehicleYear
    }
}