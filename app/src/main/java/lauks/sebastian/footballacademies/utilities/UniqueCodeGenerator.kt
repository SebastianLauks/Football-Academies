package lauks.sebastian.footballacademies.utilities

class UniqueCodeGenerator {


    companion object {
        fun generateHash(length: Int): String {
            val allowedChars = ('A'..'Z') + ('a'..'z')
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }
}