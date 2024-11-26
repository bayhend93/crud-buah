package com.example.spring.boot.crud.spring_boot.controllers;

import com.example.spring.boot.crud.spring_boot.JwtUtil;
import com.example.spring.boot.crud.spring_boot.entities.Buah;
import com.example.spring.boot.crud.spring_boot.repositories.BuahRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class BuahController {
    private final BuahRepository buahRepository;

    public BuahController(BuahRepository buahRepository) {
        this.buahRepository = buahRepository;
    }

    @GetMapping("/api/all-buah")
    public ResponseEntity<List<Buah>> getAllBuah(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authorizationHeader.substring(7); // Get token part
        if (!isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Buah> buahList = (List<Buah>) buahRepository.findAll();
        return ResponseEntity.ok(buahList);
    }

    @PostMapping("/api/add-buah")
    public ResponseEntity<?> addBuah(@RequestBody @Validated Buah buah, BindingResult result,@RequestHeader("Authorization") String authorizationHeader) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authorizationHeader.substring(7); // Get token part
        if (!isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Buah savedBuah = buahRepository.save(buah);
        return ResponseEntity.ok(savedBuah);
    }

    @GetMapping("/api/buah/{id}")
    public ResponseEntity<Buah> getBuahById(@PathVariable("id") Long id,@RequestHeader("Authorization") String authorizationHeader) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null); // Return a bad request response for null id
        }
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authorizationHeader.substring(7); // Get token part
        if (!isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Buah buah = buahRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buah not found with id: " + id));
        return ResponseEntity.ok(buah);
    }
    @PostMapping("/api/update/{id}")
    public ResponseEntity<Buah> updateBuah(@PathVariable("id") long id, @RequestBody Buah updatedBuah,@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authorizationHeader.substring(7); // Get token part
        if (!isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Buah buah = buahRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buah not found with id: " + id));

        buah.setName(updatedBuah.getName());
        buahRepository.save(buah);
        return ResponseEntity.ok(buah);
    }

    @DeleteMapping("/api/delete/{id}")
    public ResponseEntity<String> deleteBuah(@PathVariable("id") long id,@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authorizationHeader.substring(7); // Get token part
        if (!isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Buah buah = buahRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid buah Id: " + id));
        buahRepository.delete(buah);
        return ResponseEntity.ok("Buah deleted successfully");
    }

    @GetMapping("/form-add-buah")
    public String showSignUpForm(Buah buah) {
        return "add-buah";
    }

    @GetMapping("/form-list-buah")
    public String listBuah(Buah buah) {
        return "list-buah";
    }

    @GetMapping("/form-edit-buah/{id}")
    public String editForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("id", id);
        return "update-buah";
    }

    @GetMapping("/shards.min.css")
    public String showShardsMinCss(Buah buah) {
        return "shards.min.css";
    }

    private boolean isValidToken(String token) {
        return JwtUtil.validateToken(token);
    }
    private String extractUsernameFromToken(String token) {
        return JwtUtil.extractUsername(token);
    }
}
