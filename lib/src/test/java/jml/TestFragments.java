package jml;

import org.eclipse.jdt.core.dom.Block;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;


import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * @author Alexander Weigl
 * @version 1 (2/4/20)
 */
public class TestFragments {
    @TestFactory public List<DynamicTest> abc(){
        InputStream in = getClass().getResourceAsStream("/fragments.txt");
        Assertions.assertNotNull(in, "Could not find 'fragments.txt' via classpath");
        List<DynamicTest> list = new LinkedList<>();

        try {
            int counter = 0;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String tmp;
                String[] a = new String[]{"", "", ""+counter++};
                int cur = 0;
                while ((tmp = br.readLine()) != null) {
                    if (tmp.startsWith("#")) continue;
                    if (tmp.startsWith("<<<<<<<")) {
                        a[2] = tmp.substring(7);
                        if(a[2].trim().isEmpty()) a[2] = "" + (++counter);
                        a[0] = a[1] = "";
                        cur = 0;
                        continue;
                    }
                    if (tmp.startsWith("=======")) {
                        cur = 1;
                        continue;
                    }
                    if (tmp.startsWith(">>>>>>>")) {
                        list.add(dynamicTest(a[2], () -> test(a[0], a[1])));
                        continue;
                    }
                    a[cur] += tmp + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    static JmlProject p = JmlCore.createProject();

    private static void test(String actual, String expected) throws IOException {
        PartialAst<Block> res = p.compileStatements(actual);
        try (StringWriter sw = new StringWriter()) {
            p.dumpJson(sw, Stream.of(res.getResult()));
            Assertions.assertEquals(expected.trim(), sw.toString().trim());
        }
    }
}
