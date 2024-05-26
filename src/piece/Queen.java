package piece;

import main.Type;

public class Queen extends Piece{
    public Queen(int row,int col,int color) {  //creates a black/white queen at
                                                // the given row and column
        super(row,col,color);
        type= Type.QUEEN;
        if(color==0){
            image=getImage("/piece/white-queen");
        }else{
            image=getImage("/piece/black-queen");
        }
    }
    public boolean canMove(int targetRow,int targetCol){
        if(withinBoard(targetRow,targetCol) && !isSameSquare(targetRow,targetCol)){
            //vertical and horizontal movement
            if(targetCol==prevCol || targetRow==prevRow){
                return isValidSquare(targetCol,targetRow) && !pieceOnStraightLine(targetRow,targetCol);
            }
            //diagonal movement
            if(Math.abs(targetRow - prevRow) == Math.abs(targetCol - prevCol)){
                return isValidSquare(targetCol,targetRow) && !pieceOnDiagonal(targetRow,targetCol);
            }
        }
        return false;
    }
}

