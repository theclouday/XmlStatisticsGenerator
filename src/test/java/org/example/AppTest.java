package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ScriptAppTest {

    @Test
    void inputVoidPath()
    {
        GettingAllFiles getAllFiles = new GettingAllFiles("D:\\Java Projects\\tesate\\statistik\\src\\main\\resources");
        String userPath = GettingAllFiles.getPath();
        assertNotNull(userPath, "Uncorrected Path");

    }


}
