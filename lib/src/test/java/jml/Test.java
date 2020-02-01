package jml;

import java.io.FileWriter;
import java.io.IOException;

public class Test {
    @org.junit.jupiter.api.Test
    public void test() throws IOException {
        JmlCore.init();
        JmlProject p = JmlCore.createProject();
        p.setEnvironment("../example");
        var cus = p.compileAllIn("../example");
        cus.forEach(System.out::println);
        try (var out = new FileWriter("/tmp/blubber.json")) {
            p.dumpJson(out);
        }
    }
}