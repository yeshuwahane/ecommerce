package com.yeshuwahane.task.utils



fun cleanText(description: String): String {
    return description
        .replace("\\r", "") // Remove \r
        .replace("\\n", "") // Remove \n
        .replace("<\\/p>", "\n\n") // Replace paragraph tags with double newline
        .replace("<\\/li>", "\n") // Replace list items with newline
        .replace("<\\/ul>", "") // Remove closing unordered list tags
        .replace("<\\/ol>", "") // Remove closing ordered list tags
        .replace("<[^>]*>".toRegex(), "") // Remove any remaining HTML tags
        .trim()
}
