package org.xdemo.app.xutils.j2se.FileWatch;

import java.io.File;

/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年9月6日 上午10:57:42
 */
public abstract  class FileWatch extends Thread {

	static final public long DEFAULT_DELAY = 3000;
	protected String filename;
	
	/**
	 * The delay to observe between every check. By default set
	 * {@link #DEFAULT_DELAY}.
	 */
	protected long delay = DEFAULT_DELAY;

	File file;
	long lastModif = 0;

	public FileWatch(String filename) {
		super(filename);
		this.filename = filename;
		file = new File(filename);
		checkAndConfigure();
		this.start();
	}

	
	public void setDelay(long delay) {
		this.delay = delay;
	}

	public abstract void onChange();

	protected void checkAndConfigure() {
		long l = file.lastModified(); 
		lastModif=lastModif==0?l:lastModif;
		if (l > lastModif) {
			lastModif = l; 
			onChange();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(DEFAULT_DELAY);
			} catch (InterruptedException e) {
			}
			checkAndConfigure();
		}
	}
	
}
