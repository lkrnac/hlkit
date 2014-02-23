package net.lkrnac.hlkit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Class for comparing directories.
 * 
 * @author sitko
 */
public class DirectoryComparator extends AbstractDirectoryHandler {
	private Collection<File> missingFilesInTarget;
	private Collection<File> missingFilesInSource;

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
		DirectoryComparisonResult result = null;

		if (sourceDir != null && targetDir != null) {
			File[] sourceFiles = sourceDir.listFiles();
			File[] targetFiles = targetDir.listFiles();
			if (sourceFiles.length == NumberUtils.INTEGER_ZERO
					|| targetFiles.length == NumberUtils.INTEGER_ZERO) {
				result = DirectoryComparisonResult.DIFFERENT_FILES;
			} else {
				result = performComparison(sourceFiles, targetFiles);
			}
		}
		return result;
	}

	/**
	 * Performs comparison of directories.
	 * <p>
	 * NOMPD: Can't think of better result matcher for now
	 * 
	 * @param sourceFiles
	 *            array of file names belonging to source directory
	 * @param targetFiles
	 *            array of file names belonging to target directory
	 * @return comparison result
	 * @throws IOException
	 *             if I/O error occurs
	 */
	@SuppressWarnings("PMD.ConfusingTernary")
	private DirectoryComparisonResult performComparison(File[] sourceFiles, File[] targetFiles)
			throws IOException {
		missingFilesInTarget = new ArrayList<>(Arrays.asList(sourceFiles));
		missingFilesInSource = new ArrayList<>();
		DirectoryComparisonResult result = null;

		// do the comparison
		super.fileFacingLoop(Arrays.asList(targetFiles), Arrays.asList(sourceFiles));
		if (missingFilesInTarget.isEmpty() && missingFilesInSource.isEmpty()) {
			result = DirectoryComparisonResult.EQUAL;
		} else if (!missingFilesInTarget.isEmpty() && !missingFilesInSource.isEmpty()) {
			result = DirectoryComparisonResult.DIFFERENT_FILES;
		} else if (!missingFilesInTarget.isEmpty()) {
			result = DirectoryComparisonResult.MISSING_MEDIA_FILES_IN_TARGET;
		} else if (!missingFilesInSource.isEmpty()) {
			result = DirectoryComparisonResult.MISSING_MEDIA_FILES_IN_SOURCE;
		}
		return result;
	}

	/**
	 * Remove source file from unmatched list.
	 * <p>
	 * <b> Javadoc from parent class:<br>
	 * </b> {@inheritDoc}
	 */
	@Override
	protected final void performActionFace(File targetFile, File sourceFile) throws IOException {
		missingFilesInTarget.remove(sourceFile);
	}

	/**
	 * Saves missing target file into private cache.
	 * <p>
	 * <b> Javadoc from parent class:<br>
	 * </b> {@inheritDoc}
	 */
	@Override
	protected void performActionMissingInSource(File targetFile) {
		missingFilesInSource.add(targetFile);
	}
}
