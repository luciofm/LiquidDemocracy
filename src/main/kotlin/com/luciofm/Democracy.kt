package com.luciofm

import java.security.InvalidParameterException

class Democracy {
    var debugEnabled = false

    fun countVotes(lines: List<String>): Pair<Int, Map<String, Int>> {
        val votes = parseVotes(lines)
        return processVotes(votes)
    }

    fun parseVotes(lines: List<String>): Map<String, Vote> {
        val votes = hashMapOf<String, Vote>()
        lines.forEach { line ->
            val (voter, what, who) = line.split(" ")
            if (voter.isEmpty() or what.isEmpty() or who.isEmpty()) {
                throw IllegalStateException("Invalid line format: $line")
            }
            val action = parseAction(what)
            val vote = Vote(voter.toLowerCase(), action, who.toLowerCase())
            votes.put(vote.voter, vote)?.let {
                throw IllegalStateException("duplicate voter: ${vote.voter}")
            }
        }

        return votes
    }

    private fun processVotes(map: Map<String, Vote>): Pair<Int, Map<String, Int>> {
        var invalid = 0
        val castedVotes = hashMapOf<String, Int>()

        map.forEach { (_, vote) ->
            try {
                val castedVote = if (vote.action == Action.PICK) {
                    // look for vote.voter vote, since it is a pick action...
                    findVote(vote.voter, "", vote.voter, map)
                } else {
                    // look for the delegate vote recursively
                    findVote(vote.voter, vote.voter, vote.who, map)
                }
                addVote(castedVote, castedVotes)
                debug { "${vote.voter} voted for $castedVote" }
            } catch (ex: Throwable) {
                invalid++
                debug { "Invalid vote: ${ex.message}" }
            }
        }

        return Pair(invalid, castedVotes)
    }

    // voter: keeps track who is the original voter
    // currentVoter is the current voter, for example "Paul delegate John", "John pick Ringo", on the second iteration
    // original will be Paul, and current will be john
    // selection: the vote to look for... "Paul delegate John" -> selection would be John
    private fun findVote(voter: String, currentVoter: String, selection: String, map: Map<String, Vote>): String {
        map[selection].let { vote ->
            if (voter == vote?.who) throw InvalidParameterException("voter and vote.who must be different: $voter, ${vote.who}")
            if (currentVoter == vote?.who) throw InvalidParameterException("currentVoter and vote.who must be different: $currentVoter, ${vote.who}")
            return when (vote?.action) {
                Action.PICK -> {
                    if (currentVoter == selection) {
                        throw InvalidParameterException("Invalid self vote")
                    }
                    vote.who
                }
                Action.DELEGATE -> findVote(voter, vote.voter, vote.who, map)
                null -> throw InvalidParameterException("Vote not found for: ${vote?.action}")
            }
        }
    }

    private fun addVote(what: String, castedVotes: HashMap<String, Int>) {
        val votes = (castedVotes[what] ?: 0) + 1
        castedVotes[what] = votes
    }

    fun parseAction(what: String): Action {
        return when(what.toLowerCase()) {
            "pick" -> Action.PICK
            "delegate" -> Action.DELEGATE
            else -> throw InvalidParameterException("expected values \"pick\" and \"delegate\": actual value: \"$what\"")
        }

    }

    private fun debug(message: () -> String) {
        if (debugEnabled) println(message())
    }
}

enum class Action {
    PICK, DELEGATE
}

data class Vote(
    val voter: String,
    val action: Action,
    val who: String
)