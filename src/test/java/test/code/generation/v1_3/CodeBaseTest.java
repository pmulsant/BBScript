package test.code.generation.v1_3;

import com.code.generation.tests.RunResult;
import com.code.generation.tests.TestRunner;
import com.code.generation.v1_3.util.for_test.organization.BaseTestFolderOrganization;
import com.code.generation.v1_3.util.for_test.organization.code_folder_processor.ITriCodeFolderProcessor;
import com.code.generation.v1_3.util.for_test.organization.code_folder_processor.NotCompatibleFoldersException;
import com.code.generation.v1_3.util.for_test.organization.folder_processors.IFolderComparator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

public class CodeBaseTest {
    @BeforeAll
    public static void removeTempFolder() throws IOException {
        org.junit.Assume.assumeTrue(TestRunner.wantContinue());
        BaseTestFolderOrganization.removeTempFolders();
    }

    @TestFactory
    public Collection<DynamicTest> tests() throws NotCompatibleFoldersException {
        Collection<DynamicTest> result = new LinkedList<>();
        ITriCodeFolderProcessor codeFolderProcessor = BaseTestFolderOrganization.getCodeFolderProcessor();
        codeFolderProcessor.process((sourceFolder, resultFolder, originalResultFolder) -> {
            addDynamicTest(result, sourceFolder, resultFolder, originalResultFolder);
        });
        return result;
    }

    private void addDynamicTest(Collection<DynamicTest> dynamicTests, File sourceFolder, File resultFolder, File originalFolder){
        dynamicTests.add(DynamicTest.dynamicTest("test_" + sourceFolder.getPath(), () -> {
            RunResult runResult = TestRunner.run(sourceFolder, resultFolder);
            throwIfMsg(runResult);
            assert IFolderComparator.instance(originalFolder, resultFolder).compare();
            BaseTestFolderOrganization.removeFolder(resultFolder);
        }));
    }

    private void throwIfMsg(RunResult runResult) throws Exception {
        if(runResult.getMsg() != null){
            throw new Exception(runResult.getMsg());
        }
    }

    @AfterAll
    public static void cleanTempFolder(){
        BaseTestFolderOrganization.clean_tempFolder_AllButNotSourceOrResultFolder();
    }
}
