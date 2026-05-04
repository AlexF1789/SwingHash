SwingHash
===

SwingHash is a simple utility created to perform and verify the hash of files and text. Currently it's under development and its usage is **advised** only if the files passed have backups.

## Core structure
The program is written in Java using the Swing toolkit for the GUI, the MessageDigest class for the hash computation and the Stream API and Thread class for parallel computation.

The code is written with Java 1.8 compliance level making it runnable with all Java versions greater or equal than 8. It's anyway strongly advised to use it with Java 25 since the usage will be considerably more performant.

## Graphical interface
The graphical interface is modern and slick. It's offered in both the light and the dark theme. It's divided into an input panel, an output panel and the buttons to perform the program operations.

The program contains a menu bar created with a fluent approach which allows the user to perform all the basic operations.

## Development interest
So far the idea for the program development is to extend the hashing algorithms to all the ones supported by the MessageDigest class. The structure itself is already made to be as modular as possible.

Another goal for the project is to persist the preferences, with regard to the **theme** and the **algorithms** to grant a more fluent usage experience.