package com.socialmeli.socialmeli.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.socialmeli.socialmeli.utils.LocalDateDeserializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"user_id"})
public class PostDto {
    @JsonProperty("user_id")
    @Positive(message = "User id must be positive")
    @NotNull(message = "User id is required")
    private Integer userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @Valid
    @NotNull(message = "Product is required")
    private ProductDto product;

    @NotNull(message = "Category is required")
    private Integer category;

    @NotNull(message = "Price is required")
    @DecimalMax(value = "10000000.0", message = "Price must be less than 10000000.0")
    private Double price;
}
