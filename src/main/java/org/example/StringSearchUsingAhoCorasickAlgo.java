package org.example;

import java.util.*;

class AhoCorasick {

    private int MAXS = 500;
    private int MAXC = 26;
    private int[] out = new int[MAXS];
    private int[] f = new int[MAXS];
    private int[][] g = new int[MAXS][MAXC];

    private int buildMatchingMachine(String[] arr, int k) {
        Arrays.fill(out, 0);
        for (int i = 0; i < MAXS; i++) Arrays.fill(g[i], -1);

        int states = 1;
        for (int i = 0; i < k; ++i) {
            String word = arr[i];
            int currentState = 0;
            for (int j = 0; j < word.length(); ++j) {
                char ch = word.charAt(j);
                if (Character.isLowerCase(ch)) {
                    int idx = ch - 'a';
                    if (g[currentState][idx] == -1) g[currentState][idx] = states++;
                    currentState = g[currentState][idx];
                }
            }
            out[currentState] |= (1 << i);
        }

        for (int ch = 0; ch < MAXC; ++ch) if (g[0][ch] == -1) g[0][ch] = 0;

        Arrays.fill(f, -1);
        Queue<Integer> q = new LinkedList<>();

        for (int ch = 0; ch < MAXC; ++ch) {
            if (g[0][ch] != 0) {
                f[g[0][ch]] = 0;
                q.add(g[0][ch]);
            }
        }

        while (!q.isEmpty()) {
            int state = q.peek();
            q.remove();
            for (int ch = 0; ch < MAXC; ++ch) {
                if (g[state][ch] != -1) {
                    int failure = f[state];
                    while (g[failure][ch] == -1) failure = f[failure];
                    failure = g[failure][ch];
                    f[g[state][ch]] = failure;
                    out[g[state][ch]] |= out[failure];
                    q.add(g[state][ch]);
                }
            }
        }
        return states;
    }

    private int findNextState(int currentState, char nextInput) {
        int answer = currentState;
        if (Character.isLowerCase(nextInput)) {
            int ch = nextInput - 'a';
            while (g[answer][ch] == -1) answer = f[answer];
            answer = g[answer][ch];
        }
        return answer;
    }

    public List<String> searchWords(String[] arr, String text) {
        int k = arr.length;
        buildMatchingMachine(arr, k);
        int currentState = 0;
        List<String> matches = new ArrayList<>();
        Set<String> seenMatches = new HashSet<>();

        for (int i = 0; i < text.length(); ++i) {
            currentState = findNextState(currentState, text.charAt(i));
            if (out[currentState] == 0) continue;

            for (int j = 0; j < k; ++j) {
                if ((out[currentState] & (1 << j)) > 0) {
                    if (!arr[j].isEmpty() ) {
                        if (!seenMatches.contains(arr[j])){
                            if (i - arr[j].length() + 1 >= 0){
                                String match = "Word " + arr[j] + " appears from " + (i - arr[j].length() + 1) + " to " + i;
                                matches.add(match);
                                seenMatches.add(arr[j]);
                            }
                        }
                    }
                }
            }
        }
        return matches;
    }
}
