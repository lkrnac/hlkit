package net.lkrnac.hlkit.comparators;

import java.nio.file.Path;
import java.util.Comparator;

/**
 * Comparator for {@link Path} based on file name.
 * 
 * @author lubos krnac
 * 
 */
public class FileNamePathComparator implements Comparator<Path> {
	private boolean caseSensitive;

	/**
	 * Creates {@link FileNamePathComparator} instance. Comparison of file names
	 * will be case sensitive.
	 */
	public FileNamePathComparator() {
		this(true);
	}

	/**
	 * Creates {@link FileNamePathComparator} instance with given case
	 * sensitivity.
	 * 
	 * @param caseSensitive
	 *            flag if comparison should be case sensitive
	 */
	public FileNamePathComparator(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	/**
	 * Compares two {@link Path} objects based on file name.
	 * 
	 * <b>JavaDoc from parent:</b><br>
	 * {@inheritDoc}
	 * 
	 * @param path1
	 *            path object 1 to compare
	 * @param path2
	 *            path object 2 to compare
	 * @return comparison result according {@link Comparator} result contract
	 * @throws NullPointerException
	 *             if any of the comparing object is null or any file name is
	 *             null
	 */
	@Override
	public int compare(Path path1, Path path2) {
		String fileName1 = path1.getFileName().toString();
		String fileName2 = path2.getFileName().toString();

		return caseSensitive ? fileName1.compareTo(fileName2) : fileName1
				.compareToIgnoreCase(fileName2);
	}
}
