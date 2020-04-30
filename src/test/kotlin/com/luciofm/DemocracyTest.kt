package com.luciofm

import org.junit.Test
import java.security.InvalidParameterException

class DemocracyTest {
    private val democracy = Democracy()

    @Test(expected = InvalidParameterException::class)
    fun invalidActionShouldThrowInvalidParameterException() {
        democracy.parseAction("invalid")
    }

    @Test
    fun delegateActionShouldAcceptAnyCase() {
        assert(democracy.parseAction("delegate") == Action.DELEGATE)
        assert(democracy.parseAction("DELEGATE") == Action.DELEGATE)
        assert(democracy.parseAction("dElEgAtE") == Action.DELEGATE)
    }

    @Test
    fun pickActionShouldAcceptAnyCase() {
        assert(democracy.parseAction("pick") == Action.PICK)
        assert(democracy.parseAction("PICK") == Action.PICK)
        assert(democracy.parseAction("pIcK") == Action.PICK)
    }

    @Test(expected = IllegalStateException::class)
    fun parseVotesShouldntAcceptDuplicateVotes() {
        val lines = listOf("Alice pick pizza", "Alice pick salada")
        democracy.parseVotes(lines)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun parseVotesShouldThrowOnMissingValues() {
        val lines = listOf("Alice pick pizza", "Bob delegate")
        democracy.parseVotes(lines)
    }

    @Test(expected = IllegalStateException::class)
    fun parseVotesShouldThrowOnEmptyValues() {
        val lines = listOf("Alice pick pizza", "Bob  pizza")
        democracy.parseVotes(lines)
    }

    @Test
    fun shouldInvalidateSelfVote() {
        val lines = listOf("Ringo pick Ringo")
        val (invalid, _) = democracy.countVotes(lines)

        assert(invalid == 1)
    }

    @Test
    fun invalidateIndirectSelfVote() {
        val lines = listOf("Paul delegate John", "John delegate Ringo", "Ringo pick Paul")
        val (invalid, votes) = democracy.countVotes(lines)

        assert(invalid == 1)
        assert(votes["paul"] == 2)
    }

    @Test
    fun shouldCountDelegateVotes() {
        val lines = listOf("Paul delegate John", "John delegate Ringo", "Ringo delegate George", "George pick Beatles")
        val (_, votes) = democracy.countVotes(lines)

        assert(votes["beatles"] == 4)
    }

    @Test
    fun shouldCountDirectVotes() {
        val lines = listOf("Paul pick beatles", "John pick beatles", "Mick pick stones", "Keith pick stones")
        val (invalid, votes) = democracy.countVotes(lines)

        assert(invalid == 0)
        assert(votes["beatles"] == 2)
        assert(votes["stones"] == 2)
        assert(votes["other"] == null)
    }
}