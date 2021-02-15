package com.challange.munrolibrary.model;

import java.util.Comparator;

public class MurComparator implements Comparator<MunroLibraryData> {

	@Override
	public int compare(MunroLibraryData o1, MunroLibraryData o2) {
		return o1.getName().compareTo(o2.getName());
	}

}
