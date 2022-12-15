package boardgame;

public class Piece {
	
	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		position = null;               //posição de uma peça recém criada
	}

	protected Board getBoard() {         //tabuleiro uso interno protected
		return board;
	}
	
   //set apagado para não permitir que o tabuleiro seja alterado
	

}
