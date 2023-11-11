package com.example.pricingapp

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "ERROR", strict = false)
data class Error(
    @field:Element(name = "ERRORCODE", required = false)
    var errorCode: Int? = null,

    @field:Element(name = "ERRORMESSAGE", required = false)
    var errorMessage: String? = null
)

@Root(name = "ABF", strict = false)
data class QuoteResponse(
    @field:Element(name = "QUOTEID", required = false)
    var quoteID: String? = null,

    @field:Element(name = "CHARGE", required = false)
    var charge: Float = 0f,

    @field:Element(name = "ADVERTISEDTRANSIT", required = false)
    var transitTime: String? = null,

    @field:Element(name = "EXPIRATIONDATE", required = false)
    var expirationDate: String? = null,

    @field:Element(name = "NUMERRORS", required = false)
    var numErrors: Int? = null,

    @field:Element(name = "ERROR", required = false)
    var error: Error? = null
)
