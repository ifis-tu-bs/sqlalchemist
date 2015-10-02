## Synopsis

This project provides important backend functions for the SQL-Alchemist-Game.

## Code Example

Main function: Create a task - check, validate and parse the given xml-file and create a new taskinstance with an associated db. Furthermore the user can now manage the task with the given functions from class "Task":

Task test = new Task("alchemy-task", "alchemy-task");
test.startTask();
test.closeTask();

## Motivation

The project is part of the SQL-Alchemist-Game. Declared as a "Teamprojekt", the prject states the backendfunctions of the game.

## Installation

Simply insert the project to the sbt-builder.

## API Reference

Following...

## Tests

JUnit-Tests can be performed.

## Contributors

Create a new instance of a Task with the name of the given XML-file and start the task (startTask()).

## License

We use free java-downloadpackages (com.h2database, org.apache.logging.log4j, com.typesafe, com.novocode)