package com.example.pricingapp

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "TravelerinformationResponse", strict = false)
data class QuoteResponse(
    @field:Element(name = "page")
    var page: Int = 0,

    @field:Element(name = "total_pages")
    var totalPages: Int = 0
)
