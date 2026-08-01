package com.spring.ai.controller;

import com.spring.ai.service.MultiModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/multi-model")
public class MultiModelController {

    private MultiModelService multiModelService;

    public MultiModelController(MultiModelService multiModelService) {
        this.multiModelService = multiModelService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> getChatResponse(@RequestParam(value="model") String model, @RequestParam(value="q") String query) {
        return ResponseEntity.ok(multiModelService.getChatResponse(model, query));
    }
}
