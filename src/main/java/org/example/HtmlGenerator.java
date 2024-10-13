package org.example;

import java.util.Random;

public class HtmlGenerator {
    private static final String[] TAGS = {
            "div", "span", "a", "img", "p", "h1", "h2", "h3", "ul", "li",
            "table", "tr", "td", "th", "form", "input", "button", "select", "option"
    };
    private static final String[] ATTRIBUTES = {
            "class", "id", "src", "href", "alt", "title", "style", "onclick", "onmouseover",
            "data-*", "aria-*", "role", "type", "value", "placeholder", "disabled", "readonly"
    };
    private static final String[] VALUES = {
            "main", "header", "footer", "content", "image", "link", "button", "input",
            "select", "option", "table", "row", "cell", "form", "text", "password", "checkbox"
    };

    private Random random = new Random();

    public String generateHtml(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\n");
        generateElement(sb, depth);
        sb.append("</html>\n");
        return sb.toString();
    }

    private void generateElement(StringBuilder sb, int depth) {
        if (depth == 0) return;

        String tag = TAGS[random.nextInt(TAGS.length)];
        sb.append("<").append(tag);

        // Add attributes
        int numAttributes = random.nextInt(5);
        for (int i = 0; i < numAttributes; i++) {
            String attr = ATTRIBUTES[random.nextInt(ATTRIBUTES.length)];
            String value = generateAttributeValue();
            // Check for tag-specific attributes
            if (isAttributeApplicable(tag, attr)) {
                sb.append(" ").append(attr).append("=\"").append(value).append("\"");
            }
        }

        // Self-closing tags
        if (tag.equals("img") || tag.equals("input")) {
            sb.append(">\n");
        } else {
            sb.append(">");

            // Add content
            if (random.nextBoolean()) {
                sb.append(generateContent());
            }

            // Add child elements
            int numChildren = random.nextInt(4);
            for (int i = 0; i < numChildren; i++) {
                generateElement(sb, depth - 1);
            }

            sb.append("</").append(tag).append(">\n");
        }
    }

    private String generateAttributeValue() {
        if (random.nextDouble() < 0.1) {
            // Generate invalid or unexpected value
            return generateRandomString(10);
        } else {

                String value = VALUES[random.nextInt(VALUES.length)];
            // Adjust values for specific attributes
            switch (value) {
                case "href":
                case "src":
                    return "http://example.com";
                case "type":
                    return random.nextBoolean() ? "text" : "password"; // Example for input types
                case "disabled":
                case "readonly":
                    return ""; // Empty value for boolean attributes
                default:
                    return value;
            }
        }
    }

    private String generateContent() {
        int numWords = random.nextInt(10) + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numWords; i++) {
            sb.append(generateRandomString(5)).append(" ");
        }
        return sb.toString().replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot;");
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private boolean isAttributeApplicable(String tag, String attribute) {
        switch (attribute) {
            case "href":
            case "src":
                return tag.equals("a") || tag.equals("link") || tag.equals("img");
            case "type":
                return tag.equals("input") || tag.equals("button") || tag.equals("select");
            default:
                return true;
        }
    }
}