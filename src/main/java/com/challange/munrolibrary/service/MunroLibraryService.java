package com.challange.munrolibrary.service;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.challange.munrolibrary.common.ParamException;
import com.challange.munrolibrary.constants.AppConstants;
import com.challange.munrolibrary.model.MunroLibraryData;
import com.challange.munrolibrary.util.Helper;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class MunroLibraryService {
	public boolean validateInputs(Optional<String> hillCategory, Optional<String> sortByHeightInMeters,
			Optional<String> sortByName, Optional<String> totalNumberOfResults, Optional<String> maxHeightInMeters,
			Optional<String> minHeightInMeters, Optional<String> sortPreference) throws ParamException {
		if (hillCategory.isPresent() && (!hillCategory.get().equalsIgnoreCase(AppConstants.HILL_TYPE_TOP)
				&& !hillCategory.get().equalsIgnoreCase(AppConstants.HILL_TYPE_MUNRO)))
			throw new ParamException("Invalid paramters. Expected values for hillCategory are TOP and MUN");

		if (sortByName.isPresent() && (!sortByName.get().equalsIgnoreCase(AppConstants.ASC)
				&& !sortByName.get().equalsIgnoreCase(AppConstants.DESC)))
			throw new ParamException("Invalid paramters. Expected values for sortByName are asc and desc");

		if ((sortByName.isPresent() && !Helper.isAscOrDesc(sortByName.get())
				|| sortByHeightInMeters.isPresent() && !Helper.isAscOrDesc(sortByHeightInMeters.get())))
			throw new ParamException(
					"Invalid paramters. Expected values for sortByName and sortByHeightInMeters are 'asc' and 'desc'");

		if ((minHeightInMeters.isPresent() && !Helper.isNumericDouble(minHeightInMeters.get()))
				|| (maxHeightInMeters.isPresent() && !Helper.isNumericDouble(maxHeightInMeters.get())))
			throw new ParamException("Invalid paramters. Min and max height must be numeric");

		if ((minHeightInMeters.isPresent() && minHeightInMeters.isPresent()
				&& !Helper.isMinSmallerThanMax(minHeightInMeters.get(), maxHeightInMeters.get())))
			throw new ParamException("Invalid paramters. Max must be greater than or equal min");

		if (totalNumberOfResults.isPresent() && !Helper.isNumericInt(totalNumberOfResults.get()))
			throw new ParamException("Invalid paramters. Natural numeric value expected for totalNumberOfResults");

		if (sortPreference.isPresent() && (sortPreference.get().equalsIgnoreCase(AppConstants.PREFERENCE_SORT_NAME)
				&& sortPreference.get().equalsIgnoreCase(AppConstants.PREFERENCE_SORT_HEIGHT)))
			throw new ParamException(
					"Invalid paramters. 'name' and 'height' are expected parameters for sorting preference");

		return true;
	}

	public List<MunroLibraryData> readMunroDataFromCSV() throws Exception {
		List<MunroLibraryData> munroLibraryData;

		try {
			File file = ResourceUtils.getFile("classpath:static/munrotab_v6.2.csv");
			munroLibraryData = new CsvToBeanBuilder<MunroLibraryData>(new FileReader(file))
					.withType(MunroLibraryData.class).build().parse();
			// always exclude record if hill type is blank
			munroLibraryData = munroLibraryData.stream()
					.filter(hill -> !hill.getPost1997().equalsIgnoreCase(AppConstants.EMPTY_STRING)
							&& hill.getPost1997() != null)
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new Exception("Unable to load data from source CSV" + e.getStackTrace());
		}

		return munroLibraryData;
	}

	public List<MunroLibraryData> processMunroLibraryData(Optional<String> hillCategory,
			Optional<String> sortByHeightInMeters, Optional<String> sortByName, Optional<String> totalNumberOfResults,
			Optional<String> maxHeightInMeters, Optional<String> minHeightInMeters, Optional<String> sortingPreference)
			throws Exception {
		validateInputs(hillCategory, sortByHeightInMeters, sortByName, totalNumberOfResults, maxHeightInMeters,
				minHeightInMeters, sortingPreference);
		List<MunroLibraryData> libraryData = readMunroDataFromCSV();

		libraryData = filterData(libraryData, hillCategory, maxHeightInMeters, minHeightInMeters);

		if (sortByHeightInMeters.isPresent() && !sortByName.isPresent()) {
			libraryData = sortMunroDataByHeight(libraryData, sortByHeightInMeters.get());
		} else if (sortByName.isPresent() && !sortByHeightInMeters.isPresent()) {
			libraryData = sortMunroDataByName(libraryData, sortByName.get());
		} else if (sortByName.isPresent() && sortByHeightInMeters.isPresent()) {
			libraryData = sortByNameAndHeight(libraryData, sortByName.get(), sortByHeightInMeters.get(),
					sortingPreference.isPresent() ? sortingPreference.get() : AppConstants.PREFERENCE_SORT_HEIGHT);
		}

		// apply the limit if criteria provided
		if (totalNumberOfResults.isPresent())
			libraryData = limitResults(libraryData, Integer.parseInt(totalNumberOfResults.get()));

		return libraryData;
	}

	/*
	 * Collects all filter criteria and applies them to return filtered list
	 * 
	 * @param munroLibraryData List<MunroLibraryData>
	 * 
	 * @param hillCategory TOP/ MUN/ EITHER String
	 * 
	 * @param maxHeightInMeters Double param minHeightInMeters Double
	 * 
	 * @return List<MunroLibraryData> filtered Munro library data
	 */
	private List<MunroLibraryData> filterData(List<MunroLibraryData> munroLibraryData, Optional<String> hillCategory,
			Optional<String> maxHeightInMeters, Optional<String> minHeightInMeters) {
		List<Predicate<MunroLibraryData>> allPredicates = new ArrayList<Predicate<MunroLibraryData>>();
		if (hillCategory.isPresent())
			// filter if TOP or MUN provided
			allPredicates.add(hill -> hill.getPost1997().equalsIgnoreCase(hillCategory.get()));

		if (maxHeightInMeters.isPresent())
			allPredicates.add(hill -> hill.getHeight() <= Double.parseDouble(maxHeightInMeters.get()));

		if (minHeightInMeters.isPresent())
			allPredicates.add(hill -> hill.getHeight() >= Double.parseDouble(minHeightInMeters.get()));

		if (allPredicates.size() > 0)
			munroLibraryData = munroLibraryData.stream()
					.filter(allPredicates.stream().reduce(x -> true, Predicate::and)).collect(Collectors.toList());

		return munroLibraryData;
	}

	private List<MunroLibraryData> limitResults(List<MunroLibraryData> munroLibraryData, int limit) {
		return munroLibraryData.stream().limit(limit).collect(Collectors.toList());
	}

	private List<MunroLibraryData> sortMunroDataByHeight(List<MunroLibraryData> munroLibraryData, String ascOrDesc) {
		if (ascOrDesc.equalsIgnoreCase(AppConstants.ASC)) {
			munroLibraryData.sort(Comparator.comparing(MunroLibraryData::getHeight));
			return munroLibraryData;
		} else {
			munroLibraryData.sort(Comparator.comparing(MunroLibraryData::getHeight).reversed());
			return munroLibraryData;
		}
	}

	private List<MunroLibraryData> sortMunroDataByName(List<MunroLibraryData> munroLibraryData, String ascOrDesc) {
		if (ascOrDesc.equalsIgnoreCase(AppConstants.ASC)) {
			munroLibraryData.sort(Comparator.comparing(MunroLibraryData::getName));
			return munroLibraryData;
		} else {
			munroLibraryData.sort(Comparator.comparing(MunroLibraryData::getName).reversed());
			return munroLibraryData;
		}
	}

	private List<MunroLibraryData> sortByNameAndHeight(List<MunroLibraryData> munroLibraryData, String sortByName,
			String sortByHeight, String preference) {
		Comparator<MunroLibraryData> comparator;
		if (preference.equalsIgnoreCase(AppConstants.PREFERENCE_SORT_NAME)) {
			comparator = sortByName.equalsIgnoreCase(AppConstants.ASC) ? Comparator.comparing(MunroLibraryData::getName)
					: Comparator.comparing(MunroLibraryData::getName).reversed();
			comparator = sortByHeight.equalsIgnoreCase(AppConstants.ASC)
					? comparator.thenComparing(Comparator.comparing(MunroLibraryData::getHeight))
					: comparator.thenComparing(Comparator.comparing(MunroLibraryData::getHeight).reversed());
		} else {
			comparator = sortByName.equalsIgnoreCase(AppConstants.ASC)
					? Comparator.comparing(MunroLibraryData::getHeight)
					: Comparator.comparing(MunroLibraryData::getHeight).reversed();
			comparator = sortByHeight.equalsIgnoreCase(AppConstants.ASC)
					? comparator.thenComparing(Comparator.comparing(MunroLibraryData::getName))
					: comparator.thenComparing(Comparator.comparing(MunroLibraryData::getName).reversed());
		}

		munroLibraryData = munroLibraryData.stream().sorted(comparator).collect(Collectors.toList());
		return munroLibraryData;
	}

}
