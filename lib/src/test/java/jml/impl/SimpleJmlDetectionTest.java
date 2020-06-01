package jml.impl;

import jml.JmlComment;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.*;
import java.util.stream.Stream;

import static jml.JmlComment.*;
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

    @SuppressWarnings("unchecked")
    private <T> List<T> listOf(T... s) {
        return Arrays.asList(s);
    }

    @Test
    void testGetAnnotations() {
        assertEquals(new TreeSet<String>(), sjd.getAnnotationKeys("//@"));
        assertEquals(setOf("+KeY"), sjd.getAnnotationKeys("//+KeY@"));
        assertEquals(setOf("+KeY", "-ESC"), sjd.getAnnotationKeys("//+KeY-ESC@"));
    }

    @SuppressWarnings("unchecked")
    private <T> Set<T> setOf(T... s) {
        return new HashSet<>(Arrays.asList(s));
    }


    @TestFactory
    public Stream<DynamicTest> testGetType() {
        List<DynamicTest> tests = new ArrayList<>();
        tests.add(typeTest("/*@ pure helper */", JmlComment.TYPE_MODIFIER));
        tests.add(typeTest("//@ instance invariant true;", JmlComment.TYPE_CLASS_INVARIANT));
        tests.add(typeTest("/*@ public normal_behaviour ensures true; */", JmlComment.TYPE_METHOD_CONTRACT));
        tests.add(typeTest("//@ loop_invariant true;", JmlComment.TYPE_LOOP_INVARIANT));
        tests.add(typeTest("/*@ set a = 2; */", JmlComment.TYPE_GHOST_SET));
        return tests.stream();
    }

    private DynamicTest typeTest(String input, int expected) {
        return DynamicTest.dynamicTest(input, () -> assertEquals(expected, sjd.getType(input)));
    }

    @Test
    void isJmlComment() {
    }

    @Test
    void getAnnotationKeys() {
    }

    @Test
    void getType() {
    }

    @TestFactory
    Stream<DynamicTest> getAttachingTypeCompletelyDefined() {
        int[] types = new int[]{
                TYPE_GHOST_FIELD,
                TYPE_MODEL_FIELD,
                TYPE_MODEL_METHOD,
                TYPE_CLASS_INVARIANT,
                TYPE_LOOP_INVARIANT,
                TYPE_BLOCK_CONTRACT,
                TYPE_METHOD_CONTRACT,
                TYPE_MODIFIER,
                TYPE_GHOST_SET,
                TYPE_ASSUME,
                TYPE_ASSERT};

        SimpleJmlDetection detector = new SimpleJmlDetection();
        return Arrays.stream(types).mapToObj(it ->
                DynamicTest.dynamicTest("getAttachingTypeCompletelyDefined(" + it + ")",
                        () -> {
                            int act = detector.getAttachingType("", it);
                            assertTrue(act != AT_UNKNOWN);
                        }
                ));
    }
}