package com.yeshuwahane.task.data.model

data class Data(
    val about_the_brand: Any,
    val attribute_set_id: String,
    val brand: String,
    val brand_banner_url: String,
    val brand_name: String,
    val bundle_options: List<Any>,
    val celebrity_id: Int,
    val configurable_option: List<ConfigurableOption>,
    val created_at: String,
    val description: String,
    val final_price: String,
    val has_options: String,
    val how_to_use: Any,
    val id: String,
    val image: String,
    val images: List<String>,
    val is_best_seller: Int,
    val is_following_brand: Int,
    val is_new: Int,
    val is_return: Int,
    val is_salable: Boolean,
    val is_sale: Int,
    val is_trending: Int,
    val key_ingredients: Any,
    val manufacturer: Any,
    val meta_description: String,
    val meta_keyword: Any,
    val meta_title: String,
    val name: String,
    val options: List<Any>,
    val price: String,
    val related: List<Any>,
    val remaining_qty: Int,
    val returns_and_exchanges: Any,
    val review: Review,
    val shipping_and_delivery: Any,
    val short_description: Any,
    val size_chart: Any,
    val sku: String,
    val status: String,
    val type: String,
    val updated_at: String,
    val upsell: List<Any>,
    val web_url: String,
    val weight: Any,
    val wishlist_item_id: Int
)