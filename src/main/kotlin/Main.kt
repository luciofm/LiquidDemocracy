import com.luciofm.Democracy
import com.luciofm.FileReader
import java.lang.Exception

fun main (args: Array<String>) {
    val democracy = Democracy()
    try {
        val lines = FileReader("votes.txt").readFile()
        val (invalid, votes) = democracy.countVotes(lines)

        votes.forEach { (vote, count) ->
            println("$count $vote")
        }
        println("$invalid Invalid")
    } catch (ex: Exception) {
        ex.printStackTrace()
        println("Error reading file")
    }
}