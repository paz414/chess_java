//DISCLAIMER: Too many comments ahead!!
package main;
import javax.swing.JPanel; // JPanel is a class in javax.swing package
                            // that provides a container for holding components

import java.awt.*;
// that provides methods to work with colors

// that provides methods to draw the board and the pieces

import piece.*; // Piece is a class in piece package
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{

    public static final int WIDTH=1100,HEIGHT=800; // width and height of the window
    final int FPS=120; // frames per second
    Thread gameThread; // thread to run the game
    Board board=new Board();
    Mouse mouse=new Mouse(); // create a new Mouse object

    //PIECE
    public static ArrayList<Piece> pieces=new ArrayList<>(); // stores all the pieces
    public static ArrayList<Piece> pieces1=new ArrayList<>(); // back-up list
    public static ArrayList<Piece> promotionPieces=new ArrayList<>(); // stores the promotion pieces
    Piece activePiece;// stores the active piece
    public static Piece castlingPiece; //the castling piece
    public static Piece checkingPiece; //the checking piece

    //COLOR
    public static final int WHITE=0;
    public static final int BLACK=1;
    int currentColor=WHITE;    //game starts with white color

    //checkers
    boolean canMove=false;
    boolean validMove=false;
    boolean promotion;
    boolean gameOver;
    boolean staleMate;

    public GamePanel(){
        setPreferredSize(new Dimension(WIDTH,HEIGHT)); // set the size of the panel
        //i want to set the background color such that both white and black promotion pieces are visible
        setBackground(Color.pink);// set the background color of the panel as black
        addMouseListener(mouse); // add the mouse listener to the panel
        addMouseMotionListener(mouse); // add the mouse motion listener to the panel

        setPieces(); // set the pieces on the board
        copyPieces(/*source*/pieces,/*target*/pieces1); // copy the pieces to pieces1

    }
    public void launchGame(){
        gameThread=new Thread(this); // create a new thread
        gameThread.start(); // start the thread
    }

    public void setPieces(){

        //WHITE PIECES
        pieces.add(new Pawn(6,0,WHITE));
        pieces.add(new Pawn(6,1,WHITE));
        pieces.add(new Pawn(6,2,WHITE));
        pieces.add(new Pawn(6,3,WHITE));
        pieces.add(new Pawn(6,4,WHITE));
        pieces.add(new Pawn(6,5,WHITE));
        pieces.add(new Pawn(6,6,WHITE));
        pieces.add(new Pawn(6,7,WHITE));
        pieces.add(new Knight(7,1,WHITE));
        pieces.add(new Knight(7,6,WHITE));
        pieces.add(new Rook(7,0,WHITE));
        pieces.add(new Rook(7,7,WHITE));
        pieces.add(new Bishop(7,2,WHITE));
        pieces.add(new Bishop(7,5,WHITE));
        pieces.add(new Queen(7,3,WHITE));
        pieces.add(new King(7,4,WHITE));




        //WHITE PIECES
        pieces.add(new Pawn(1,0,BLACK));
        pieces.add(new Pawn(1,1,BLACK));
        pieces.add(new Pawn(1,2,BLACK));
        pieces.add(new Pawn(1,3,BLACK));
        pieces.add(new Pawn(1,4,BLACK));
        pieces.add(new Pawn(1,5,BLACK));
        pieces.add(new Pawn(1,6,BLACK));
        pieces.add(new Pawn(1,7,BLACK));
        pieces.add(new Knight(0,1,BLACK));
        pieces.add(new Knight(0,6,BLACK));
        pieces.add(new Rook(0,0,BLACK));
        pieces.add(new Rook(0,7,BLACK));
        pieces.add(new Bishop(0,2,BLACK));
        pieces.add(new Bishop(0,5,BLACK));
        pieces.add(new Queen(0,3,BLACK));
        pieces.add(new King(0,4,BLACK));


    }

    private void copyPieces(ArrayList<Piece> source,ArrayList<Piece> destination){
        destination.clear();
        for(Piece piece:source){
            destination.add(piece);
        }
    }

    public void run(){
        //Game Loop
        //the purpose of the game loop is to update the game state and render the game
        //at a constant frame rate

        double DrawInterval=(double) 1000000000/FPS;
        double delta=0;
        long lastTime=System.nanoTime();
        long currentTime;

        while(gameThread!=null){
            currentTime=System.nanoTime();
            delta+=(currentTime-lastTime)/DrawInterval;
            lastTime=currentTime;
            if(delta>=1){
                update(); // update the game state
                repaint();  // render the updated game
                delta--;
            }
        }
    }
    private void changePlayer(){
        if(currentColor==WHITE){
            currentColor=BLACK;
            //reset the two-step move of the black pawns
            for(Piece piece:pieces1){
                if(piece.color==BLACK)
                    piece.twostepped=false;
            }
        }
        else{
            currentColor=WHITE;
            //reset the two-step move of the white pawns
            for(Piece piece:pieces1){
                if(piece.color==WHITE)
                    piece.twostepped=false;
            }
        }
        activePiece=null;
    }
    private boolean illegalKingMove(Piece king){
        if(king.type==Type.KING){
            for(Piece piece:pieces1){
                if(piece!=king && piece.color!=king.color){
                    if(piece.canMove(king.row,king.col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private void update(){ // updates the game state
        if(promotion)
        {
            promoting();
        }
        else if(!gameOver && !staleMate){
            if(mouse.pressed){
                if(activePiece==null) {
                    for (Piece piece : pieces1) {
                        if (piece.color == currentColor && piece.col == mouse.x / Board.TILE_SIZE
                                && piece.row == mouse.y / Board.TILE_SIZE) { // if the piece is of the current player
                            activePiece = piece;
                            break;
                        }
                    }
                }
                else{
                    simulateMove();
                }
            }
            else{
                if(activePiece!=null){
                    if(validMove) {
                        copyPieces(pieces1, pieces);
                        activePiece.updatePosition();
                        if(castlingPiece!=null && checkingPiece==null){
                            castlingPiece.updatePosition();
                        }
                        if(isKinginCheck() && isCheckMate()){
                            gameOver=true;
                        }
                        else if(isStaleMate() && !isKinginCheck())
                            staleMate=true;
                        else {
                            if (canPromote())
                                promotion = true;
                            else
                                changePlayer();
                        }
                    }
                    else {
                        copyPieces(pieces,pieces1);
                        activePiece.resetPosition();
                        activePiece = null;
                    }
                }
            }
        }

    }

    private void promoting(){
        if(mouse.pressed){
            for(Piece piece:promotionPieces){
                if(piece.col==mouse.x/Board.TILE_SIZE && piece.row==mouse.y/Board.TILE_SIZE){
                    switch(piece.type){
                        case QUEEN:
                            pieces1.add(new Queen(activePiece.row,activePiece.col,currentColor));
                            break;
                        case ROOK:
                            pieces1.add(new Rook(activePiece.row,activePiece.col,currentColor));
                            break;
                        case BISHOP:
                            pieces1.add(new Bishop(activePiece.row,activePiece.col,currentColor));
                            break;
                        case KNIGHT:
                            pieces1.add(new Knight(activePiece.row,activePiece.col,currentColor));
                            break;
                    }
                    pieces1.remove(activePiece.getIndex());
                    copyPieces(pieces1,pieces);
                    activePiece=null;
                    promotion=false;
                    changePlayer();
                }
            }
        }
    }

    private Piece getKing(boolean opponent){
        Piece king=null;
        for(Piece piece:pieces1){
            if(opponent){
                if(piece.type==Type.KING && piece.color!=currentColor){
                    king=piece;
                    break;
                }
            }
            else{
                if(piece.type==Type.KING && piece.color==currentColor){
                    king=piece;
                    break;
                }
            }
        }
        return king;
    }

    private boolean isKinginCheck(){
        Piece king=getKing(true);
        if(activePiece.canMove(king.row,king.col)){
            checkingPiece=activePiece;
            return true;
        }
        checkingPiece=null;
        return false;
    }
    public boolean kingIsInCheck(){  //checks if the current player's king is in check
        Piece king=getKing(false);
        for(Piece piece:pieces1){
            if(piece.color!=king.color && piece.canMove(king.row,king.col)){
                return true;
            }
        }
        return false;
    }

    private void simulateMove(){

        canMove=false;
        validMove=false;
        copyPieces(pieces,pieces1);

        //reset the position of the castling piece
        if(castlingPiece!=null){
            castlingPiece.resetPosition();
            castlingPiece=null;
        }

        activePiece.x=mouse.x-Board.HALF_TILE_SIZE; // set the x coordinate of the active piece
        activePiece.y=mouse.y-Board.HALF_TILE_SIZE; //we did the -Board.HALF_TILE_SIZE to
                                                    // center the cursor
        activePiece.col=activePiece.getCol(activePiece.x);
        activePiece.row=activePiece.getRow(activePiece.y);

        if(activePiece.canMove(activePiece.row,activePiece.col)){
            canMove=true;

            if(activePiece.gettingHit!=null){
                pieces1.remove(activePiece.gettingHit.getIndex());
            }
            if(checkingPiece==null)
                checkCastling();
            if(!illegalKingMove(activePiece) && !kingIsInCheck()){
                validMove=true;
            }
        }
    }
    private void checkCastling(){
        if(castlingPiece !=null){
            if(castlingPiece.col==0)
                castlingPiece.col+=3;
            else if(castlingPiece.col==7)
                castlingPiece.col-=2;
            castlingPiece.x=castlingPiece.getX(castlingPiece.col);
        }
    }

    private boolean isCheckMate(){
        Piece king =getKing(true);
        if(kingCanMove(king))
            return false;
        else {
            //it is a check and the king cannot move
            //but another piece can block the king and dismiss the check

            int colDiff=Math.abs(checkingPiece.col-king.col);
            int rowDiff=Math.abs(checkingPiece.row-king.row);

            if(colDiff==0) {// piece is attacking vertically

                if(checkingPiece.row<king.row) {//below
                    for(int row=checkingPiece.row;row<king.row;row++)
                    {
                        for(Piece piece:pieces1)
                            if(piece!=king && piece.color!=currentColor && piece.canMove(row,king.col))
                                return false;
                    }
                }
                else {//above
                    for(int row=king.row;row<checkingPiece.row;row++)
                    {
                        for(Piece piece:pieces1)
                            if(piece!=king && piece.color!=currentColor && piece.canMove(row,king.col))
                                return false;
                    }
                }
            }
            else if(rowDiff==0){ //piece is attacking horizontally
                if(checkingPiece.col>king.col) {//on right
                    for(int col=king.col;col<checkingPiece.col;col++)
                    {
                        for(Piece piece:pieces1)
                            if(piece!=king && piece.color!=currentColor && piece.canMove(king.row,col))
                                return false;
                    }
                }
                else {//on left
                    for(int col=king.col;col>checkingPiece.col;col--)
                    {
                        for(Piece piece:pieces1)
                            if(piece!=king && piece.color!=currentColor && piece.canMove(king.row,col))
                                return false;
                    }
                }
            }
            else if(rowDiff==colDiff){//piece is attacking diagonally
                //checking piece is up
                if(checkingPiece.row>king.row)
                {
                    //checking piece left-up
                    if(checkingPiece.col<king.col){
                        for(int col=checkingPiece.col,row=checkingPiece.row;col<king.col;row--,col++)
                            for(Piece piece:pieces1)
                                if(piece!=king && piece.color!=currentColor && piece.canMove(row,col))
                                    return false;
                    }
                    //checking piece right-up
                    else{
                        for(int col=checkingPiece.col,row=checkingPiece.row;col>king.col;row--,col--)
                            for(Piece piece:pieces1)
                                if(piece!=king && piece.color!=currentColor && piece.canMove(row,col))
                                    return false;
                    }
                }
                //checking piece is down
                else{
                    //checking piece is left down
                    if(checkingPiece.col<king.col){
                        for(int col=checkingPiece.col,row=checkingPiece.row;col<king.col;row++,col++)
                            for(Piece piece:pieces1)
                                if(piece!=king && piece.color!=currentColor && piece.canMove(row,col))
                                    return false;
                    }
                    //checking piece is right down
                    else{
                        for(int col=checkingPiece.col,row=checkingPiece.row;col>king.col;row++,col--)
                            for(Piece piece:pieces1)
                                if(piece!=king && piece.color!=currentColor && piece.canMove(row,col))
                                    return false;
                    }
                }
            }
            //if piece is a knight checkmate cannot be blocked
        }
        return true;
    }
    private boolean kingCanMove(Piece king){
        if(isValidMove(king,-1,-1)) return true;
        if(isValidMove(king,-1,0)) return true;
        if(isValidMove(king,-1,1)) return true;
        if(isValidMove(king,0,-1)) return true;
        if(isValidMove(king,0,1)) return true;
        if(isValidMove(king,1,-1)) return true;
        if(isValidMove(king,1,0)) return true;
        if(isValidMove(king,1,1)) return true;
        return false;
    }
    private boolean isValidMove(Piece king,int colplus,int rowplus){
        boolean isValid=false;
        king.col+=colplus;
        king.row+=rowplus;
        //simulating the possible move
        if(king.canMove(king.row,king.col)){
            if(king.gettingHit !=null){
                pieces1.remove(king.gettingHit.getIndex());
            }
            if(!illegalKingMove(king)){
                isValid=true;
            }
        }
        king.resetPosition();
        copyPieces(pieces,pieces1);
        return isValid;
    }
    public void paintComponent(Graphics g){ // paints the components
                                            // it is called by repaint() call
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g; // Graphics2D is different from Graphics
                                    // it provides more methods to draw shapes
        //draw the board
        board.draw(g2);

        //draw the pieces
        for(Piece piece:pieces1){
            piece.draw(g2);
        }
        if(activePiece!=null){// highlights the active piece wherever it is dragged
            if(canMove) {
                if(!illegalKingMove(activePiece) && !kingIsInCheck()) {
                    g2.setColor(Color.white);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2.fillRect(activePiece.col * Board.TILE_SIZE, activePiece.row * Board.TILE_SIZE,
                            Board.TILE_SIZE, Board.TILE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
                else{
                    g2.setColor(Color.red);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2.fillRect(activePiece.col * Board.TILE_SIZE, activePiece.row * Board.TILE_SIZE,
                            Board.TILE_SIZE, Board.TILE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2.fillRect(activePiece.col * Board.TILE_SIZE, activePiece.row * Board.TILE_SIZE,
                            Board.TILE_SIZE, Board.TILE_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
            }
            activePiece.draw(g2);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); // improves the quality of the text and graphics
        g2.setFont(new Font("Source Sans Pro",Font.BOLD,30)); // set the font of the text
        g2.setColor(Color.white); // set the color of the text

        if(promotion) {
            g2.drawString("PROMOTE TO", 840, 150);
            for (Piece piece : promotionPieces) {
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row), Board.TILE_SIZE, Board.TILE_SIZE, null);
            }

        }
        else {
            if (currentColor == WHITE) {
                g2.drawString("WHITE'S TURN", 840, 650);
                if(checkingPiece!=null && checkingPiece.color==BLACK){
                    g2.setColor(Color.red);
                    g2.drawString("CHECK!!",840,400);
                }
            }
            else {
                g2.drawString("BLACK'S TURN", 840, 150);
                if(checkingPiece!=null && checkingPiece.color==WHITE){
                    g2.setColor(Color.red);
                    g2.drawString("CHECK!!",840,400);
                }
            }
        }
        if(gameOver){
            String s=currentColor==WHITE?"White Wins!":"Black Wins!";
            g2.setFont(new Font("Times New Roman",Font.BOLD,90));
            g2.setColor(Color.cyan);
            g2.drawString(s,200,420);
        }
        if(staleMate){
            String s="STALEMATE";
            g2.setFont(new Font("Times New Roman",Font.BOLD,90));
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(s,200,420);
        }

    }
    private boolean isStaleMate(){
        int count=0;
        for(Piece piece:pieces1)
            if(piece.color!=currentColor)
                count++;
        if(count==1)//only king is left
            return !kingCanMove(getKing(false));
        return false;
    }
    public boolean canPromote(){
        if(activePiece.type==Type.PAWN){
            if(activePiece.color==WHITE && activePiece.row==0 || activePiece.color==BLACK && activePiece.row==7)
            {
                promotionPieces.clear();
                promotionPieces.add(new Queen(2,9,currentColor));
                promotionPieces.add(new Rook(3,9,currentColor));
                promotionPieces.add(new Bishop(4,9,currentColor));
                promotionPieces.add(new Knight(5,9,currentColor));
                return true;
            }
        }
        return false;
    }
}
