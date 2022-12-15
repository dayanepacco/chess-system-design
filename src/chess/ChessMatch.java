package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {           
	
	private Board board;
	
	public ChessMatch() {                //dimensão de um tabuleiro
		board = new Board(8, 8);
		initialSetup();                 //inicia com o tabuleiro no inicio da partida
	}
	
	public ChessPiece[][] getPieces() {      //retornar matriz de peças de xadrez correspondente a essa partida
	    ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];  //p cada peça fazer um dowcasting p chesspiece
	    for (int i=0; i<board.getRows(); i++) {
	    	for ( int j=0; j<board.getColumns(); j++) {
	    		mat[i][j] = (ChessPiece) board.piece(i, j);
	    	}
	    }
	    return mat;
	}
	
	private void initialSetup() {                //iniciar a partida de xadrez colocando as peças no tabuleiro
		board.placePiece(new Rook(board, Color.WHITE), new Position(2, 1));     //posição, cor
        board.placePiece(new King(board, Color.BLACK), new Position(0, 4));	
        board.placePiece(new King(board, Color.WHITE), new Position(7, 4));
	}

}
