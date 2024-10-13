package org.example;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

public class MutationEngine {
    private Random random = new Random();

    public String mutate(String html) {
        // Случайно выбираем мутацию
        int mutationType = random.nextInt(4);
        switch (mutationType) {
            case 0:
                return deleteRandomElement(html);
            case 1:
                return changeTagName(html);
            case 2:
                return modifyAttribute(html);
            case 3:
                return insertRandomElement(html);
            default:
                return html;
        }
    }

    private String deleteRandomElement(String html) {
        String regex = "<[^>]+>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        List<String> tags = new ArrayList<>();
        while (matcher.find()) {
            tags.add(matcher.group());
        }
        if (tags.isEmpty()) return html;
        String tagToRemove = tags.get(random.nextInt(tags.size()));
        return html.replaceFirst(Pattern.quote(tagToRemove), "");
    }

    private String changeTagName(String html) {
        String regex = "<(/)?(\\w+)([^>]*)>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            String oldTag = matcher.group(2);
            String newTag = getRandomTag();
            return html.replaceFirst(oldTag, newTag);
        }
        return html;
    }

    private String modifyAttribute(String html) {
        String regex = "(\\w+)=\"([^\"]*)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            String attr = matcher.group(1);
            String newValue = generateRandomString(5);
            return html.replaceFirst(Pattern.quote(matcher.group()), attr + "=\"" + newValue + "\"");
        }
        return html;
    }

    private String insertRandomElement(String html) {
        String randomTag = "<" + getRandomTag() + ">" + generateRandomString(5) + "</" + getRandomTag() + ">";
        int index = random.nextInt(html.length());
        return html.substring(0, index) + randomTag + html.substring(index);
    }

    private String getRandomTag() {
        String[] tags = {
                "div", "span", "a", "img", "p", "h1", "h2", "h3", "ul", "li",
                "table", "tr", "td", "th", "form", "input", "button", "select", "option"
        };
        return tags[random.nextInt(tags.length)];
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}