package com.challange.munrolibrary.model;

import com.opencsv.bean.CsvBindByName;

public class MunroLibraryData {
	@CsvBindByName(column = "Post 1997")
	private String hillCategory;

	@CsvBindByName(column = "Height (m)")
	private double height;

	@CsvBindByName(column = "Name")
	private String name;

	@CsvBindByName(column = "Grid Ref")
	private String gridRef;

	public String getHillCategory() {
		return hillCategory;
	}

	public void setHillCategory(String hillCategory) {
		this.hillCategory = hillCategory;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGridRef() {
		return gridRef;
	}

	public void setGridRef(String gridRef) {
		this.gridRef = gridRef;
	}
}
