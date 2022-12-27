package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece {

	public Queen(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "Q";          //essa letra aparece aonde estiver essa peça no tabuleiro
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0,0);
		
		//above
		p.setValues(position.getRow() -1, position.getColumn());
		while (getBoard().positionExists(p)&& !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow()-1);
		}
		if (getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//left
		p.setValues(position.getRow(), position.getColumn()-1);
		while (getBoard().positionExists(p)&& !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn()-1);
		}
		if (getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//right
		p.setValues(position.getRow(), position.getColumn()+1);
		while (getBoard().positionExists(p)&& !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn()+1);
		}
		if (getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//below
		p.setValues(position.getRow() +1, position.getColumn());
		while (getBoard().positionExists(p)&& !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow()+1);
		}
		if (getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//noroeste nw
				p.setValues(position.getRow() -1, position.getColumn()-1);
				while (getBoard().positionExists(p)&& !getBoard().thereIsAPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
					p.setValues(p.getRow()-1, p.getColumn()-1);
				}
				if (getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
				}
				
				//nordeste ne
				p.setValues(position.getRow()-1, position.getColumn()+1);
				while (getBoard().positionExists(p)&& !getBoard().thereIsAPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
					p.setValues(p.getRow()-1, p.getColumn()+1);
				}
				if (getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
				}
				
				//sudeste se
				p.setValues(position.getRow()+1, position.getColumn()+1);
				while (getBoard().positionExists(p)&& !getBoard().thereIsAPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
					p.setValues(p.getRow()+1, p.getColumn()+1);
				}
				if (getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
				}
				
				//sudoeste sw
				p.setValues(position.getRow() +1, position.getColumn()-1);
				while (getBoard().positionExists(p)&& !getBoard().thereIsAPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
					p.setValues(p.getRow()+1, p.getColumn()-1);
				}
				if (getBoard().positionExists(p)&& isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;
				}
		return mat;
	}
	

}
