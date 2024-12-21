package com.example.pixeltest.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity("product")
data class ProductData(
    @PrimaryKey
    @SerializedName("id", alternate = ["Id"]) val id: String,
    @SerializedName("brand", alternate = ["Brand"]) val brand: String? = null,
    @SerializedName("model", alternate = ["Model"]) val model: String? = null,
    @SerializedName("description", alternate = ["Description"]) val description: String? = null,
    @SerializedName("price", alternate = ["Price"]) val price: String? = null,
    @SerializedName("image", alternate = ["Image"]) val image: String? = null,
    @SerializedName("name", alternate = ["Name"]) val name: String? = null,
    @SerializedName("createdAt", alternate = ["CreatedAt"]) val createdAt: String? = null,
    var isFavorite: Boolean = false,
    var isInCart: Boolean = false,
    var quantity: Int = 1
)
