package net.lkrnac.hlkit;

/**
 * Results of directory comparison.
 * 
 * @author sitko
 * 
 */
public enum DirectoryComparisonResult {
	/** Directories can be considered as identical mirrors. */
	EQUAL,

	/**
	 * Files in comparing directories are different, probably incorrectly paired
	 * directories.
	 */
	DIFFERENT_FILES,

	/** Missing media files in source directory. */
	MISSING_MEDIA_FILES_IN_SOURCE,

	/** Missing media files in target directory. */
	MISSING_MEDIA_FILES_IN_TARGET;

	/**
	 * @return if comparison result is indicating that compared directories are
	 *         considered as mirrors
	 */
	public boolean areMirrors() {
		boolean result = false;
		if (EQUAL.equals(this) || MISSING_MEDIA_FILES_IN_TARGET.equals(this)) {
			result = true;
		}
		return result;
	}
}
