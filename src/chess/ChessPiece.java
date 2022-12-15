package chess;

import boardgame.Board;
import boardgame.Piece;

public class ChessPiece extends Piece {
	
	private Color color;

	public ChessPiece(Board board, Color color) {     //construtor
		super(board);                   //construtor da superclasse
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

// set apagado para que a cor não seja modificada e apenas acessada
	
	
	
	
	

}
