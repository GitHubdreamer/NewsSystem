package magicgis.newssystem.models;

import java.util.List;

public class Page<T> extends BaseEntity {

	private static final long serialVersionUID = -5688752889754349099L;
	// ��ǰҳ
	private int currentPage;
	// ÿҳ������
	private int pageSize;
	// ������
	private int totalCount;
	// ��ҳ��
	private int pageCount;
	// ʵ����
	private List<T> list;

	public Page() {

	}

	public Page(int currentPage, int pageSize, int totalCount, List<T> list) {
		super();
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.list = list;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 10 : pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}