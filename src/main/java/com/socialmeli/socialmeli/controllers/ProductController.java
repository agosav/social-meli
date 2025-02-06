package com.socialmeli.socialmeli.controllers;
import com.socialmeli.socialmeli.dto.PostDto;
import com.socialmeli.socialmeli.dto.response.ProductListDto;
import com.socialmeli.socialmeli.dto.response.ProductSaleCountDto;
import com.socialmeli.socialmeli.dto.response.MessageDto;
import com.socialmeli.socialmeli.services.IPostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.socialmeli.socialmeli.dto.PostSaleDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final IPostService postService;

    // US 0005 - Dar de alta una nueva publicación.
    @PostMapping("/post")
    public ResponseEntity<MessageDto> addPost(@RequestBody @Valid PostDto postDto) {
        return ResponseEntity.ok(postService.savePost(postDto));
    }

    // US 0006 - Obtener un listado de las publicaciones realizadas en las últimas dos semanas,
    // por los vendedores que un usuario sigue
    @GetMapping("/followed/{userId}/list")
    public ResponseEntity<ProductListDto> getPostsOfFollowedSellers(
            @Positive @PathVariable  Integer userId,
            @RequestParam(defaultValue = "date_desc") @Pattern(regexp = "^(date_asc|date_desc)$",
                    message = "Invalid order. Allowed values are 'date_asc' or 'date_desc'.") String order) {
        return ResponseEntity.ok(postService.getRecentPostFromUsers(order, userId));
    }

    // US 0010 - Llevar a cabo la publicación de un nuevo producto en promoción.
    @PostMapping("/promo-post")
    public ResponseEntity<MessageDto> addPostSale(@RequestBody @Valid PostSaleDto postSaleDto) {
        return ResponseEntity.ok(postService.savePostSale(postSaleDto));
    }

    // US 0011 - Obtener la cantidad de productos en promoción de un determinado vendedor.
    @GetMapping("promo-post/count")
    public ResponseEntity<ProductSaleCountDto> getPromoPostCount(@Positive @RequestParam("user_id") Integer userId) {
        return ResponseEntity.ok(postService.getProductSaleCountByUser(userId));
    }
}
