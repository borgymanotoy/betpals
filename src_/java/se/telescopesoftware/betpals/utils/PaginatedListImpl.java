package se.telescopesoftware.betpals.utils;

import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;

public class PaginatedListImpl implements PaginatedList {

	private int fullListSize;
	private List<Object> currentList;
	private int objectsPerPage;
	private int pageNumber;

	private String searchId;
	private String sortCriterion;
	private SortOrderEnum sortDirection;
	
	public PaginatedListImpl(int fullListSize, List<Object> currentList,
			int objectsPerPage, int pageNumber) {
		super();
		this.fullListSize = fullListSize;
		this.currentList = currentList;
		this.objectsPerPage = objectsPerPage;
		this.pageNumber = pageNumber;
	}

	public int getFullListSize() {
		return this.fullListSize;
	}

	public List<Object> getList() {
		return this.currentList;
	}

	public int getObjectsPerPage() {
		return this.objectsPerPage;
	}

	public int getPageNumber() {
		return this.pageNumber + 1;
	}

	public String getSearchId() {
		return this.searchId;
	}

	public String getSortCriterion() {
		return this.sortCriterion;
	}

	public SortOrderEnum getSortDirection() {
		return this.sortDirection != null ? this.sortDirection : SortOrderEnum.DESCENDING;
	}

	public List<Object> getCurrentList() {
		return currentList;
	}

	public void setCurrentList(List<Object> currentList) {
		this.currentList = currentList;
	}

	public void setFullListSize(int fullListSize) {
		this.fullListSize = fullListSize;
	}

	public void setObjectsPerPage(int objectsPerPage) {
		this.objectsPerPage = objectsPerPage;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	public void setSortCriterion(String sortCriterion) {
		this.sortCriterion = sortCriterion;
	}

	public void setSortDirection(SortOrderEnum sortDirection) {
		this.sortDirection = sortDirection;
	}

}
