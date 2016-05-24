
package fingerprint;
public class Points {

    private int X;
    private int Y;
    
    public Points(){
    this.X = 0;
    this.Y=0;
    
    }
     public Points(int x,int y){
    this.X = x;
    this.Y=y;
    
    }
       public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public int getY() {
        return Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }
    
    
}
