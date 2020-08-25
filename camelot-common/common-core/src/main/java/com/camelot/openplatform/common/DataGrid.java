package com.camelot.openplatform.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.Page;

/**
 * DataGrid模型
 * 
 */
public class DataGrid<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long total = 0L;	//总记录数
	private List<T> rows = new ArrayList<T>();	 //结果集
	
	private int pageNum =0;    // 第几页
    private int pageSize =0;    // 每页记录数
    private int pages =0;        // 总页数
    private int size =0;        // 当前页的数量 <= pageSize，该属性来自ArrayList的size属性
    private int startRow;		//起始行
    private int endRow;			//末行
    

	/**
     * 包装Page对象，因为直接返回Page对象，在JSON处理以及其他情况下会被当成List来处理，
     * 而出现一些问题。
     * @param list          page结果
     * @param navigatePages 页码数量
     */
    public DataGrid(List<T> rows) {
       this.setRows(rows);
    }
    
	public DataGrid() {
	}

	public void setRows(List<T> rows) {
		if (rows instanceof Page) {
            Page<T> page = (Page<T>) rows;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.rows = page;
            this.size = page.size();
            this.startRow=page.getStartRow();
            this.endRow=page.getEndRow();
        }else{
        	this.rows = rows;
        }
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public List<T> getRows() {
		return rows;
	}
	
}
