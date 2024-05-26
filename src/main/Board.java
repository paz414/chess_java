package main;
import java.awt.Color;
import java.awt.Graphics2D;
public class Board {
    final int MAX_COL=8;
    final int MAX_ROW=8;
    public static final int TILE_SIZE=100;
    public static final int HALF_TILE_SIZE=TILE_SIZE/2;

    public void draw(Graphics2D g){
        for(int row=0;row<MAX_ROW;row++){
            for(int col=0;col<MAX_COL;col++){
                if((row+col)%2==0){
                    g.setColor(new Color(210,165,125));
                }else{
                    g.setColor(new Color(175,115,70));
                }
                g.fillRect(col*TILE_SIZE,row*TILE_SIZE,TILE_SIZE,TILE_SIZE);
            }
        }
    }

}
