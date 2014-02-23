package net.lkrnac.hlkit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Abstract class containing common logic for directory handlers.
 * 
 * @author sitko
 * 
 */
public abstract class AbstractDirectoryHandler {
	/**
	 * Factory method that runs through target directory. For each target item
	 * finds belonging source mirror and performs action. This action is
	 * implemented in child classes.
	 * 
	 * @param sourceFiles
	 *            collection of files in source directory
	 * @param targetFiles
	 *            collection of files in target directory
	 * @throws IOException
	 *             if some I/O error occurs
	 */
	protected final void fileFacingLoop(Collection<File> targetFiles, Collection<File> sourceFiles)
			throws IOException {
		Collection<File> missingFilesInSourceDir = new ArrayList<>(targetFiles);

		// do the comparison
		for (File targetFile : targetFiles) {
			for (File sourceFile : sourceFiles) {
				if (targetFile.getName().equals(sourceFile.getName())
						&& targetFile.length() == sourceFile.length()) {
					performActionFace(targetFile, sourceFile);
					missingFilesInSourceDir.remove(targetFile);
					break;
				}
			}
		}
		for (File missingFileInSrouce : missingFilesInSourceDir) {
			performActionMissingInSource(missingFileInSrouce);
		}
	}

	/**
	 * Performs action for files that are matched in target and source
	 * directory.
	 * 
	 * @param targetFile
	 *            file in target directory
	 * @param sourceFile
	 *            file in source directory
	 * @throws IOException
	 *             if some I/O error occurs
	 */
	protected abstract void performActionFace(File targetFile, File sourceFile) throws IOException;

	/**
	 * Performs action for file missing in source directory.
	 * 
	 * @param targetFile
	 *            file in source directory
	 */
	protected abstract void performActionMissingInSource(File targetFile);
}
