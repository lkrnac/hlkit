package net.lkrnac.hlkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.lkrnac.patere.Patere;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link HardLinksHandler}.
 * 
 * @author sitko
 * 
 */
public class HardLinksHandlerTest {
	private static final String TEST_DIRECTORY = "test - album";
	private static final String DIR_NAME_SOURCE_DIRECTORY = "=[full]";
	private static final String DIR_NAME_TEMP = "temp";

	private String resourcesPath;

	/**
	 * Indicates type copy of testing data into testing temporary directory.
	 * 
	 * @author sitko
	 */
	private enum ECopyIntoTempType {
		COPY_NORMAL, // normal copy of all files
		COPY_SOME_HARD_LINKS, // copy files, some normally, some as hard links
		COPY_ALL_HARD_LINKS // make hard links of all files
	}

	/**
	 * Deletes temporary directory after test.
	 * 
	 * @throws IOException
	 *             if I/O error occurs during deletion
	 */
	@AfterMethod
	public void tidyUp() throws IOException {
		File tempDir = new File(resourcesPath + File.separator + DIR_NAME_TEMP);
		FileUtils.deleteDirectory(tempDir);
	}

	/**
	 * Prepares testing data for test
	 * {@link HardLinksHandlerTest# testVerifyHardLinks(ECopyIntoTempType, boolean)}
	 * .
	 * 
	 * @return parameters for test
	 */
	@DataProvider
	public Object[][] testVerifyHardLinks() {
		return new Object[][] { new Object[] { ECopyIntoTempType.COPY_ALL_HARD_LINKS, true },
				new Object[] { ECopyIntoTempType.COPY_SOME_HARD_LINKS, false },
				new Object[] { ECopyIntoTempType.COPY_NORMAL, false }, };
	}

	/**
	 * Tests {@link HardLinksHandler#verifyHardLinks(File)}.
	 * <p>
	 * This test expects specific testing directory and file structure on the
	 * hard disk
	 * 
	 * @param copyType
	 *            type of the copying of testing data
	 * @param expectedStatus
	 *            expected node status
	 * @throws IOException
	 *             if some I/O error occurs
	 */
	@Test(dataProvider = "testVerifyHardLinks")
	public void testVerifyHardLinks(ECopyIntoTempType copyType, boolean expectedStatus)
			throws IOException {
		resourcesPath = new Patere().getResourcesPathForMethod();
		File targetDir = getTestingDir(TEST_DIRECTORY, false, false);
		HardLinksHandler testingObj = new HardLinksHandler(targetDir);
		File sourceDir = getTestingDir(TEST_DIRECTORY, true, true);

		copyDirForTesting(sourceDir, targetDir, copyType);

		// call testing method
		boolean result = testingObj.verifyHardLinks(sourceDir);

		// verify node status
		Assert.assertEquals(result, expectedStatus);
	}

	/**
	 * Verifies if directory exists and returns file object for it. If needed
	 * copy of directory is created
	 * 
	 * @param testingDirName
	 *            name of the testing directory
	 * @param isSource
	 *            flag indicating source or target directory
	 * @param copyToTemp
	 *            copy of directory into temporary is needed of the original in
	 *            temporary directory
	 * @return file object belonging to the testing directory
	 * @throws IOException
	 *             if I/O error occurs during copying into temporary directory
	 */
	private File getTestingDir(String testingDirName, boolean isSource, boolean copyToTemp)
			throws IOException {
		String directoryRelativePath = (isSource ? File.separator + DIR_NAME_SOURCE_DIRECTORY : "")
				+ File.separator + testingDirName;
		File testDir = new File(resourcesPath + directoryRelativePath);
		File tmpTestDir = new File(resourcesPath + File.separator + DIR_NAME_TEMP
				+ directoryRelativePath);

		if (copyToTemp) {
			if (!testDir.exists()) {
				Assert.fail("Directory for testing doesn't exist: " + testDir.getAbsolutePath());
			}
			FileUtils.copyDirectory(testDir, tmpTestDir);
		}
		testDir = tmpTestDir;
		return testDir;
	}

	/**
	 * Creates hard links of files from given source directory into destination
	 * directory. Ignores sub-directories.
	 * 
	 * @param sourceDir
	 *            source directory of the hard links
	 * @param destinationDir
	 *            destination directory of hard links
	 * @param copyType
	 *            type of copying
	 * @throws IOException
	 *             if I/O error occurs
	 */
	private void copyDirForTesting(File sourceDir, File destinationDir, ECopyIntoTempType copyType)
			throws IOException {
		if (!destinationDir.exists()) {
			destinationDir.mkdirs();
		}
		if (ECopyIntoTempType.COPY_NORMAL.equals(copyType)) {
			FileUtils.copyDirectory(sourceDir, destinationDir);
		} else {
			int idx = 0;
			for (File file : sourceDir.listFiles()) {
				Path destinationPath = Paths.get(destinationDir.getAbsolutePath() + File.separator
						+ file.getName());
				if (ECopyIntoTempType.COPY_SOME_HARD_LINKS.equals(copyType) && idx++ % 2 == 0) {
					Files.copy(file.toPath(), destinationPath);
				} else {
					Files.createLink(destinationPath, file.toPath());
				}
			}
		}
	}

	/**
	 * Prepares testing data for test
	 * {@link HardLinksHandlerTest# testBuildHardLinks(String, boolean)}.
	 * 
	 * @return parameters for test
	 */
	@DataProvider
	public Object[][] testBuildHardLinks() {
		// @formatter:off
		return new Object[][] { 
				new Object[] { "test - album - success1", true },
				new Object[] { "test - album - success2", true }, 
				new Object[] { "test - album - success3", true },
				new Object[] { "test - album - success4", true },
				new Object[] { "test - album - fail1", false },
				new Object[] { "test - album - fail2", false },
		};
		// @formatter:on
	}

	/**
	 * Tests success use case of
	 * {@link HardLinksHandler#buildHardLinks(File, DirectoryComparator)}.
	 * <p>
	 * This test expects specific testing directory and file structure on the
	 * hard disk
	 * 
	 * @param testingDirectoryName
	 *            directory name for testing
	 * @param expectedResult
	 *            expected result of testing method
	 * @throws IOException
	 *             if I/O error occurs during test
	 */
	@Test(dataProvider = "testBuildHardLinks")
	public void testBuildHardLinks(String testingDirectoryName, boolean expectedResult)
			throws IOException {
		resourcesPath = new Patere().getResourcesPathForMethod();
		File targetDir = getTestingDir(testingDirectoryName, false, true);
		HardLinksHandler testingObj = new HardLinksHandler(targetDir);
		File sourceDir = getTestingDir(testingDirectoryName, true, true);
		DirectoryComparator dirComparator = new DirectoryComparator();

		// call testing method
		boolean actualReault = testingObj.buildHardLinks(sourceDir, dirComparator);

		Assert.assertEquals(actualReault, expectedResult);
		Assert.assertEquals(testingObj.verifyHardLinks(sourceDir), expectedResult,
				"Hard links verification failed: ");
	}

}