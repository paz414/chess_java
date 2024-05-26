package piece;

import main.GamePanel;
import main.Type;

public class King extends Piece{
    public King(int row,int col,int color) {  //creates a black/white king at
        // the given row and column
        super(row,col,color);
        type = Type.KING;
        if(color==0){
            image=getImage("/piece/white-king");
        }else{
            image=getImage("/piece/black-king");
        }
    }
    public boolean canMove(int targetRow,int targetCol){
        if(withinBoard(targetRow,targetCol) && !isSameSquare(targetRow,targetCol)){
            //movement of the king
            if(Math.abs(targetRow - prevRow) <= 1 && Math.abs(targetCol - prevCol) <= 1)
                return isValidSquare(targetCol,targetRow);

            //castling
            if(!moved && GamePanel.checkingPiece==null){
                //right castling
                if(targetCol==prevCol+2 && targetRow==prevRow && !pieceOnStraightLine(targetRow,targetCol)){
                    for(Piece piece: GamePanel.pieces1){
                        if(piece.row==prevRow && piece.col==prevCol+3 && !piece.moved){
                            GamePanel.castlingPiece=piece;
                            return true;
                        }
                    }
                }

                //left castling
                if(targetCol==prevCol-2 && targetRow==prevRow && !pieceOnStraightLine(targetRow,targetCol)){
                    Piece p[]=new Piece[2];
                    for(Piece piece: GamePanel.pieces1){
                        if(piece.row==prevRow && piece.col==prevCol-4 && !piece.moved){
                            p[1]=piece;
                        }
                        if(piece.row==prevRow && piece.col==prevCol-3 && !piece.moved){
                            p[0]=piece;
                        }
                    }
                    if(p[0]==null && p[1]!=null && !p[1].moved){
                        GamePanel.castlingPiece=p[1];
                        return true;
                    }
                }

            }
        }

        return false;
    }
}
