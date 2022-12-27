package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {
	
	private Color color;

	public ChessPiece(Board board, Color color) {     //construtor
		super(board);                                 //construtor da superclasse
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
// set apagado para que a cor não seja modificada e apenas acessada
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p.getColor() != color;
	}
	
	
	
	

}
