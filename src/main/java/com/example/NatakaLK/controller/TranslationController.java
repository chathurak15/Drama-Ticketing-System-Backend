package com.example.NatakaLK.controller;

import com.example.NatakaLK.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@CrossOrigin
public class TranslationController {
    @Autowired
    private TranslationService translationService;

    @GetMapping("/translate")
    public ResponseEntity<String> translate(
            @RequestParam String text,
            @RequestParam String lang) {

        String translated = translationService.translateText(text, lang);
        return ResponseEntity.ok(translated);
    }

}
