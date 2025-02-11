package com.socialmeli.socialmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaleCountDto {
    @JsonProperty("user_id")
    private Integer id;

    @JsonProperty("user_name")
    private String name;

    @JsonProperty("promo_products_count")
    private Integer promoProductsCount;
}
