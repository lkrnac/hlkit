package net.lkrnac.hlkit.comparators;

import java.nio.file.Path;
import java.util.Comparator;

public class FileNamePathComparator implements Comparator<Path> {
	private boolean caseSensitive;

	public FileNamePathComparator() {
		this(true);
	}

	public FileNamePathComparator(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	@Override
	public int compare(Path path1, Path path2) {
		String fileName1 = path1.getFileName().toString();
		String fileName2 = path2.getFileName().toString();

		return caseSensitive ? fileName1.compareTo(fileName2) : fileName1
				.compareToIgnoreCase(fileName2);
	}
}
