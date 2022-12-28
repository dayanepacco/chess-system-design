package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;

	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() { // dimensão de um tabuleiro
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		check = false; // por padrão já inicia com false, opcional se quiser enfatizar
		initialSetup(); // inicia com o tabuleiro no inicio da partida
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

	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}

	public ChessPiece getPromoted() {
		return promoted;
	}

	public ChessPiece[][] getPieces() { // retornar matriz de peças de xadrez correspondente a essa partida
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()]; // p cada peça fazer um dowcasting p
																					// chesspiece
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
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
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		// #specialmove promotion
		promoted = null;                                     //assegurando que está fazendo um novo test
		if (movedPiece instanceof Pawn) {                   //se a peça movida for um peão teste
		 if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
		     promoted = (ChessPiece)board.piece(target);                   
		     promoted = replacePromotedPiece("Q");                         //recebe a letra da queen por padrão
		 }
		}
			 	 
		check = (testCheck(opponent(currentPlayer))) ? true : false;  //se o oponente ficou em check recebe v senão f
		
		if(testCheckMate(opponent(currentPlayer))) {     //se a jogada que eu fiz deixou em checkMate acabou o jogo
		checkMate = true;
		}
		else {                      //caso contrário partida continua e chama o próximo turno
			nextTurn();
		}
		
		// #specialmove en passant
		//se a peça movida for um peão e a diferença de linha 2 pra mais ou pra menos mov inicial fica vulneravel
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() -2 || target.getRow() == source.getRow() +2)) {
			enPassantVulnerable = movedPiece;
		}
		else {
			enPassantVulnerable = null;
		}
		return (ChessPiece)capturedPiece;
	}

	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("There is no piece to be promoted");
		}
		if (!type.equals("B")&& !type.equals("N") && !type.equals("R") & !type.equals("Q")) {      //comparar se String é igual a outro, tipo classe 
			return promoted;
			
		}
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);                             //remove a peça e guarda variavel p
        piecesOnTheBoard.remove(p);                           //excluir essa peça p da lista de peças no tabuleiro 	
	
        ChessPiece newPiece = newPiece(type, promoted.getColor());        //instancia a queen 
        board.placePiece(newPiece, pos);                                     //coloca no tabuleiro no lugar da outra peça
        piecesOnTheBoard.add(newPiece);
        
        return newPiece;
	}
	
	private ChessPiece newPiece(String type, Color color) {       //instancia a peça especifica 
		if (type.equals("B")) return new Bishop(board, color);
		if (type.equals("N")) return new Knight(board, color);
		if (type.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);
	}

	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece) board.removePiece(source); // tiro a peça do tabuleiro
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target); // tiro possível peça capturada
		board.placePiece(p, target); // coloco na posição de destino a peça que estava na posição de origem

		if (capturedPiece != null) { // testar se a peça capturada for diferente de null remover
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece); // add na lista de peças capturadas
		}

		// #specialmove castling kingside rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}

		// #specialmove castling queenside rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) { // se a peça foi um rei
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4); // posição da torre foi a posição
																						// do rei
			Position targetT = new Position(source.getRow(), source.getColumn() - 1); // destino posição do rei coluna
																						// menos 1
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT); // retira a torre da posição de origem
			board.placePiece(rook, targetT); // coloca na posição de destino
			rook.increaseMoveCount(); // incrementa quantidade de movimentos
		}

		// #specialmove en passant
		if (p instanceof Pawn) {
			// o meu peão andou na diagonal e não pegou peça siginica que foi um passant
			if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition; // posição do peão capturado
				if (p.getColor() == Color.WHITE) { // se a cor da peça que moveu for branca sig q a peça a ser capturada
													// está embaixo
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				} else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition); // remover esse peão do tabuleiro
				capturedPieces.add(capturedPiece); // add na lista de peças capturadas
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		return capturedPiece;
	}

	// desfazer o movimento
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target); // tira a peça que moveu no destino
		p.decreaseMoveCount();
		board.placePiece(p, source); // devolver para a posição de origem

		if (capturedPiece != null) { // voltar peça posição de destino
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece); // retirar da lista de capturadas
			piecesOnTheBoard.add(capturedPiece); // add na lista de peças no tabuleiro
		}

		// #specialmove castling kingside rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

		// #specialmove castling queenside rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

		// #specialmove en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece) board.removePiece(target);
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				} else {
					pawnPosition = new Position(4, target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}
		}
	}

	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}

	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private Color opponent(Color color) { // metodo devolve o oponente de uma cor
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private ChessPiece king(Color color) { // percorrer para encontrar a cor do rei
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList()); // lambda
		for (Piece p : list) { // para cada peça na list
			if (p instanceof King) { // se é uma instancia de rei
				return (ChessPiece) p; // encontrei o rei
			}
		}
		// se não encontrar nenhum rei lançar exceção
		throw new IllegalStateException("There is no " + color + " king on the board");
	}

	private boolean testCheck(Color color) { // testando se o rei dessa cor está em check
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream()
				.filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) { // se nessa matriz a posição correspondente do
																		// rei for true está em check
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) { // se essa cor não está em check não está em checkMate
			return false;
		}
		// pega tds peças da color
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) { // se existir alguma peça nessa lista que possua movimento que tira do check
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) { // percorrer linhas da matriz
				for (int j = 0; j < board.getColumns(); j++) { // percorrer colunas da matriz
					if (mat[i][j]) { // há um movimento possível que tira do check
						Position source = ((ChessPiece) p).getChessPosition().toPosition(); // posição da peça
						Position target = new Position(i, j); // posição de destino, movimento possível
						Piece capturedPiece = makeMove(source, target); // movimento da peça origem para destino
						boolean testCheck = testCheck(color); // testar se ainda está em check
						undoMove(source, target, capturedPiece); // desfazer o movimento do teste
						if (!testCheck) { // testar se não estava em check
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

	private void initialSetup() { // iniciar a partida de xadrez colocando as peças no tabuleiro
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
	}

}
