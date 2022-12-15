package application;

import chess.ChessPiece;

public class UI {

	public static void printBoard(ChessPiece[][] pieces) {
		for (int i=0; i<pieces.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j=0; j<pieces.length; j++) {
				printPiece(pieces[i][j]);          //imprime a peça
			}
			System.out.println();        //para quebra de linha
		}
		System.out.println(" a b c d e f g h");

	}

	private static void printPiece(ChessPiece piece) { // método p imprimir uma peça
		if (piece == null) {
			System.out.print("-"); // sem peça
		} else {
			System.out.print(piece); // imprime a peça
		}
		System.out.print(" "); // espaço para que as peças não fiquem grudadas
	}

}
