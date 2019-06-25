package test.code.generation.v1_3;

import com.code.generation.v1_3.exception.FileException;
import com.code.generation.v1_3.util.for_test.organization.BaseTestFolderOrganization;
import com.code.generation.v1_3.util.for_test.organization.folder_processors.IFolderComparator;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

public class ForCleanTest {
    private static final String SOURCES = "sources";
    private static final String RESULTS = "results";
    private static final String TEMP = "temp";

    private static final String TEST_FOR_NORMAL_CLEAN_FOLDER_NAME = GeneralFolderProcessingTest.FOLDER_SAMPLES + "/tests_for_normal_clean";
    private static final String NORMAL_SOURCES_FOLDER_NAME = TEST_FOR_NORMAL_CLEAN_FOLDER_NAME + "/" + SOURCES;
    private static final String NORMAL_TEMP_FOLDER_NAME = TEST_FOR_NORMAL_CLEAN_FOLDER_NAME + "/" + TEMP;

    private static final String TEST_FOR_SPECIAL_CLEAN_FOLDER_NAME = GeneralFolderProcessingTest.FOLDER_SAMPLES + "/tests_for_special_clean";
    private static final String SPECIAL_SOURCES_FOLDER_NAME = TEST_FOR_SPECIAL_CLEAN_FOLDER_NAME + "/" + SOURCES;
    private static final String SPECIAL_TEMP_FOLDER_NAME = TEST_FOR_SPECIAL_CLEAN_FOLDER_NAME + "/" + TEMP;

    @BeforeAll
    public static void copySources() throws IOException {
        copySources(NORMAL_SOURCES_FOLDER_NAME, NORMAL_TEMP_FOLDER_NAME);
        copySources(SPECIAL_SOURCES_FOLDER_NAME, SPECIAL_TEMP_FOLDER_NAME);
    }

    private static void copySources(String sourcesFolderName, String tempFolderName) throws IOException {
        File sourcesFolder = new File(sourcesFolderName);
        File tempFolder = new File(tempFolderName);
        FileUtils.cleanDirectory(tempFolder);
        for (File folder : sourcesFolder.listFiles()) {
            FileUtils.copyDirectoryToDirectory(folder, tempFolder);
        }
    }

    @TestFactory
    public Collection<DynamicTest> test_normal_clean() {
        return createTests(NORMAL_TEMP_FOLDER_NAME, true);
    }

    @TestFactory
    public Collection<DynamicTest> test_special_clean() {
        return createTests(SPECIAL_TEMP_FOLDER_NAME, false);
    }

    private Collection<DynamicTest> createTests(String tempFolderName, boolean normalClean) {
        Collection<DynamicTest> result = new LinkedList<>();
        File tempFolder = new File(tempFolderName);
        for (File tempFolderContainingOneOrNoneFolderToTest : tempFolder.listFiles()) {
            File resultFolder = new File(tempFolderContainingOneOrNoneFolderToTest.getAbsolutePath().replaceFirst(TEMP, RESULTS));
            File[] files = tempFolderContainingOneOrNoneFolderToTest.listFiles();
            if (files == null || files.length == 0) {
                addTestForNonExistingFolder(result, resultFolder, tempFolderContainingOneOrNoneFolderToTest, normalClean);
                continue;
            }
            if (files.length > 1) {
                throw new IllegalStateException();
            }
            addNormalTest(result, resultFolder, tempFolderContainingOneOrNoneFolderToTest, normalClean);
        }
        return result;
    }

    public void addTestForNonExistingFolder(Collection<DynamicTest> dynamicTests, File resultFolder, File tempFolderContainingOneFolderToTest, boolean normalClean) {
        dynamicTests.add(DynamicTest.dynamicTest("test_not_existing_folder_case", () -> {
            File nonExistingFile = new File(tempFolderContainingOneFolderToTest.getAbsolutePath() + "no_existing_file_like_this");
            if (nonExistingFile.exists()) {
                throw new FileException(nonExistingFile, " exists");
            }
            if (normalClean) {
                BaseTestFolderOrganization.cleanEmptyFolders(nonExistingFile);
            } else {
                BaseTestFolderOrganization.cleanAllButNotSourceOrResultFolder(nonExistingFile);
            }
            assert compare(resultFolder, tempFolderContainingOneFolderToTest);
        }));
    }

    public void addNormalTest(Collection<DynamicTest> dynamicTests, File resultFolder, File tempFolderContainingOneFolderToTest, boolean normalClean) {
        dynamicTests.add(DynamicTest.dynamicTest("test_" + tempFolderContainingOneFolderToTest.getAbsolutePath(), () -> {
            File aFolder = tempFolderContainingOneFolderToTest.listFiles()[0];
            if (normalClean) {
                BaseTestFolderOrganization.cleanEmptyFolders(aFolder);
            } else {
                BaseTestFolderOrganization.cleanAllButNotSourceOrResultFolder(aFolder);
            }
            assert compare(resultFolder, tempFolderContainingOneFolderToTest);
        }));
    }

    public boolean compare(File folder1, File folder2) {
        return IFolderComparator.instance(folder1, folder2).compare();
    }
}
