# Liquid Democracy

Write a program to count votes in a referendum according to the following rules:
* Voters can choose one of the available options or delegate the choice to another voter.
* It is possible that a voter directly or indirectly delegates the vote to themselves. Such
votes become invalid. All votes delegated to such voter also become invalid.
* The result of the referendum is the list of options that got at least one vote with the
number of votes for each option.
* If there are invalid votes they should be counted and reported.

### Input
List of votes.
Each line consists of voter’s name and their choice, which can be either pick <choice>or delegate <name>. Names and choices are alphanumeric strings with no spaces. Example:
```
Alice pick Pizza
Bob delegate Carol
Carol pick Salad
Dave delegate Eve
Eve delegate Mallory
Mallory delegate Eve
```

### Output
One line for each choice and a line with the number of invalid votes if there are any. The lines
should contain the number of votes and the choice or the word invalid. The lines should be
sorted in descending order by number of votes with the invalid line coming last. Example:
```
2 Salad
1 Pizza
3 invalid
```


## Running
`./gradlew run`

`./gradlew test`
