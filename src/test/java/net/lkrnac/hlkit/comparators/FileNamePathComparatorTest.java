package net.lkrnac.hlkit.comparators;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FileNamePathComparator}.
 * 
 * @author lubos krnac
 * 
 */
public class FileNamePathComparatorTest {
	private static final String SEP = FileSystems.getDefault().getSeparator();

	/**
	 * Data provider for test
	 * {@link FileNamePathComparatorTest#testCompareSuccess(int, String, String, FileNamePathComparator, TestCompareResult)}
	 * .
	 * 
	 * NOPMD: Test cases are better readable with literals than with constants
	 * 
	 * @return test cases
	 */
	@SuppressWarnings("PMD.AvoidDuplicateLiterals")
	@DataProvider
	public final Object[][] testCompareSuccess() {
		String fileInDir1 = "dir1" + SEP + "name";
		String fileInDir2 = "dir2" + SEP + "name";

		//@formatter:off
		//SUPPRESS CHECKSTYLE MagicNumber 50 Numbers are used to enhance readability of test cases 
		return new Object [][]{
			//case sensitive, similar name 
			{ 1, "name", "name",  new FileNamePathComparator(), TestCompareResult.ZERO },
			{ 2, "Name", "Name",  new FileNamePathComparator(), TestCompareResult.ZERO },
			{ 3, "name", "Name",  new FileNamePathComparator(), TestCompareResult.LESS },
 			{ 4, "Name", "name",  new FileNamePathComparator(), TestCompareResult.GREATER },
			{ 5, "name", "nAme",  new FileNamePathComparator(), TestCompareResult.LESS },
			{ 6, "nAme", "name",  new FileNamePathComparator(), TestCompareResult.GREATER },
			{10, "name", "name",  new FileNamePathComparator(true), TestCompareResult.ZERO },
			{11, "Name", "Name",  new FileNamePathComparator(true), TestCompareResult.ZERO },
			{12, "name", "Name",  new FileNamePathComparator(true), TestCompareResult.LESS },
 			{13, "Name", "name",  new FileNamePathComparator(true), TestCompareResult.GREATER },
			{14, "name", "nAme",  new FileNamePathComparator(true), TestCompareResult.LESS },
			{16, "nAme", "name",  new FileNamePathComparator(true), TestCompareResult.GREATER },
			
			//case insensitive, similar name
			{20, "name", "name",  new FileNamePathComparator(false), TestCompareResult.ZERO },
			{21, "Name", "Name",  new FileNamePathComparator(false), TestCompareResult.ZERO },
			{22, "name", "Name",  new FileNamePathComparator(false), TestCompareResult.ZERO },
 			{23, "Name", "name",  new FileNamePathComparator(false), TestCompareResult.ZERO },
			{24, "name", "nAme",  new FileNamePathComparator(false), TestCompareResult.ZERO },
			{26, "nAme", "name",  new FileNamePathComparator(false), TestCompareResult.ZERO },
			
			//different names
			{30, "name", "names",  new FileNamePathComparator(), TestCompareResult.GREATER },
			{31, "names", "name",  new FileNamePathComparator(), TestCompareResult.LESS },
			{32, "name", "names",  new FileNamePathComparator(true), TestCompareResult.GREATER },
			{33, "names", "name",  new FileNamePathComparator(true), TestCompareResult.LESS },
			{34, "name", "names",  new FileNamePathComparator(false), TestCompareResult.GREATER },
			{35, "names", "name",  new FileNamePathComparator(false), TestCompareResult.LESS },
			
			//different paths 
			{40, fileInDir1, fileInDir2,  new FileNamePathComparator(), TestCompareResult.ZERO },
			{41, fileInDir1, fileInDir2,  new FileNamePathComparator(), TestCompareResult.ZERO },
			{42, fileInDir1, fileInDir2,  new FileNamePathComparator(true), TestCompareResult.ZERO },
			{43, fileInDir1, fileInDir2,  new FileNamePathComparator(true), TestCompareResult.ZERO },
			{44, fileInDir1, fileInDir2,  new FileNamePathComparator(false), TestCompareResult.ZERO },
			{45, fileInDir1, fileInDir2,  new FileNamePathComparator(false), TestCompareResult.ZERO },
		};
		//@formatter:on
	}

