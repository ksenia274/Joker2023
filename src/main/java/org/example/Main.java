package org.example;

public class Main {
    public static void main(String[] args)
    {
        AhoCorasick ac = new AhoCorasick();
        String arr[] = { "he", "she", "hers", "his" };
        String text = "ahishers";

        ac.searchWords(arr, text);
    }
}