package net.lkrnac.hlkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

/**
 * Generates hard links of source files.
 * 
 * @author sitko
 * 
 */
public class HardLinksHandler extends AbstractDirectoryHandler {
	private File targetDir;

	/**
	 * Creates instance of hard links generator for source directory.
	 * 
	 * @param targetDir
	 *            target directory for which is instance of hard link handler
	 *            created in directories comparison
	 */
	public HardLinksHandler(File targetDir) {
		super();
		if (targetDir == null) {
			throw new IllegalArgumentException("targetDir can't be null");
		}
		this.targetDir = targetDir;
	}

	/**
	 * @return target directory for this hard links handler.
	 */
	public File getTargetDir() {
		return targetDir;
	}

	/**
	 * Verify if all files in target directory are hard links of files in source
	 * directory.
	 * 
	 * @param sourceDir
	 *            source directory to compare
	 * @return true - if all target files are hard links
	 * @throws IOException
	 *             if I/O error occurs
	 */
	public final boolean verifyHardLinks(File sourceDir) throws IOException {
		boolean result = false;
		for (File targetFile : targetDir.listFiles()) {
			boolean hasHardLink = false;
			for (File sourceFile : sourceDir.listFiles()) {
				if (getFileKey(targetFile).equals(getFileKey(sourceFile))) {
					hasHardLink = true;
					break;
				}
			}
			if (hasHardLink) {
				result |= hasHardLink;
			} else {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * Reads hard link key for file.
	 * 
	 * @param file
	 *            file on disk
	 * @return hard link key value
	 * @throws IOException
	 *             if I/O error occurs
	 */
	private static Object getFileKey(File file) throws IOException {
		Path filePath = FileSystems.getDefault().getPath(file.getAbsolutePath());
		BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
		return attrs.fileKey();
	}

	/**
	 * Builds hard links in {@link HardLinksHandler#getTargetDir()}. Original
	 * files are read from given source directory
	 * 
	 * @param sourceDir
	 *            source directory from which to create hard links
	 * @param dirComparator
	 *            comparator of media directories
	 * @return if hard link were created in target directory
	 */
	public final boolean buildHardLinks(File sourceDir, DirectoryComparator dirComparator) {
		boolean hardLinksCreated = false;
		try {
			DirectoryComparisonResult result = dirComparator.compareDirectories(sourceDir,
					this.getTargetDir());
			if (result.areMirrors()) {
				File[] sourceFiles = sourceDir.listFiles();
				File[] targetFiles = targetDir.listFiles();
				fileFacingLoop(Arrays.asList(targetFiles), Arrays.asList(sourceFiles));
				hardLinksCreated = true;
			}
		} catch (IOException ioException) {
			hardLinksCreated = false;
		}
		return hardLinksCreated;
	}

	/**
	 * Verifies if files are hard links and if not deletes file in target
	 * directory and creates hard link copy of source file.
	 * <p>
	 * <b> Javadoc from parent class:<br>
	 * </b> {@inheritDoc}
	 */
	@Override
	protected final void performActionFace(File targetFile, File sourceFile) throws IOException {
		if (!getFileKey(targetFile).equals(getFileKey(sourceFile))) {
			targetFile.delete();
			Files.createLink(Paths.get(targetFile.getAbsolutePath()),
					Paths.get(sourceFile.getAbsolutePath()));
		}
	}

	/**
	 * Creates hard link of target file in source directory.
	 * <p>
	 * <b> Javadoc from parent class:<br>
	 * </b> {@inheritDoc}
	 */
	@Override
	protected final void performActionMissingInSource(File targetFile) {
		// TODO not implemented
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}
}
