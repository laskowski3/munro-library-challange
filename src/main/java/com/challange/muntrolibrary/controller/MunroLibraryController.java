package com.challange.muntrolibrary.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/new")
public class MunroLibraryController {

	@GetMapping("/")
	public String getMunroLibraryData(@RequestParam String d) {
		return "";
	}
}
