package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class AhoCorasickTest {
    @Test
    void testEmptyWordArray() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {};
        String text = "test";
        assertTrue(ac.searchWords(words, text).isEmpty());
    }

    @Test
    void testEmptyText() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"test"};
        String text = "";
        assertTrue(ac.searchWords(words, text).isEmpty());
    }
    @Test
    void testEmptyWordInArray() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"test", ""};
        String text = "test";
        List<String> expected = List.of("Word test appears from 0 to 3");
        assertEquals(expected, ac.searchWords(words, text));
    }

    @Test
    void testSpecialCharactersInWords() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"test@"};
        String text = "test@";
        List<String> expected = List.of("Word test@ appears from 0 to 4");
        assertEquals(expected, ac.searchWords(words, text));
    }

    @Test
    void testDigitsInWords() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"test1"};
        String text = "test1";
        List<String> expected = List.of("Word test1 appears from 0 to 4");
        assertEquals(expected, ac.searchWords(words, text));
    }


    @Test
    void testWordFoundAtBeginning() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"test"};
        String text = "test example";
        List<String> expected = List.of("Word test appears from 0 to 3");
        assertEquals(expected, ac.searchWords(words, text));
    }

    @Test
    void testWordFoundInMiddle() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"test"};
        String text = "example test example";
        List<String> expected = List.of("Word test appears from 8 to 11");
        assertEquals(expected, ac.searchWords(words, text));
    }

    @Test
    void testWordFoundAtEnd() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"test"};
        String text = "example test";
        List<String> expected = List.of("Word test appears from 8 to 11");
        assertEquals(expected, ac.searchWords(words, text));
    }

    @Test
    void testMultipleWordsFound() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"test", "example"};
        String text = "example test";
        List<String> expected = List.of(
                "Word example appears from 0 to 6",
                "Word test appears from 8 to 11"
        );
        assertEquals(expected, ac.searchWords(words, text));
    }

    @Test
    void testCommonPrefix() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"test", "testing"};
        String text = "testing example";
        List<String> expected = List.of("Word test appears from 0 to 3", "Word testing appears from 0 to 6");
        assertEquals(expected, ac.searchWords(words, text));
    }

    @Test
    void testCommonSuffix() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"test", "best"};
        String text = "best example";
        List<String> expected = List.of("Word best appears from 0 to 3");
        assertEquals(expected, ac.searchWords(words, text));
    }




    @Test
    void testSingleWordMatch() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"test"};
        String text = "test";
        List<String> expected = List.of("Word test appears from 0 to 3");
        assertEquals(expected, ac.searchWords(words, text));
    }

    @Test
    void testSingleWordNoMatch() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"test"};
        String text = "abc";
        assertTrue(ac.searchWords(words, text).isEmpty());
    }

    @Test
    void testMultipleWordsMatch() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"he", "she", "his", "hers"};
        String text = "ushers";
        List<String> expected = List.of(
                "Word he appears from 2 to 3",
                "Word she appears from 1 to 3",
                "Word hers appears from 2 to 5"
        );
        assertEquals(expected, ac.searchWords(words, text));
    }

    @Test
    void testTransitionToFailState() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"ab", "bc"};
        String text = "abc";
        List<String> expected = List.of(
                "Word ab appears from 0 to 1",
                "Word bc appears from 1 to 2"
        );
        assertEquals(expected, ac.searchWords(words, text));
    }



    @Test
    void testNonLowercaseCharacters() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"Test", "case"};
        String text = "This is a Test case.";
        words = Arrays.stream(words).map(String::toLowerCase).toArray(String[]::new);
        text = text.toLowerCase().replaceAll("[^a-z]", "");
        List<String> expected = List.of(
                "Word test appears from 7 to 10",
                "Word case appears from 11 to 14"
        );
        assertEquals(expected, ac.searchWords(words, text));
    }


    @Test
    void testRootNodeFailure() {
        AhoCorasick ac = new AhoCorasick();
        String[] words = {"a"};
        String text = "ba";
        List<String> expected = List.of("Word a appears from 1 to 1");
        assertEquals(expected, ac.searchWords(words, text));
    }


}
