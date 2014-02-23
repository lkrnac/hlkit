package net.lkrnac.hlkit;

import java.io.File;
import java.io.IOException;

import net.lkrnac.patere.Patere;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link DirectoryComparatorTest}.
 * 
 * @author sitko
 * 
 */
public class DirectoryComparatorTest {
	private static final String DIR_SOURCE = "source";
	private static final String DIR_TARGET = "target";

	/**
	 * Data provider for test
	 * {@link DirectoryComparatorTest# testCompareDirectories(String, DirectoryComparisonResult)}
	 * .
	 * 
	 * @return parameters for various test runs
	 */
	@DataProvider
	public Object[][] testCompareDirectories() {
		//@formatter:off
		return new Object[][] { 
				new Object[] { "success1", DirectoryComparisonResult.EQUAL },
				new Object[] { "success2", DirectoryComparisonResult.MISSING_MEDIA_FILES_IN_TARGET }, 
				new Object[] { "fail1", DirectoryComparisonResult.MISSING_MEDIA_FILES_IN_SOURCE },
				new Object[] { "fail2", DirectoryComparisonResult.DIFFERENT_FILES }, 
				new Object[] { "fail3", DirectoryComparisonResult.DIFFERENT_FILES },
				new Object[] { "fail4", DirectoryComparisonResult.DIFFERENT_FILES }, 
				new Object[] { "fail5", DirectoryComparisonResult.DIFFERENT_FILES },
				new Object[] { "fail6", DirectoryComparisonResult.DIFFERENT_FILES }, 
				new Object[] { "fail7", DirectoryComparisonResult.DIFFERENT_FILES }, 
		};
		//@formatter:on
	}

	/**
	 * Unit test for method
	 * {@link DirectoryComparator#compareDirectories(File, File)}.
	 * <p>
	 * This test expects specific testing directory and file structure on the
	 * hard disk
	 * 
	 * @param testingDataLocation
	 *            name of the directory on HDD where are testing data stored
	 * @param expectedResult
	 *            expected status for the test
	 * @throws IOException
	 *             if I/O error occurs
	 */
	@Test(dataProvider = "testCompareDirectories")
	public void testCompareDirectories(String testingDataLocation,
			DirectoryComparisonResult expectedResult) throws IOException {
		String resourcesPath = new Patere().getResourcesPathForMethod() + File.separator
				+ testingDataLocation + File.separator;

		File sourceFile = getTestingDir(resourcesPath, DIR_SOURCE);
		File targetFile = getTestingDir(resourcesPath, DIR_TARGET);

		DirectoryComparisonResult result = new DirectoryComparator().compareDirectories(sourceFile,
				targetFile);

		Assert.assertEquals(result, expectedResult);
	}

	/**
	 * Creates File object of testing directory and verifies its existence.
	 * 
	 * @param resourcesPath
	 *            path to the testing directory
	 * @param dirName
	 *            testing directory name
	 * @return file object of testing directory
	 */
	private File getTestingDir(String resourcesPath, String dirName) {
		File file = new File(resourcesPath + dirName);
		if (!file.exists()) {
			Assert.fail("Testing directory not found: " + file.getAbsolutePath());
		}
		return file;
	}
}
