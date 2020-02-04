package jml;

import org.eclipse.jdt.core.dom.Block;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;


import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * @author Alexander Weigl
 * @version 1 (2/4/20)
 */
public class TestFragments {
    @TestFactory
    public List<DynamicTest> testFragments() throws IOException {
        InputStream in = TestFragments.class.getResourceAsStream("fragments.txt");
        Assumptions.assumeTrue(in != null);
        List<DynamicTest> list = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String tmp;
            String[] a = new String[]{"", "", ""};
            int cur = 0;
            while ((tmp = br.readLine()) != null) {
                if (tmp.startsWith("#")) continue;
                if (tmp.startsWith("<<<<<<<")) {
                    a[2] = tmp.substring(7);
                    a[0] = a[1] = "";
                    cur = 0;
                }
                if (tmp.startsWith("=======")) {
                    cur = 1;
                }
                if (tmp.startsWith(">>>>>>>")) {
                    list.add(dynamicTest(a[2], () -> test(a[0], a[1])));
                }
                a[cur] += tmp + "\n";
            }
        }
        return list;
    }

    static JmlProject p = JmlCore.createProject();

    private static void test(String actual, String expected) throws IOException {
        PartialAst<Block> res = p.compileStatements(actual);
        try (StringWriter sw = new StringWriter()) {
            p.dumpJson(sw, Stream.of(res.getResult()));
            Assertions.assertEquals(expected.trim(), actual.trim());
        }
    }
}
