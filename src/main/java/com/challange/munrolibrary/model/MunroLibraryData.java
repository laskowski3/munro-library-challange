package com.challange.munrolibrary.model;

import com.opencsv.bean.CsvBindByName;

public class MunroLibraryData {
	@CsvBindByName(column = "Post 1997")
	private String post1997;

	@CsvBindByName(column = "Height (m)")
	private double height;

	@CsvBindByName(column = "Name")
	private String name;

	@CsvBindByName(column = "Grid Ref")
	private String gridRef;

	public String getPost1997() {
		return post1997;
	}

	public void setPost1997(String post1997) {
		this.post1997 = post1997;
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
