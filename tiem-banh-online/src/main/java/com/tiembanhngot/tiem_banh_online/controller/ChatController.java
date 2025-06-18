package com.tiembanhngot.tiem_banh_online.controller; // HOẶC PACKAGE PHÙ HỢP

import com.tiembanhngot.tiem_banh_online.service.RAGService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private RAGService ragService;

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody Map<String, String> body) {
        String question = body.get("question");
        String answer = ragService.getAnswer(question);
        return ResponseEntity.ok(Map.of("answer", answer));
    }
}