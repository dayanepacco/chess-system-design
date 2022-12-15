package application;

import chess.ChessMatch;

public class Program {

	public static void main(String[] args) {
	
		ChessMatch chessMatch = new ChessMatch();     //impirmir tabuleiro
		UI.printBoard(chessMatch.getPieces());       //vai receber a matriz de peças da partida

	}

}
