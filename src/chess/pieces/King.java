package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
	
	private ChessMatch chessMatch;
	

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;   //associou os dois
	}
	
	@Override
	public String toString() {
		return "K";
	}
	
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p == null || p.getColor() != getColor();
	}
	
	private boolean testRookCastling(Position position) {     //testar se a torre está apta para jogada rook
        ChessPiece p = (ChessPiece)getBoard().piece(position);
        return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;  //se a pça é diferente de null, ou é uma torre, ou se a cor é igual do rei
		
	}
		
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0,0);
		
		//above
		p.setValues(position.getRow()-1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//below
		p.setValues(position.getRow()+1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//left
		p.setValues(position.getRow(), position.getColumn()-1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//right
		p.setValues(position.getRow(), position.getColumn()+1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//noroeste  nw
		p.setValues(position.getRow()-1, position.getColumn()-1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//nordeste  ne
		p.setValues(position.getRow()-1, position.getColumn()+1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//sudoeste  sw
		p.setValues(position.getRow()+1, position.getColumn()-1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//sudeste  se
		p.setValues(position.getRow()+1, position.getColumn()+1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// #specialmove castling   
		if(getMoveCount()== 0 && !chessMatch.getCheck()) {
			// #specialmove castling kingside rook
			Position posT1 = new Position(position.getRow(), position.getColumn() + 3);
			if (testRookCastling(posT1)) {   //se tudo isso der vdd 
				Position p1 = new Position(position.getRow(), position.getColumn() + 1);
				Position p2 = new Position(position.getRow(), position.getColumn() + 2);
				if (getBoard().piece(p1)== null && getBoard().piece(p2) == null) {
					mat[position.getRow()][position.getColumn()+2] = true;
				}
			}

		// #specialmove castling queenside rook
				Position posT2 = new Position(position.getRow(), position.getColumn() - 4);
				if (testRookCastling(posT2)) {   //se tudo isso der vdd 
					Position p1 = new Position(position.getRow(), position.getColumn() - 1);
					Position p2 = new Position(position.getRow(), position.getColumn() - 2);
					Position p3 = new Position(position.getRow(), position.getColumn() - 3);
					if (getBoard().piece(p1)== null && getBoard().piece(p2) == null && getBoard().piece(p3)== null) {
						mat[position.getRow()][position.getColumn()-2] = true;
					}
				}
		}
		
		return mat;
	}
	
	

}
