package com.onetwoclass.onetwoclass.controller.customer;

import com.onetwoclass.onetwoclass.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer/store")
@RequiredArgsConstructor
public class CustomerStoreController {

  private final StoreService storeService;

  @GetMapping
  ResponseEntity<?> getAllStore(Pageable pageable) {

    return ResponseEntity.ok(storeService.getAllStore(pageable));
  }

  @GetMapping("/{name}")
  ResponseEntity<?> getAllStoreByName(@PathVariable String name, Pageable pageable) {

    return ResponseEntity.ok(storeService.getAllStoreByName(pageable, name));
  }

}
