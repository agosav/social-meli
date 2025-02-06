package com.socialmeli.socialmeli.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"product_id", "product_name"})
public class ProductDto {
    @JsonProperty("product_id")
    private Integer id;

    @JsonProperty("product_name")
    private String name;

    @NotBlank
    private String type;

    private String brand;

    private String color;

    private String notes;
}
