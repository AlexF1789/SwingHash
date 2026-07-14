SwingHash
===

SwingHash is a simple utility created to perform and verify the hash of files and text. The core idea behind it is to emulate GtkHash providing the Java cross-platform advantages (for example under Windows in which GTK+ is a bit laggy). It's fully developed by hand (no AI of any kind) and it aims to give a lightweight general-purpose usage.

## Core structure
The program is written in Java using the Swing toolkit for the GUI, the MessageDigest class for the hash computation and the Stream API and Thread class for parallel computation.

The code is written with Java 1.8 compliance level making it runnable with all Java versions greater or equal than 8. It's anyway strongly advised to use it with Java 25 since the usage will be considerably more performant.

## Graphical interface
The graphical interface is modern and slick. It's offered in both the light and the dark theme. It's divided into an input panel, an output panel and the buttons to perform the program operations.

The program contains a menu bar created with a fluent approach which allows the user to perform all the basic operations.

<img width="300" alt="light" src="https://github.com/user-attachments/assets/584c6d77-e63a-4678-aa63-3ee0f03ef2a3" />
<img width="300" alt="immagine" src="https://github.com/user-attachments/assets/0878b6e5-9193-44ac-a3b8-8c8c4b995174" />

It is possible, from the settings to adoperate the classical cross-platform Java Swing look.

## Features
SwingHash offers the following features:

- text and file hash in all the JVM supported algorithms (at least SHA-256, SHA-512 and MD5);
- history of the computed hashes;
- light, dark or Swing legacy theme;
- settings and history persistence in the file system.

## Contributing
Contributions are appreciated and accepted. Please follow the fork -> pull request approach. All contributions will be analyzed before merging.
