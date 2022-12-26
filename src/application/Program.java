package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
	
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();     //impirmir tabuleiro
		
		while (true) {
			try {
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces());       //vai receber a matriz de peças da partida
		        System.out.println();
		        System.out.println("Source: ");
		        ChessPosition source = UI.readChessPosition(sc);
		        
		        System.out.println();
		        System.out.println("Target: ");
		        ChessPosition target = UI.readChessPosition(sc);
		        
		        ChessPiece capturedPiece = chessMatch.performChessMove(source, target);	
			}
			catch(ChessException  e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch(InputMismatchException  e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
	
        
		}
	}

}
