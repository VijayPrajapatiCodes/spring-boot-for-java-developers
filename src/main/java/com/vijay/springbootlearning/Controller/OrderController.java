package com.vijay.springbootlearning.Controller;

import com.vijay.springbootlearning.dto.OrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
//    @PostMapping
//         public OrderRequest createOrder(@RequestBody OrderRequest order){
//          return  order;
//         }

         @PostMapping
        public ResponseEntity<OrderRequest> createOrder(@Valid @RequestBody OrderRequest order){
         return ResponseEntity.status(HttpStatus.CREATED).body(order);
        }

    @GetMapping("/{id}/details")
    public ResponseEntity<String> getOrderDetails(
            @PathVariable int id) {

        return ResponseEntity
                .ok()
                .header("X-App-Name", "SpringBootLearning")
                .header("X-Order-Id", String.valueOf(id))
                .body("Order found: " + id);
    }
    }

