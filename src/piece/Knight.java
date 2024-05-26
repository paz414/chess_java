package piece;

import main.Type;

public class Knight extends Piece{
    public Knight(int row,int col,int color) {  //creates a black/white knight at
        // the given row and column
        super(row,col,color);
        type= Type.KNIGHT;
        if(color==0){
            image=getImage("/piece/white-knight");
        }else{
            image=getImage("/piece/black-knight");
        }
    }
    public boolean canMove(int targetRow,int targetCol){
        if(withinBoard(targetRow,targetCol) && !isSameSquare(targetRow,targetCol)){
            if((Math.abs(targetRow - prevRow) * Math.abs(targetCol - prevCol) == 2))
                return isValidSquare(targetCol,targetRow);
        }
        return false;
    }
}
