# Chess.java

Welcome to Chess.java, a classical chess game implementation written in Java. This project aims to provide a user-friendly and interactive chess experience through a graphical user interface (GUI) created using Java's Graphics2D library. The GUI offers an intuitive and visually appealing way to play chess on your computer.

## Project Timeline

- **Start Date:** 19/5/2024
- **End Date:** 26/5/24

## Directory Structure

The project is organized into two main directories:

1. **src**: This is the source directory containing the Java code files. It contains the following packages:
   - **main**: This package includes all the classes and code related to game mechanics and the game design.
   - **piece**: This package contains information about chess pieces and dictates their movement rules.

2. **res**: This is the resource directory marked as a resource root. It contains PNG images of the chess pieces, which are used in the source code.

## Features

The Chess.java program includes the following features:

- [Castling of King](https://en.wikipedia.org/wiki/Castling#:~:text=Castling%20is%20permitted%20only%20if,pieces%20are%20moved%20at%20once.): The ability to castle the King under the appropriate conditions.
- [En Passant](https://en.wikipedia.org/wiki/En_passant): The pawn can capture an opponent's pawn that has just advanced two squares from its original square and is now on an adjacent file.
- Move Validation: Ensures that all moves made by the players adhere to the rules of chess.
- Check Detection: Identifies when a player's King is in check and provides appropriate notifications.
- Checkmate Detection: Recognizes when a player has achieved checkmate, ending the game.
- Pawn Promotion: Allows pawns to be promoted to a different piece (Queen, Rook, Bishop, or Knight) upon reaching the opposite end of the board.

## Getting Started

To get started with Chess.java, follow these steps:

1. Clone the repository:
   ````
   git clone https://github.com/paz414/chess_java.git
   ````
2. Navigate to the project directory:
   ````
   cd chess_java
   ````
3. Compile the Java source files:
To compile the source files, run the following command from the project root directory:

````
javac src/main/*.java src/piece/*.java
````
4. Run the main class: After successful compilation, run the main class with the following command:
````
java main.Main
````

Follow the on-screen instructions to start playing the game.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments

We would like to thank the open-source community for their valuable resources and contributions, which have been instrumental in the development of Chess.java.
