package piece;

import main.GamePanel;
import main.Type;

public class Pawn extends Piece{
    public Pawn(int row,int col,int color) {  //creates a black/white pawn at
                                                // the given row and column
        super(row,col,color);
        type = Type.PAWN;
        if(color==0){
            image=getImage("/piece/white-pawn");
        }else{
            image=getImage("/piece/black-pawn");
        }
    }
    public boolean canMove(int targetRow,int targetCol) {
        if (withinBoard(targetRow, targetCol) && !isSameSquare(targetRow,targetCol))
        {
            int moveValue;
            if(color== 0) //white
                moveValue=-1;
            else
                moveValue=1;

            gettingHit=gettingHitP(targetRow,targetCol);


            //en passant
            if(Math.abs(targetCol - prevCol) == 1
                    && targetRow == prevRow + moveValue){
                for(Piece piece: GamePanel.pieces1){
                    if(piece.col==targetCol && piece.row==prevRow && piece.twostepped
                            && piece.color!=color){
                        gettingHit=piece;
                        return true;
                    }
                }
            }

            if (targetCol == prevCol && gettingHit == null) {
                if (targetRow == prevRow + moveValue) {
                    return true;
                } else if (targetRow == prevRow + 2 * moveValue  //first move
                        && ( (prevRow == 6 && color==0)
                        || (prevRow == 1 && color==1)) ) {
                    return !pieceOnStraightLine(targetRow, targetCol);
                }
            }
            else return Math.abs(targetCol - prevCol) == 1
                    && targetRow == prevRow + moveValue
                    && gettingHit != null
                    && gettingHit.color!=color; //capture

    }
    return false;
    }
}