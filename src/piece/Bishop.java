package piece;

import main.Type;

public class Bishop extends Piece{
    public Bishop(int row,int col,int color) {  //creates a black/white bishop at
                                             // the given row and column
        super(row,col,color);
        type = Type.BISHOP;
        if(color==0){
            image=getImage("/piece/white-bishop");
        }else{
            image=getImage("/piece/black-bishop");
        }
    }
    public boolean canMove(int targetRow,int targetCol){
        if(withinBoard(targetRow,targetCol) && !isSameSquare(targetRow,targetCol)){
            if(Math.abs(targetRow - prevRow) == Math.abs(targetCol - prevCol))
                return isValidSquare(targetCol,targetRow) && !pieceOnDiagonal(targetRow,targetCol);
        }
        return false;
    }
}
