package jml.impl;

import jml.JmlComment;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexander Weigl
 * @version 1 (2/1/20)
 */
class SimpleJmlDetectionTest {
    SimpleJmlDetection sjd = new SimpleJmlDetection();

    @TestFactory
    Stream<DynamicTest> testIsJmlCommentPositive() {
        List<String> positive = listOf("//@",
                "/*@ */",
                "/*@ @*/",
                "   //@    ",
                "   //@",
                "//@ dfafdslkjsafjsaf ffsafsfdsfjafsdf; ",
                "/*+KeY@ */",
                "/*-KeY@ */",
                "/*-KeY+affd@ */",
                "/*-KeY+affd+afdsf@ */",
                "//-KeY+affd+afdsf@ fffdf");
        return DynamicTest.stream(positive.iterator(), it -> it, it -> {
            System.out.println(it);
            assertTrue(sjd.isJmlComment(it),
                    "ERROR: Input '" + it + "' is *not* classified as Jml comment.");
        });
    }


    @TestFactory
    Stream<DynamicTest> testIsJmlCommentNegative() {
        List<String> positive = listOf(
                "// @",
                "/* @ ",
                "/* fdsaf",
                "/** javadoc",
                "/*-Key",
                "//-KeY+affd+afdsf @ fffdf");

        return DynamicTest.stream(positive.iterator(), it -> it, it -> {
            System.out.println(it);
            assertFalse(sjd.isJmlComment(it), "ERROR: Input '" + it + "' is classified as Jml comment.");
        });
    }

    private <T> List<T> listOf(T... s) {
        return Arrays.asList(s);
    }

    @Test
    void testGetAnnotations() {
        assertEquals(new TreeSet<String>(), sjd.getAnnotationKeys("//@"));
        assertEquals(setOf("+KeY"), sjd.getAnnotationKeys("//+KeY@"));
        assertEquals(setOf("+KeY", "-ESC"), sjd.getAnnotationKeys("//+KeY-ESC@"));
    }

    private <T> Set<T> setOf(T... s) {
        return new HashSet<>(Arrays.asList(s));
    }


    @TestFactory
    public Stream<DynamicTest> testGetType() {
        List<DynamicTest> tests = new ArrayList<>();
        tests.add(typeTest("//@ instance invariant true;", JmlComment.Type.CLASS_INVARIANT));
        tests.add(typeTest("/*@ public normal_behaviour ensures true; */", JmlComment.Type.METHOD_CONTRACT));
        tests.add(typeTest("//@ loop_invariant true;", JmlComment.Type.LOOP_INVARIANT));
        tests.add(typeTest("/*@ pure helper */", JmlComment.Type.MODIFIER));
        tests.add(typeTest("/*@ set a = 2; */", JmlComment.Type.GHOST_SET));
        return tests.stream();
    }

    private DynamicTest typeTest(String input, JmlComment.Type expected) {
        return DynamicTest.dynamicTest(input, () -> assertEquals(expected, sjd.getType(input)));
    }
}