package net.lkrnac.hlkit;

import java.io.File;
import java.io.IOException;

/**
 * Facade for directory I/O handlers. Lazy initialization of I/O handlers
 * 
 * @author sitko
 * 
 */
public class DirectoryIoFacade {
	private final File targetDir;
	private DirectoryComparator directoryComparator;
	private HardLinksHandler hardLinksHandler;

	/**
	 * Creates directory facade instance.
	 * 
	 * @param targetDir
	 *            target directory to which directory IO facade belongs
	 */
	public DirectoryIoFacade(File targetDir) {
		super();
		this.targetDir = targetDir;
	}

	/**
	 * Compares directories based on file names and file sizes. If files from
	 * target directory matches files in source directory, <code>true</code> is
	 * returned.
	 * 
	 * @param sourceDir
	 *            directory in which are files matched
	 * @param targetDir
	 *            all files in this directory should match files from sourceDir
	 * @return <code>true</code> if all files from sourceDir match files in
	 *         sourceDir (based on file size and file name)
	 * @throws IOException
	 *             if some I/O error occurs
	 */
	public DirectoryComparisonResult compareDirectories(File sourceDir, File targetDir)
			throws IOException {
		return getDirectoryComparator().compareDirectories(sourceDir, targetDir);
	}

	/**
	 * Verify if all files in target directory are hard links of files in source
	 * directory.
	 * 
	 * @param sourceDir
	 *            source media directory to compare
	 * @return true - if all source files are hard links
	 * @throws IOException
	 *             if I/O error occurs
	 */
	public boolean verifyHardLinks(File sourceDir) throws IOException {
		return getHardLinksHandler().verifyHardLinks(sourceDir);
	}

	/**
	 * Builds hard links in {@link HardLinksHandler#getTargetDir()}. Original
	 * files are read from given source directory
	 * 
	 * @param sourceDir
	 *            source directory from which to create hard links
	 */
	public void buildHardLinks(File sourceDir) {
		this.getHardLinksHandler().buildHardLinks(sourceDir, getDirectoryComparator());
	}

	/**
	 * @return directory comparator I/O handler
	 */
	private DirectoryComparator getDirectoryComparator() {
		if (directoryComparator == null) {
			directoryComparator = new DirectoryComparator();
		}
		return directoryComparator;
	}

	/**
	 * @return directory hard links I/O handler
	 */
	private HardLinksHandler getHardLinksHandler() {
		if (hardLinksHandler == null) {
			hardLinksHandler = new HardLinksHandler(targetDir);
		}
		return hardLinksHandler;
	}
}
