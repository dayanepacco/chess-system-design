package boardgame;

public class Board {

	private int rows; // linhas
	private int columns; // colunas do tabuleiro
	private Piece[][] pieces; // matriz de peças

	public Board(int rows, int columns) { // contructor
		if (rows < 1 || columns < 1) {              //programação defensiva
			throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns]; // matriz de peças instanciada com linhas e colunas informada
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
//set rows e set colums retirado para não ter possibilidade de modificação

	public Piece piece(int row, int column) {               // metodo para retornar uma linha e uma coluna
		if (!positionExists(row, column)) {
			throw new BoardException("Position not on the board");
		}
		return pieces[row][column];
	}

	public Piece piece(Position position) {                // sobrecarga do método de cima, retornando pela posição
		if (!positionExists(position)) {
		throw new BoardException("Position not on the board");
		}
		return pieces[position.getRow()][position.getColumn()];
	}

	public void placePiece(Piece piece, Position position) { // pegar a matriz e atribuir a ela a peça q eu informei e não está mais null
		if(thereIsAPiece(position)) {
			throw new BoardException("There is already a piece on position " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}

	private boolean positionExists(int row, int column) { // existe quando esta dentro do tabuleiro
		return row >= 0 && row < rows && column >= 0 && column < columns; // linha maio e = o, menor q a altura do
																			// tabuleiro
	}

	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn()); // posição existe ou não

	}

	public boolean thereIsAPiece(Position position) {
		if(!positionExists(position)) {
			throw new BoardException("There is already a piece on position " + position);
		}
		return piece(position) != null ;         //se for diferente de null significa q tem uma peça nessa posição
	}

}
