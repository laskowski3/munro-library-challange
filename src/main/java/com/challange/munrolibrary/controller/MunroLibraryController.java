package com.challange.munrolibrary.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.challange.munrolibrary.model.MunroLibraryData;
import com.challange.munrolibrary.service.MunroLibraryService;

@RestController
@RequestMapping("/munro")
public class MunroLibraryController {

	@Autowired
	MunroLibraryService munroLibraryService;

	@GetMapping("/")
	public List<MunroLibraryData> getMunroLibraryData(@RequestParam Optional<String> hillCategory,
			@RequestParam Optional<String> sortByHeightInMeters, @RequestParam Optional<String> sortByName,
			@RequestParam Optional<String> totalNumberOfResults, @RequestParam Optional<String> maxHeightInMeters,
			@RequestParam Optional<String> minHeightInMeters, @RequestParam Optional<String> sortingPreference)
			throws Exception {
		List<MunroLibraryData> response = new ArrayList<MunroLibraryData>();
		response = munroLibraryService.processMunroLibraryData(hillCategory, sortByHeightInMeters, sortByName,
				totalNumberOfResults, maxHeightInMeters, minHeightInMeters, sortingPreference);
		return response;
	}
}
