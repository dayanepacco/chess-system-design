package boardgame;

public class Board {
	
	private int rows;                  //linhas
	private int columns;               // colunas do tabuleiro
	private Piece[][] pieces;          //matriz de peças
	
	public Board(int rows, int columns) {            //contructor 
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];            //matriz de peças instanciada com linhas e colunas informada
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
	

	
}