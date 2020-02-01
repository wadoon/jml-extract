package jml.impl;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alexander Weigl
 * @version 1 (2/1/20)
 */
class SimpleJmlDetectionTest {
    SimpleJmlDetection sjd = new SimpleJmlDetection();

    @TestFactory
    Stream<DynamicTest> testIsJmlCommentPositive() {
        var positive = List.of("//@",
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
            assertTrue(sjd.isJmlComment(it));
        });
    }

    @Test
    void testGetAnnotations() {
        assertEquals(new TreeSet<String>(), sjd.getAnnotationKeys("//@"));
        assertEquals(Set.of("+KeY"), sjd.getAnnotationKeys("//+KeY@"));
        assertEquals(Set.of("+KeY", "-ESC"), sjd.getAnnotationKeys("//+KeY-ESC@"));
    }
}