package com.mnc.tinyurl.controller;

import com.mnc.tinyurl.model.ShortCodeRequest;
import com.mnc.tinyurl.service.ShortCodeGeneratorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ShortCodeController {

    private final ShortCodeGeneratorService service;

    public ShortCodeController(ShortCodeGeneratorService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public Map<String, String> shorten(@RequestBody ShortCodeRequest request) {

        String longUrl = request.getUrl();
        String code = service.generateShortCode(longUrl);

        return Map.of(
                "url", longUrl,
                "shortCode", code
        );
    }

    @GetMapping("/shorten/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        String longUrl = service.resolve(shortCode);

        if (longUrl == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create(longUrl))
                .build();
    }
}
