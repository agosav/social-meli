package com.socialmeli.socialmeli.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
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
    @Positive
    private Integer userId;

    private LocalDate date;

    @Valid
    private ProductDto product;

    private Integer category;

    private Double price;
}
