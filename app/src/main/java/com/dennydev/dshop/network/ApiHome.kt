package com.dennydev.dshop.network

import com.dennydev.dshop.model.ApiResponse
import com.dennydev.dshop.model.listcategory.ListCategory
import com.dennydev.dshop.model.productresponse.ProductResponse
import com.dennydev.dshop.model.sliderresponse.SliderResponse

interface ApiHome {
    suspend fun getSlider(): ApiResponse<SliderResponse>
    suspend fun getNewestProduct(): ApiResponse<ProductResponse>
    suspend fun getCategory(): ApiResponse<ListCategory>
}