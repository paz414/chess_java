package piece;

import main.Type;

public class Rook extends Piece{
    public Rook(int row,int col,int color) {  //creates a black/white rook at
                                            // the given row and column
        super(row,col,color);
        type= Type.ROOK;
        if(color==0){
            image=getImage("/piece/white-rook");
        }else{
            image=getImage("/piece/black-rook");
        }
    }
    public boolean canMove(int targetRow,int targetCol){
        if(withinBoard(targetRow,targetCol) && !isSameSquare(targetRow,targetCol)){
            if((targetRow==prevRow) || (targetCol==prevCol))
                return isValidSquare(targetCol,targetRow) && !pieceOnStraightLine(targetRow,targetCol);
        }
        return false;
    }
}
