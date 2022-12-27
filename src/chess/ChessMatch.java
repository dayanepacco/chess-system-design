package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Rook;

public class ChessMatch {           
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	public ChessMatch() {                //dimensão de um tabuleiro
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		check = false;                  //por padrão já inicia com false, opcional se quiser enfatizar
		initialSetup();                 //inicia com o tabuleiro no inicio da partida
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
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
	
	public boolean [][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);  //depois de executar o movimento
		
		if(testCheck(currentPlayer)) {                   //se ele se colocou em check
			undoMove(source, target, capturedPiece);        //desfazer o movimento
			throw new ChessException("You can´t put yourself in check");          //lançar exceção
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;  //se o oponente ficou em check recebe v senão f
		
		if(testCheckMate(opponent(currentPlayer))) {     //se a jogada que eu fiz deixou em checkMate acabou o jogo
		checkMate = true;
		}
		else {                      //caso contrário partida continua e chama o próximo turno
			nextTurn();
		}

		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);                  //tiro a peça do tabuleiro
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);      //tiro possível peça capturada
		board.placePiece(p,  target);                        //coloco na posição de destino a peça que estava na posição de origem
		
		if (capturedPiece != null) {                      //testar se a peça capturada for diferente de null remover 
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);      //add na lista de peças capturadas
		}
		return capturedPiece;
	}
	
	//desfazer o movimento
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);             //tira a peça que moveu no destino
		p.decreaseMoveCount();
		board.placePiece(p, source);                     //devolver para a posição de origem
		
		if(capturedPiece != null) {                       //voltar peça posição de destino
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);        //retirar da lista de capturadas
			piecesOnTheBoard.add(capturedPiece);          //add na lista de peças no tabuleiro
		}
	}
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE)? Color.BLACK : Color.WHITE;
	}
	
	private Color opponent(Color color) {                   //metodo devolve o oponente de uma cor
		return (color == Color.WHITE)? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {      //percorrer para encontrar a cor do rei
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());    //lambda
        for (Piece p : list) {            //para cada peça na list
        	if (p instanceof King) {              //se é uma instancia de rei
        		return (ChessPiece)p;             //encontrei o rei
        	}
        }
        //se não encontrar nenhum rei lançar exceção
        throw new IllegalStateException("There is no " + color + " king on the board");
	}
	
	private boolean testCheck(Color color) {      //testando se o rei dessa cor está em check
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
	    for (Piece p : opponentPieces) {
	    	boolean[][] mat = p.possibleMoves();
	    	if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {    //se nessa matriz a posição correspondente do rei for true está em check
	    		return true;
	    	}
	    }
	    return false;
	}
	
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {       //se essa cor não está em check não está em checkMate
			return false;
		}
		//pega tds peças da color 
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
	    for (Piece p : list) {                                //se existir alguma peça nessa lista que possua movimento que tira do check
	         boolean[][] mat = p.possibleMoves();             
	         for (int i=0; i<board.getRows(); i++) {             //percorrer linhas da matriz
	        	 for (int j=0; j<board.getColumns(); j++) {         //percorrer colunas da matriz
	        		 if (mat[i][j]) {                               //há um movimento possível que tira do check
	        			 Position source = ((ChessPiece)p).getChessPosition().toPosition();    //posição da peça 
	        			 Position target = new Position(i, j);                                 //posição de destino, movimento possível
	        			 Piece capturedPiece = makeMove(source, target);                    //movimento da peça origem para destino
	        			 boolean testCheck = testCheck(color);                              //testar se ainda está em check
	        			 undoMove(source, target, capturedPiece);                           //desfazer o movimento do teste
	        			 if(!testCheck) {                                                    //testar se não estava em check
	        				 return false;
	        			 }
	        		 }
	        	 }
	         }
	    }
	    return true;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	private void initialSetup() {                //iniciar a partida de xadrez colocando as peças no tabuleiro
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
	}

}
