package org.xdemo.app.xutils.j2se.FileWatch;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件夹监控
 * 
 * @author Goofy <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2015年7月3日 上午9:21:33
 */
public class WatchDir {

	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private final boolean subDir;

	/**
	 * 构造方法
	 * 
	 * @param file
	 *            文件目录，不可以是文件
	 * @param subDir
	 * @throws Exception
	 */
	public WatchDir(File file, boolean subDir, FileActionCallback callback) throws Exception {
		if (!file.isDirectory())
			throw new Exception(file.getAbsolutePath() + "is not a directory!");

		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();
		this.subDir = subDir;

		Path dir = Paths.get(file.getAbsolutePath());

		if (subDir) {
			registerAll(dir);
		} else {
			register(dir);
		}
		processEvents(callback);
	}

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	/**
	 * 观察指定的目录
	 * 
	 * @param dir
	 * @throws IOException
	 */
	private void register(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
		keys.put(key, dir);
	}

	/**
	 * 观察指定的目录，并且包括子目录
	 */
	private void registerAll(final Path start) throws IOException {
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				register(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * 发生文件变化的回调函数
	 */
	@SuppressWarnings("rawtypes")
	void processEvents(FileActionCallback callback) {
		for (;;) {
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}
			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("操作未识别");
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				Kind kind = event.kind();

				// 事件可能丢失或遗弃
				if (kind == StandardWatchEventKinds.OVERFLOW) {
					continue;
				}

				// 目录内的变化可能是文件或者目录
				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();
				Path child = dir.resolve(name);
				File file = child.toFile();
				if (kind.name().equals(FileAction.DELETE.getValue())) {
					callback.delete(file);
				} else if (kind.name().equals(FileAction.CREATE.getValue())) {
					callback.create(file);
				} else if (kind.name().equals(FileAction.MODIFY.getValue())) {
					callback.modify(file);
				} else {
					continue;
				}

				// if directory is created, and watching recursively, then
				// register it and its sub-directories
				if (subDir && (kind == StandardWatchEventKinds.ENTRY_CREATE)) {
					try {
						if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
							registerAll(child);
						}
					} catch (IOException x) {
						// ignore to keep sample readbale
					}
				}
			}

			boolean valid = key.reset();
			if (!valid) {
				// 移除不可访问的目录
				// 因为有可能目录被移除，就会无法访问
				keys.remove(key);
				// 如果待监控的目录都不存在了，就中断执行
				if (keys.isEmpty()) {
					break;
				}
			}
		}
	}

}
