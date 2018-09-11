package com.mrhan.string;

public class StringQuery {
	/**
	 * 测试区域
	 */
	private String testAreaString;
	/**
	 * 查找的字符
	 */
	private String findString;

	private int row;//所在行
	private String rowTxt;//当前行的字符


	/**
	 * 找到的下标
	 */
	private int findIndex;

	/**
	 * ?找到的下标 所在行
	 */
	private int rowIndex;

	public StringQuery(String testAreaString, String findString, int findIndex) {
		super();
		this.testAreaString = testAreaString;
		this.findString = findString;
		this.findIndex = findIndex;
	}
	public String getTestAreaString() {
		return testAreaString;
	}


	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public String getRowTxt() {
		return rowTxt;
	}
	public void setRowTxt(String rowTxt) {
		this.rowTxt = rowTxt;
	}
	public void setTestAreaString(String testAreaString) {
		this.testAreaString = testAreaString;
	}
	public String getFindString() {
		return findString;
	}
	public void setFindString(String findString) {
		this.findString = findString;
	}
	public int getFindIndex() {
		return findIndex;
	}
	public void setFindIndex(int findIndex) {
		this.findIndex = findIndex;
	}
	@Override
	public String toString() {
		return "结果 [所在行 "+row+" ,行下标 = "+rowIndex+" , 区域下标 =" + findIndex
				+ ", 匹配字符串 =" + findString + ", 匹配区域字符串 =" + (testAreaString.length()<100?testAreaString.replace("\n",""):(testAreaString.substring(0,100)+"...").replace("\n",""))
				+ " ]";
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}



}
