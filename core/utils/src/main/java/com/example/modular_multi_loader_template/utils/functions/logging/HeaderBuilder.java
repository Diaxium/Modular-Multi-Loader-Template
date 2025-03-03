package com.example.modular_multi_loader_template.utils.functions.logging;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility for creating custom header strings using a builder–style API.
 *
 * <p>This utility allows you to specify a header title, description lines, and additional details.
 * The header is formatted with a border and comment markers for clarity.
 * Example usage:
 * <pre>{@code
 * String header = HeaderBuilder.newBuilder()
 *         .setTotalWidth(150)
 *         .setTitle("My Application Header")
 *         .setDescription("This is a sample header.\nIt can span multiple lines.")
 *         .addDetail("Version", "1.0.0")
 *         .addDetail("Java", "17")
 *         .build();
 * }</pre>
 * </p>
 */
public class HeaderBuilder {

    public static class Builder {
        // Default configuration values.
        private int totalWidth = 150;
        private String borderChar = "/";
        private String commentStart = "/*";
        private String commentEnd = "*/";
        private String title = "";
        private final List<String> descriptionLines = new ArrayList<>();
        private final Map<String, String> details = new LinkedHashMap<>();

        /**
         * Sets the total width of the header (in characters).
         *
         * @param totalWidth the total width.
         * @return the builder instance.
         */
        public Builder setTotalWidth(int totalWidth) {
            this.totalWidth = totalWidth;
            return this;
        }

        /**
         * Sets the character used to build the borderline.
         *
         * @param borderChar the border character.
         * @return the builder instance.
         */
        public Builder setBorderChar(String borderChar) {
            this.borderChar = borderChar;
            return this;
        }

        /**
         * Sets the comment markers to use for formatting each line.
         *
         * @param commentStart the starting marker.
         * @param commentEnd   the ending marker.
         * @return the builder instance.
         */
        public Builder setCommentMarkers(String commentStart, String commentEnd) {
            this.commentStart = commentStart;
            this.commentEnd = commentEnd;
            return this;
        }

        /**
         * Sets the header title.
         *
         * @param title the title text.
         * @return the builder instance.
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Adds a single line to the header description.
         *
         * @param line the description line.
         * @return the builder instance.
         */
        public Builder addDescriptionLine(String line) {
            if (line != null) {
                this.descriptionLines.add(line);
            }
            return this;
        }

        /**
         * Sets a multi–line description for the header.
         * Lines are split by newline characters.
         *
         * @param description the full description.
         * @return the builder instance.
         */
        public Builder setDescription(String description) {
            this.descriptionLines.clear();
            if (description != null && !description.trim().isEmpty()) {
                String[] lines = description.split("\n");
                for (String line : lines) {
                    this.descriptionLines.add(line.trim());
                }
            }
            return this;
        }

        /**
         * Adds a key–value pair detail to the header.
         *
         * @param key   the detail key.
         * @param value the detail value.
         * @return the builder instance.
         */
        public Builder addDetail(String key, String value) {
            this.details.put(key, value);
            return this;
        }

        // Helper: Create a border line based on the total width.
        private String createBorderLine() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < totalWidth; i++) {
                sb.append(borderChar);
            }
            return sb.toString();
        }

        // Helper: Centers text within a specified width.
        private String centerText(String text, int width) {
            if (text.length() >= width) {
                return text;
            }
            int leftPadding = (width - text.length()) / 2;
            int rightPadding = width - text.length() - leftPadding;
            return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
        }

        // Helper: Formats a line with comment markers and pads it to the full width.
        private String formatCommentLine(String content) {
            int contentWidth = totalWidth - commentStart.length() - commentEnd.length();
            if (content.length() > contentWidth) {
                content = content.substring(0, contentWidth);
            }
            int padding = contentWidth - content.length();
            return commentStart + content + " ".repeat(padding) + commentEnd;
        }

        /**
         * Builds and returns the custom header string.
         *
         * @return the formatted header.
         */
        public String build() {
            StringBuilder headerBuilder = new StringBuilder();
            String borderLine = createBorderLine();

            // Top border.
            headerBuilder.append(borderLine).append("\n");

            // Title (centered) if provided.
            if (!title.isEmpty()) {
                String centeredTitle = centerText(title, totalWidth - commentStart.length() - commentEnd.length());
                headerBuilder.append(formatCommentLine(centeredTitle)).append("\n");
                headerBuilder.append(borderLine).append("\n");
            }

            // Description lines.
            for (String line : descriptionLines) {
                headerBuilder.append(formatCommentLine(line)).append("\n");
            }
            if (!descriptionLines.isEmpty()) {
                headerBuilder.append(borderLine).append("\n");
            }

            // Key–Value details.
            for (Map.Entry<String, String> entry : details.entrySet()) {
                String detailLine = entry.getKey() + " : " + entry.getValue();
                headerBuilder.append(formatCommentLine(detailLine)).append("\n");
            }

            // Bottom border.
            headerBuilder.append(borderLine).append("\n");

            return headerBuilder.toString();
        }
    }

    /**
     * Creates a new builder instance for building a custom header.
     *
     * @return a new Builder.
     */
    public static Builder newBuilder() {
        return new Builder();
    }
}
