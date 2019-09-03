/**
 * MazeSquare.java
 * A helper class for maze solving assignment.
 * Represents a single square within a rectangular maze.
 *
 * @author Alvin Bierley
 * @author Chris Kitchen
 */
public class MazeSquare
{
    private boolean visited;
    private int r, c;
    private boolean top, bottom, left, right, start, finish;
    
    /**
    * constructor for the MazeSquare class
    */
    public MazeSquare(int r, int c, boolean top, boolean bottom, boolean left, boolean right, boolean start, boolean finish)
    {
        this.visited = false;
        this.r = r; 
        this.c = c;
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.start = start;
        this.finish = finish;
    }
    
    /**
    * returns visited information of a MazeSquare
    */
    public boolean isVisited() {
        return visited;
    }
    
    /**
    * marks a MazeSquare as visited
    */
    public void visit() {
        visited = true;
    }
    
    /**
    * returns the row for the square
    */
    public int getR()
    {
        return r;
    }
    
    /**
    * returns the column of the square
    */
    public int getC()
    {
        return c;
    }
    
    /**
    * returns true or false for if there is a top wall
    */
    public boolean hasTopWall()
    {
        return top;
    }
    
    /**
    * returns true or false for if there is a bottom wall
    */
    public boolean hasBottomWall()
    {
        return bottom;
    }
    
    /**
    * returns true or false for if there is a left wall
    */
    public boolean hasLeftWall()
    {
        return left;
    }
    
    /**
    * returns true or false for if there is a right wall
    */
    public boolean hasRightWall()
    {
        return right;
    }
    
    /**
    * returns true or false for if the square is a start square
    */
    public boolean start()
    {
        return start;
    }
    
    /**
    * returns true or false for if the square is a finish square
    */
    public boolean finish()
    {
        return finish;
    }
}