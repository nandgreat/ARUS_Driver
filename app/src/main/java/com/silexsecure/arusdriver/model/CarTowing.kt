package com.silexsecure.arusdriver.model

class CarTowing {


    var requestID: String? = null
    var vehicleType: String? = null
    var carMake: String? = null
    var requestTime: String? = null
    var paymentStatus: String? = null
    var longitude: String? = null
    var lattitude: String? = null
    var address: String? = null
    var paymentAmount: String? = null
    var paymentMethod: String? = null
    var requestStatus: String? = null
    var requestFee: String? = null



    constructor()
    constructor(requestID: String?, vehicleType: String?, carMake: String?,  mechanic: String?, requestTime: String?, paymentStatus: String?, longitude: String?, lattitude: String?, address: String?, paymentAmount: String?, paymentMethod: String?, requestStatus: String?, requestFee: String?) {
        this.requestID = requestID
        this.vehicleType = vehicleType
        this.carMake = carMake

        this.requestTime = requestTime
        this.paymentStatus = paymentStatus
        this.longitude = longitude
        this.lattitude = lattitude
        this.address = address
        this.paymentAmount = paymentAmount
        this.paymentMethod = paymentMethod
        this.requestStatus = requestStatus
        this.requestFee = requestFee
    }
}