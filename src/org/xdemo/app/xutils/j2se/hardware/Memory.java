package org.xdemo.app.xutils.j2se.hardware;

public class Memory {
	
	/**
	 * 可用空间
	 */
	private Long available;
	/**
	 * 总空间
	 */
	private Long total;
	
	public Memory() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Memory(Long available, Long total) {
		super();
		this.available = available;
		this.total = total;
	}
	public Long getAvailable() {
		return available;
	}
	public void setAvailable(Long available) {
		this.available = available;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}

}
