package com.socialmeli.socialmeli.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"user_id"})
public class PostDto {
    @JsonProperty("user_id")
    private Integer userId;

    private LocalDate date;

    private ProductDto product;

    private Integer category;

    private Double price;
}
