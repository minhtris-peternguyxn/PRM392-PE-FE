package com.example.prm392_pe_se183033_trinm_kotlin.utils

object StringUtils {
    /**
     * Remove Vietnamese diacritics (dấu) from a string
     * Example: "Phở" -> "Pho", "Hà Nội" -> "Ha Noi"
     */
    fun removeVietnameseDiacritics(text: String): String {
        val vietnameseChars = arrayOf(
            "à", "á", "ạ", "ả", "ã", "â", "ầ", "ấ", "ậ", "ẩ", "ẫ", "ă", "ằ", "ắ", "ặ", "ẳ", "ẵ",
            "è", "é", "ẹ", "ẻ", "ẽ", "ê", "ề", "ế", "ệ", "ể", "ễ",
            "ì", "í", "ị", "ỉ", "ĩ",
            "ò", "ó", "ọ", "ỏ", "õ", "ô", "ồ", "ố", "ộ", "ổ", "ỗ", "ơ", "ờ", "ớ", "ợ", "ở", "ỡ",
            "ù", "ú", "ụ", "ủ", "ũ", "ư", "ừ", "ứ", "ự", "ử", "ữ",
            "ỳ", "ý", "ỵ", "ỷ", "ỹ",
            "đ"
        )
        val replacementChars = arrayOf(
            "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a",
            "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e",
            "i", "i", "i", "i", "i",
            "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o",
            "u", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u",
            "y", "y", "y", "y", "y",
            "d"
        )
        
        var result = text
        for (i in vietnameseChars.indices) {
            result = result.replace(vietnameseChars[i], replacementChars[i], ignoreCase = true)
        }
        return result.lowercase()
    }
    
    /**
     * Check if text contains query (case-insensitive and diacritic-insensitive)
     */
    fun containsIgnoreCaseAndDiacritics(text: String, query: String): Boolean {
        val normalizedText = removeVietnameseDiacritics(text)
        val normalizedQuery = removeVietnameseDiacritics(query)
        return normalizedText.contains(normalizedQuery)
    }
}

