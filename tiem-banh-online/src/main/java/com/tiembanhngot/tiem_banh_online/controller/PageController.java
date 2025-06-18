package com.tiembanhngot.tiem_banh_online.controller; // HOẶC PACKAGE PHÙ HỢP

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController { // Hoặc một controller hiện có

    @GetMapping("/tiembanh")
    public String chatPage(Model model) {
        model.addAttribute("currentPage", "chat"); // Cho active link header nếu có
        return "chat"; // Trả về view templates/chat.html
    }
}