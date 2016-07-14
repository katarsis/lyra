package ru.katarsis.lyra.dto;

public class CSVData {
	
	public String data;
	public String categoryAttr;
	public String ignoredAttr;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

    public String getCategoryAttr() {
        return categoryAttr;
    }

    public void setCategoryAttr(String categoryAttr) {
        this.categoryAttr = categoryAttr;
    }

    public String getIgnoredAttr() {
        return ignoredAttr;
    }

    public void setIgnoredAttr(String ignoredAttr) {
        this.ignoredAttr = ignoredAttr;
    }

}
