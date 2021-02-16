package com.challange.munrolibrary;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.challange.munrolibrary.constants.AppConstants;
import com.challange.munrolibrary.model.MunroLibraryData;
import com.challange.munrolibrary.service.MunroLibraryService;

@RunWith(MockitoJUnitRunner.class)
public class MunroLibraryServiceTests {
	@InjectMocks
	private MunroLibraryService service;

	@Before
	public final void init() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public final void testCategoryFilterTopSortByNameAsc() throws Exception {
		List<MunroLibraryData> data = service.readMunroDataFromCSV();
		data = data.stream().filter(x -> x.getHillCategory().equalsIgnoreCase(AppConstants.HILL_TYPE_TOP))
				.collect(Collectors.toList());
		data.sort(Comparator.comparing(MunroLibraryData::getName));
		List<MunroLibraryData> result = service.processMunroLibraryData(Optional.of(AppConstants.HILL_TYPE_TOP),
				Optional.ofNullable(null), Optional.of(AppConstants.ASC), Optional.ofNullable(null),
				Optional.ofNullable(null), Optional.ofNullable(null), Optional.ofNullable(null));

		assertEquals(data.size(), result.size());
		assertEquals(227, result.size());
		assertEquals(data.get(2).getName(), result.get(2).getName());
		assertEquals(data.get(100).getName(), result.get(100).getName());
	}

	@Test
	public final void testMinAndMaxHeightOrderByHeightDesc() throws Exception {
		List<MunroLibraryData> data = service.readMunroDataFromCSV();
		data = data.stream().filter(x -> x.getHeight() >= 940 && x.getHeight() <= 941).collect(Collectors.toList());
		data.sort(Comparator.comparing(MunroLibraryData::getHeight).reversed());
		List<MunroLibraryData> result = service.processMunroLibraryData(Optional.ofNullable(null),
				Optional.of(AppConstants.DESC), Optional.ofNullable(null), Optional.ofNullable(null),
				Optional.of("941"), Optional.of("940"), Optional.ofNullable(null));

		assertEquals(data.size(), result.size());
		assertEquals(data.get(0).getName(), result.get(0).getName());
		assertEquals(data.get(1).getName(), result.get(1).getName());
	}

	@Test
	public final void testSortingWithPreferenceHeightDescThenNameAsc() throws Exception {
		List<MunroLibraryData> data = service.readMunroDataFromCSV();
		data.sort(
				Comparator.comparing(MunroLibraryData::getHeight).reversed().thenComparing(MunroLibraryData::getName));
		List<MunroLibraryData> result = service.processMunroLibraryData(Optional.ofNullable(null),
				Optional.of(AppConstants.ASC), Optional.of(AppConstants.DESC), Optional.ofNullable(null),
				Optional.ofNullable(null), Optional.ofNullable(null), Optional.of(AppConstants.PREFERENCE_SORT_HEIGHT));

		assertEquals(data.size(), result.size());
		assertEquals(data.get(0).getName(), result.get(0).getName());
		assertEquals(data.get(data.size() - 1).getName(), result.get(data.size() - 1).getName());
	}

}
