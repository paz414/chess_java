package piece;
import main.Board;
import main.GamePanel;
import main.Type;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Piece {

    public Type type;
    public BufferedImage image;
    public int x,y;                     //stores the x and y coordinates of the piece
    public int color;
    public int col,row,prevCol,prevRow; //stores the current and
                                        // previous column and row of the piece

    public Piece gettingHit;            //stores the piece that is getting hit

    public boolean moved,twostepped;

    public Piece(int row ,int col,int color){
        this.color=color;
        this.row=row;
        this.col=col;
        x=getX(col);
        y=getY(row);
        prevCol=col;
        prevRow=row;
    }
    public BufferedImage getImage(String imagePath){
        BufferedImage image=null;

        try {
            image= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    public int getX(int col){
        return col* Board.TILE_SIZE;
    }
    public int getY(int row){
        return row* Board.TILE_SIZE;
    }
    public void updatePosition(){

            if(type==Type.PAWN)
                if(Math.abs(row-prevRow)==2)
                    twostepped=true;
            x=getX(col);
            y=getY(row);
            prevCol=getCol(x);
            prevRow=getRow(y);
            moved=true;
    }
    public void resetPosition(){
        col=prevCol;
        row=prevRow;
        x=getX(col);
        y=getY(row);
    }
    public int getCol(int x){
        return (x+ Board.HALF_TILE_SIZE)/ Board.TILE_SIZE;
    }
    public int getRow(int y){
        return (y+ Board.HALF_TILE_SIZE)/ Board.TILE_SIZE;
    }
    /* We added the half_tile_size to find the col/row the center of the piece belongs
    *  to. The passed parameters x and y represent the corner of the square in which the
    *  piece should be.*/
    public void draw(Graphics2D g2){
        g2.drawImage(image,x,y, Board.TILE_SIZE, Board.TILE_SIZE,null);
        // drawImage is an abstract method in Graphics class that draws the specified image.
    }
    public boolean canMove(int targetRow,int targetCol){
        return false;
    }
    public boolean withinBoard(int targetRow,int targetCol){
        return targetRow>=0 && targetRow< 8 && targetCol>=0 && targetCol< 8;
    }
    public Piece gettingHitP(int targetRow,int targetCol){ //returns the piece that is getting hit by the active piece
        for(Piece piece: GamePanel.pieces1){
            if(piece.row==targetRow && piece.col==targetCol && piece!=this){
                return piece;
            }
        }
        return null;
    }
    public int getIndex(){
        return GamePanel.pieces1.indexOf(this);
    }
    public boolean isValidSquare(int targetCol,int targetRow){
        gettingHit=gettingHitP(targetRow,targetCol);
        if(gettingHit==null)
            return true;
        else{
            if(gettingHit.color!=this.color) // if the piece is of different color then it can be captured
                return true;
            else
                gettingHit=null;
        }
        return false;
    }
    public boolean pieceOnStraightLine(int targetRow,int targetCol){ //ensures that the active piece doesn't
                                                                    // jump over other pieces while moving
       //for left
        for(int i=prevCol-1;i>targetCol;i--){
            for(Piece piece: GamePanel.pieces1){
                if(piece.row==targetRow && piece.col==i){
                    gettingHit=piece;
                    return true;
                }
            }
        }
        //for right
        for(int i=prevCol+1;i<targetCol;i++){
            for(Piece piece: GamePanel.pieces1){
                if(piece.row==targetRow && piece.col==i){
                    gettingHit=piece;
                    return true;
                }
            }
        }
        //for down
        for(int i=prevRow-1;i>targetRow;i--){
            for(Piece piece: GamePanel.pieces1){
                if(piece.row==i && piece.col==targetCol){
                    gettingHit=piece;
                    return true;
                }
            }
        }
        //for up
        for(int i=prevRow+1;i<targetRow;i++){
            for(Piece piece: GamePanel.pieces1){
                if(piece.row==i && piece.col==targetCol){
                    gettingHit=piece;
                    return true;
                }
            }
        }
        return false;
    }
    public boolean pieceOnDiagonal(int targetRow,int targetCol){ //ensures that the active piece doesn't
                                                                // jump over other pieces while moving
            if(targetRow<prevRow)
            {
                //for up-left
                for(int i=prevCol-1;i>targetCol;i--){
                    int diff=Math.abs(i-prevCol);
                    for(Piece piece: GamePanel.pieces1){
                        if(piece.row==prevRow-diff && piece.col==i){
                            gettingHit=piece;
                            return true;
                        }
                    }
                }
                //for up-right
                for(int i=prevCol+1;i<targetCol;i++){
                    int diff=Math.abs(i-prevCol);
                    for(Piece piece: GamePanel.pieces1){
                        if(piece.row==prevRow-diff && piece.col==i){
                            gettingHit=piece;
                            return true;
                        }
                    }
                }
            }
            if(targetRow>prevRow)
            {
                //for down-left
                for(int i=prevCol-1;i>targetCol;i--){
                    int diff=Math.abs(i-prevCol);
                    for(Piece piece: GamePanel.pieces1){
                        if(piece.row==prevRow+diff && piece.col==i){
                            gettingHit=piece;
                            return true;
                        }
                    }
                }
                //for down-right
                for(int i=prevCol+1;i<targetCol;i++){
                    int diff=Math.abs(i-prevCol);
                    for(Piece piece: GamePanel.pieces1){
                        if(piece.row==prevRow+diff && piece.col==i){
                            gettingHit=piece;
                            return true;
                        }
                    }
                }
            }
            return false;
    }
    public boolean isSameSquare(int targetRow,int targetCol){
        return targetRow==prevRow && targetCol==prevCol;
    }
}
