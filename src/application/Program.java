package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
	
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();     //impirmir tabuleiro
		List<ChessPiece> captured = new ArrayList<>();
		
		while (!chessMatch.getCheckMate()) {         //enquanto a partida não estiver em checkMate
			try {
				UI.clearScreen();
				UI.printMatch(chessMatch, captured);       //vai receber a matriz de peças da partida
		        System.out.println();
		        System.out.println("Source: ");
		        ChessPosition source = UI.readChessPosition(sc);
		        
		        boolean[][] possibleMoves = chessMatch.possibleMoves(source);
		        UI.clearScreen();
		        UI.printBoard(chessMatch.getPieces(), possibleMoves);
		        System.out.println();
		        System.out.println("Target: ");
		        ChessPosition target = UI.readChessPosition(sc);
		        
		        ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
		        //quando efetuar um movimento e resultar em peça capturada add na lista
		        if(capturedPiece != null) {
		        	captured.add(capturedPiece);
		        }
		      
		        if (chessMatch.getPromoted() != null) {
		        	System.out.println("Enter piece for promotion (B/N/R/Q): ");
		        	String type = sc.nextLine();
		        	chessMatch.replacePromotedPiece(type);
		        }
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
		UI.clearScreen();            //deu checkMate limpa a tela
		UI.printMatch(chessMatch, captured);            //imprimir a partida novamente finalizada
		
	}

}
