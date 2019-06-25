package test.code.generation.v1_3;

import com.code.generation.v1_3.util.FileUtil;
import com.code.generation.v1_3.util.for_test.organization.folder_processors.IFolderComparator;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralFolderProcessingTest {
    public static String FOLDER_SAMPLES = "folder_examples";
    private static String TEST_FOR_COMPARE_FOLDER_NAME = FOLDER_SAMPLES + "/tests_for_compare";
    private static String SAME_FILES_FOLDER_NAME = TEST_FOR_COMPARE_FOLDER_NAME + "/same_files";
    private static String SAME_FOLDER_FOLDER_NAME = TEST_FOR_COMPARE_FOLDER_NAME + "/same_folders";
    private static String NON_SAME_FILES_FOLDER_NAME = TEST_FOR_COMPARE_FOLDER_NAME + "/non_same_files";
    private static String NON_SAME_FOLDERS_FOLDER_NAME = TEST_FOR_COMPARE_FOLDER_NAME + "/non_same_folders";

    @Test
    public void test_compare_same_files(){
        testCompare(new File(SAME_FILES_FOLDER_NAME), true, true);
    }

    @Test
    public void test_compare_same_folders(){
        testCompare(new File(SAME_FOLDER_FOLDER_NAME), false, true);
    }

    @Test
    public void test_compare_non_same_files(){
        testCompare(new File(NON_SAME_FILES_FOLDER_NAME), true, false);
    }

    @Test
    public void test_compare_non_same_folders(){
        testCompare(new File(NON_SAME_FOLDERS_FOLDER_NAME), false, false);
    }

    public void testCompare(File baseTestFolder, boolean comparePureFiles, boolean wantSame){
        List<File> failedFolders = new LinkedList<>();
        for (File containingTwoSubEls : baseTestFolder.listFiles()) {
            File[] subEls = assertStructure(containingTwoSubEls, comparePureFiles);
            boolean isSame = IFolderComparator.instance(subEls[0], subEls[1]).compare();
            if(isSame != wantSame){
                failedFolders.add(containingTwoSubEls);
            }
        }
        if(!failedFolders.isEmpty()){
            throw new IllegalStateException((wantSame ? "" : "non ") + "same " + (comparePureFiles ? "files" : "folder") + " : " + String.join(" , ", failedFolders.stream().map(file -> file.getName()).collect(Collectors.toList())));
        }
    }

    private File[] assertStructure(File containingTwoFilesOrFolder, boolean wantFiles){
        File[] subEls = containingTwoFilesOrFolder.listFiles();
        if(containingTwoFilesOrFolder.isFile() || FileUtil.getSubEls(containingTwoFilesOrFolder).size() != 2){
            throw new IllegalStateException("wrong structure");
        }
        assertKind(subEls[0], wantFiles);
        assertKind(subEls[1], wantFiles);
        return subEls;
    }

    private void assertKind(File subEl, boolean wantFile) {
        if(subEl.isFile() != wantFile){
            throw new IllegalStateException("wrong structure");
        }
    }
}
