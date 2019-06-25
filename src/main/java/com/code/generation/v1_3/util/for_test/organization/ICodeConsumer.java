package com.code.generation.v1_3.util.for_test.organization;

import java.io.File;

public interface ICodeConsumer {
    void accept(File sourceFolder, File newTargetFolder, File resultFolder);
}
