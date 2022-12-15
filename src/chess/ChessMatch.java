package chess;

import boardgame.Board;

public class ChessMatch {           
	
	private Board board;
	
	public ChessMatch() {                //dimensão de um tabuleiro
		board = new Board(8, 8);
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

}
