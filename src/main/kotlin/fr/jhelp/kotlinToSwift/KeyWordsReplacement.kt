package fr.jhelp.kotlinToSwift

import java.util.regex.Pattern

private val KEY_WORDS_REPLACEMENT = arrayOf(
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])it([^a-zA-Z0-9_]|\$)"), "$1\\$0$2"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])Unit([^a-zA-Z0-9_]|\$)"), "$1Void$2"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])this([^a-zA-Z0-9_]|\$)"), "$1self$2"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])null([^a-zA-Z0-9_]|\$)"), "$1nil$2"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])println([^a-zA-Z0-9_]|\$)"), "$1print$2"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])Boolean([^a-zA-Z0-9_]|\$)"), "$1Bool$2"),
    Pair(Pattern.compile("CommonList"), "Array"),
    Pair(Pattern.compile("CommonMap"), "Dictionary"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])do([^a-zA-Z0-9_]|\$)"), "$1repeat$2"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])and([^a-zA-Z0-9_]|\$)"), "$1&$2"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])or([^a-zA-Z0-9_]|\$)"), "$1|$2"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])xor([^a-zA-Z0-9_]|\$)"), "$1^$2"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])shl([^a-zA-Z0-9_]|\$)"), "$1<<$2"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])shr([^a-zA-Z0-9_]|\$)"), "$1>>$2"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])\\?:([^a-zA-Z0-9_]|\$)"), "$1??$2"),
    Pair(Pattern.compile("\\+\\+"), " += 1"),
    Pair(Pattern.compile("--"), " -= 1"),
    Pair(Pattern.compile("!!(\\s+)as(\\s+)"), "$1as!$2"),
    Pair(Pattern.compile("!!"), "!"),
    Pair(Pattern.compile("([^.]*)\\.\\.([^.]*)"), "$1...$2"),
    Pair(Pattern.compile("for\\s*\\(\\s*([a-zA-Z0-9_]+)\\s+in\\s+(.*)until(.*)step(.*)\\)"), "for $1 in stride(from: $2, to: $3, by: $4)"),
    Pair(Pattern.compile("for\\s*\\(\\s*([a-zA-Z0-9_]+)\\s+in\\s+(.*)downTo(.*)step\\s+(.*)\\)"), "for $1 in stride(from: $2, to: $3 - 1, by: -$4)"),
    Pair(Pattern.compile("(^|[^a-zA-Z0-9_])until([^a-zA-Z0-9_]|\$)"), "$1..<$2"),
    Pair(Pattern.compile("for\\s*\\(\\s*([a-zA-Z0-9_]+)\\s+in\\s+(.*)downTo(.*)\\)"), "for $1 in stride(from: $2, to: $3 - 1, by: -1)"),
    Pair(Pattern.compile("'(.)'"), "\"$1\""),
    Pair(Pattern.compile("'(\\\\.)'"), "\"$1\""),
    Pair(Pattern.compile("'\\\\u([0-9A-Fa-f]{4})'"), "\"\\\\u{$1}\""),
    Pair(Pattern.compile("([0-9]+)[lL]([^a-zA-Z0-9_\"]|\$)"), "Long($1)$2"),
    Pair(Pattern.compile("(0x[A-Fa-f0-9]+)[lL]([^a-zA-Z0-9_\"]|\$)"), "Long($1)$2"),
    Pair(Pattern.compile("\\(([0-9]+(?:\\.[0-9]+)?)[fF]([^a-zA-Z0-9_\"]|$)"), "(Float($1)$2"),
    Pair(Pattern.compile("([^a-zA-Z0-9_\"(][0-9]+(?:\\.[0-9]+)?)[fF]([^a-zA-Z0-9_\"]|$)"), " Float($1)$2"),
    Pair(Pattern.compile("\\((\\s*[a-zA-Z0-9_]+\\s+)!is(\\s+[a-zA-Z0-9_]+\\s*)\\)"), "!($1is$2)"),
    Pair(Pattern.compile("([a-zA-Z0-9_]+\\s+)!is(\\s+[a-zA-Z0-9_]+)"), "!($1is$2)"),
    Pair(Pattern.compile("return@[a-zA-Z0-9_]+"), "return")
)

fun keyWordReplacement(string: String): String
{
    var answer = string

    KEY_WORDS_REPLACEMENT.forEach { (pattern, replacement) ->
        answer = pattern.matcher(answer).replaceAll(replacement)
    }

    return answer
}