	/**
	 * Tests success test cases for method
	 * {@link FileNamePathComparator#compare(Path, Path)}.
	 * 
	 * @param testCaseId
	 *            ID of the test case to help easily find test case
	 * @param fileName1
	 *            testing file name 1
	 * @param fileName2
	 *            testing file name 2
	 * @param comparator
	 *            testing object
	 * @param expectedResult
	 *            comparison result verification helper
	 */
	@Test(dataProvider = "testCompareSuccess")
	public void testCompareSuccess(int testCaseId, String fileName1, String fileName2,
			FileNamePathComparator comparator, TestCompareResult expectedResult) {
		//GIVEN:
		Path path1 = Paths.get(fileName1);
		Path path2 = Paths.get(fileName2);

		//WHEN:
		Integer actualResult = comparator.compare(path1, path2);

		//THEN:
		Assert.assertTrue(expectedResult.verify(actualResult));
	}

	/**
	 * Data provider for test
	 * {@link FileNamePathComparatorTest#testCompareFailFileNameNull(String, String, FileNamePathComparator)}
	 * .
	 * 
	 * @return test cases
	 */
	@DataProvider
	public final Object[][] testCompareFailFileNameNull() {
		//@formatter:off
		return new Object [][]{
			{ null, "name", new FileNamePathComparator() },
			{ null, "name", new FileNamePathComparator(true) },
			{ null, "name", new FileNamePathComparator(false) },
			{ "name", null, new FileNamePathComparator() },
			{ "name", null, new FileNamePathComparator(true) },
			{ "name", null, new FileNamePathComparator(false) },
			{ null, null, new FileNamePathComparator() },
			{ null, null, new FileNamePathComparator(true) },
			{ null, null, new FileNamePathComparator(false) },
		};
		//@formatter:on
	}

	/**
	 * Tests fail test cases for method
	 * {@link FileNamePathComparator#compare(Path, Path)}. Tests failure when
	 * file names are null.
	 * 
	 * @param fileName1
	 *            testing file name 1
	 * @param fileName2
	 *            testing file name 2
	 * @param comparator
	 *            testing object
	 */
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	@Test(dataProvider = "testCompareFailFileNameNull", expectedExceptions = NullPointerException.class)
	public void testCompareFailFileNameNull(String fileName1, String fileName2,
			FileNamePathComparator comparator) {
		//GIVEN:
		Path path1 = Paths.get(fileName1);
		Path path2 = Paths.get(fileName2);

		//WHEN:
		comparator.compare(path1, path2);

		//THEN: see expected exception as method annotation parameter
	}

	/**
	 * Data provider for test
	 * {@link FileNamePathComparatorTest#testCompareFailPathNull(Path, Path, FileNamePathComparator)}
	 * .
	 * 
	 * @return test cases
	 */
	@DataProvider
	public final Object[][] testCompareFailPathNull() {
		//@formatter:off
		return new Object [][]{
			{ null, Paths.get("name"), new FileNamePathComparator() },
			{ null, Paths.get("name"), new FileNamePathComparator(true) },
			{ null, Paths.get("name"), new FileNamePathComparator(false) },
			{ Paths.get("name"), null, new FileNamePathComparator() },
			{ Paths.get("name"), null, new FileNamePathComparator(true) },
			{ Paths.get("name"), null, new FileNamePathComparator(false) },
			{ null, null, new FileNamePathComparator() },
			{ null, null, new FileNamePathComparator(true) },
			{ null, null, new FileNamePathComparator(false) },
		};
		//@formatter:on
	}

	/**
	 * Tests fail test cases for method
	 * {@link FileNamePathComparator#compare(Path, Path)}. Tests failure when
	 * path objects are null.
	 * 
	 * @param path1
	 *            testing path 1
	 * @param path2
	 *            testing path 2
	 * @param comparator
	 *            testing object
	 */
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	@Test(dataProvider = "testCompareFailPathNull", expectedExceptions = NullPointerException.class)
	public void testCompareFailPathNull(Path path1, Path path2, FileNamePathComparator comparator) {
		//WHEN:
		comparator.compare(path1, path2);

		//THEN: see expected exception as method annotation parameter
	}
}
