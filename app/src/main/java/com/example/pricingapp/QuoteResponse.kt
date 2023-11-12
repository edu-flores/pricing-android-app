package com.example.pricingapp

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ERROR", strict = false)
data class Error(
    @field:Element(name = "ERRORCODE", required = false)
    var errorCode: Int? = null,

    @field:Element(name = "ERRORMESSAGE", required = false)
    var errorMessage: String? = null
)

@Root(name = "ORIGTERMINFO", strict = false)
data class OriginTerminalInfo(
    @field:Element(name = "ORIGTERMADDRESS", required = false)
    var originTerminalAddress: String? = null,

    @field:Element(name = "ORIGTERMCITY", required = false)
    var origingTerminalCity: String? = null,

    @field:Element(name = "ORIGTERMSTATE", required = false)
    var origingTerminalState: String? = null,

    @field:Element(name = "ORIGTERMZIP", required = false)
    var originTerminalZip: String? = null,

    @field:Element(name = "ORIGTERMPHONE", required = false)
    var originTerminalPhone: String? = null,

    @field:Element(name = "TYPE", required = false)
    var originTerminalType: String? = null
)

@Root(name = "DESTTERMINFO", strict = false)
data class DestinationTerminalInfo(
    @field:Element(name = "DESTTERMADDRESS", required = false)
    var destinationTerminalAddress: String? = null,

    @field:Element(name = "DESTTERMCITY", required = false)
    var destinationTerminalCity: String? = null,

    @field:Element(name = "DESTTERMSTATE", required = false)
    var destinationTerminalState: String? = null,

    @field:Element(name = "DESTTERMZIP", required = false)
    var destinationTerminalZip: String? = null,

    @field:Element(name = "DESTTERMPHONE", required = false)
    var destinationTerminalPhone: String? = null,

    @field:Element(name = "TYPE", required = false)
    var destinationTerminalType: String? = null
)

@Root(name = "ABF", strict = false)
data class QuoteResponse(
    @field:Element(name = "NUMERRORS", required = false)
    var numErrors: Int? = null,

    @field:ElementList(name = "ERROR", required = false)
    var errors: List<Error>? = mutableListOf(),

    @field:Element(name = "QUOTEID", required = false)
    var quoteID: String? = null,

    @field:Element(name = "CHARGE", required = false)
    var charge: Float = 0f,

    @field:Element(name = "ADVERTISEDTRANSIT", required = false)
    var transitTime: String? = null,

    @field:Element(name = "EXPIRATIONDATE", required = false)
    var expirationDate: String? = null,

    @field:Element(name = "ORIGTERMINFO", required = false)
    var originInfo: OriginTerminalInfo? = null,

    @field:Element(name = "DESTTERMINFO", required = false)
    var destinationInfo: DestinationTerminalInfo? = null
)
