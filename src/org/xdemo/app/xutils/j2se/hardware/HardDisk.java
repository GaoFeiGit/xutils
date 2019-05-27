package org.xdemo.app.xutils.j2se.hardware;

public class HardDisk {
	
	/**
	 * 驱动器名称
	 */
	private String name;
	/**
	 * 可用空间
	 */
	private Long available;
	/**
	 * 总空间
	 */
	private Long total;
	

	public HardDisk() {
		super();
	}

	public HardDisk(String name, Long available, Long total) {
		super();
		this.name = name;
		this.available = available;
		this.total = total;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
