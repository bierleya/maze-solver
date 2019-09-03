/**
 * Maze.java
 * A class for loading and printing mazes from files.
 *
 * @author Alvin Bierley 
 * @author Chris Kitchen
 *
 * Received assistance from former Data Structures student Peter in the load & print functions
 *
 * September 23, 2018
 */

import java.io.*;
import java.util.*;

public class Maze
{
    // this is the 2d array that stores the maze
    private ArrayList<ArrayList<MazeSquare>> rowList;
    // width and height of maze
    private int w, h;
    // x and y coordinates of the starting point (begins at zero)
    private int sx, sy;
    // x and y coordinates of the finishing point
    private int fx, fy;
    // LLStack of MazeSquare Objects
    private LLStack<MazeSquare> mazeStack = new LLStack<MazeSquare>();
    /**
     * Constructor for the Maze class
     */
    public Maze()
    {
        rowList = new ArrayList<ArrayList<MazeSquare>>();
    }

    /**
     * Load in a Maze from a given file
     *
     * @param fileName the name of the file containing the maze
     */
    public void load(String fileName)
    {
        // Create a scanner for the given file
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e)
        {
            System.err.println(e);
            System.exit(1);
        }

        // First line of file is "w h"
        String[] lineParams = scanner.nextLine().split(" ");
        w = Integer.parseInt(lineParams[0]);
        h = Integer.parseInt(lineParams[1]);
        
        // Reads the coordinates for the start square
        lineParams = scanner.nextLine().split(" ");
        sx = Integer.parseInt(lineParams[0]);
        sy = Integer.parseInt(lineParams[1]);
        
        // Reads the coordinates for the finish square
        lineParams = scanner.nextLine().split(" ");
        fx = Integer.parseInt(lineParams[0]);
        fy = Integer.parseInt(lineParams[1]);
        
        // stores the line of symbols in the text file
        String line;
        int row = 0;
        boolean top, bottom, left, right, start, finish;
        // default values for bottom and left
        bottom = false;
        left = false;
        
        // reads the text while there is another line to read and stores the info for MazeSquare
        while (scanner.hasNextLine())
        {
            line = scanner.nextLine();
            rowList.add(new ArrayList<MazeSquare>());
            
            // iterates through each symbol in the line
            // Peter explained how if and else statements could be implemented
            for (int i=0; i<line.length(); i++)
            {
                if (row==0)
                {
                    top = true; 
                }
                else
                {
                    top = rowList.get(row-1).get(i).hasBottomWall();
                }
                
                if (i<line.length()-1)
                {
                    char nextSquare = line.charAt(i+1);
                    if (nextSquare == 'L' || nextSquare == '|')
                    {
                        right = true;
                    }
                    else
                    {
                        right = false;
                    }
                }
                else
                {
                    right = true;
                } 
                if (line.charAt(i) == 'L')
                {
                    left = true;
                    bottom = true;
                }
                else if (line.charAt(i) == '|')
                {
                    left = true;
                    bottom = false;
                }
                else if (line.charAt(i) == '_')
                {
                    left = false;
                    bottom = true;
                }
                else if (line.charAt(i) == '-')
                {
                    left = false;
                    bottom = false;
                }
                
                // determines the start and finish point based off coordinates
                start = sx == i  && sy == row;
                finish = fx == i && fy == row;
                
                rowList.get(row).add(new MazeSquare(row, i, top, bottom, left, right, start, finish));
            }
            row++;
        }
    }
    
    /**
    * Computes and returns a solution to this maze. If there are multiple 
    * solutions, only one is returned, and getSolution() makes no guarantees about 
    * which one. However, the returned solution will not include visits to dead 
    * ends or any backtracks, even if backtracking occurs during the solution 
    * process.
    *
    * @return a LLStack of MazeSquare objects containing the sequence of squares
    *         visited to go from the start square (bottom of the stack) to the 
    *         finish square (top of the stack).
    */
    public LLStack<MazeSquare> getSolution() {
            MazeSquare start = rowList.get(sy).get(sx);
            MazeSquare end = rowList.get(fy).get(fx);
            MazeSquare firstVisited = rowList.get(sy).get(sx);
            // pushes the start square onto the stack
            mazeStack.push(firstVisited);
            firstVisited.visit();
            return solveTheMaze();
        }
    
        /**
        * Finds the solution for the maze
        * If no solution is available, "maze is unsolvable" is printed
        * Returns a stack of MazeSquare objects containing the order of squares visited from the
        * start square to the finish square
        */
        public LLStack<MazeSquare> solveTheMaze() {
            // Keeps on repeating until the maze is solved or until the stack is empty
            while (!mazeStack.isEmpty() && (mazeStack.peek().getR() != fy || mazeStack.peek().getC() != fx)) {
                // peeks at the top of the stack
                MazeSquare peek = mazeStack.peek();
                
                /*
                if the current square does not have a bottom wall and hasn't yet been visited, the 
                MazeSquare below the current MazeSquare is pushed onto the stack and is marked as visited
                */
                if (!peek.hasBottomWall() && !rowList.get(peek.getR() + 1).get(peek.getC()).isVisited()) {
                    mazeStack.push(rowList.get(peek.getR() + 1).get(peek.getC()));
                    rowList.get(peek.getR() + 1).get(peek.getC()).visit();
                }
                
                
                /*
                if the current square does not have a right wall and hasn't yet been visited, the MazeSquare
                to the right of the the current MazeSquare is pushed to the top of the stack and is marked
                as visited
                */
                else if (!peek.hasRightWall() && !rowList.get(peek.getR()).get(peek.getC() + 1).isVisited()) {
                    mazeStack.push(rowList.get(peek.getR()).get(peek.getC() + 1));
                    rowList.get(peek.getR()).get(peek.getC() + 1).visit();
                }
                
                /*
                if the current square does not have a top wall and hasn't yet been visited, the MazeSquare
                above the current MazeSquare is pushed to the top of the stack and is marked as visited
                */
                else if (!peek.hasTopWall() && !rowList.get(peek.getR() - 1).get(peek.getC()).isVisited()) {
                    mazeStack.push(rowList.get(peek.getR() - 1).get(peek.getC()));
                    rowList.get(peek.getR() - 1).get(peek.getC()).visit();
                }
                
                /*
                if the current square does not have a left wall and hasn't yet been visited, the MazeSquare
                to the left of the current MazeSquare is pushed to the top of the stack and is marked as
                visited
                */
                else if (!peek.hasLeftWall() && !rowList.get(peek.getR()).get(peek.getC() - 1).isVisited()) {
                    mazeStack.push(rowList.get(peek.getR()).get(peek.getC() - 1));
                    rowList.get(peek.getR()).get(peek.getC() - 1).visit();
                }
                
                // if none of the above is valid, then the top of the stack is popped off
                else {
                    mazeStack.pop();
                }
            }
            // prints that the maze is unsolvable once the stack is empty
            if (mazeStack.isEmpty()) {
              System.out.println("Maze is unsolvable.");
            }
            return mazeStack;

        }

    /**
     * Print the Maze to System.out
     */
    public void print()
    {
        // solution stack for the given maze
        LLStack <MazeSquare> solution = getSolution();
        // Peter explained how to use ArrayList effectively
        ArrayList<MazeSquare> currentRow;
        MazeSquare currentSquare;
        
        // iterates through the rows in the maze
        for (int r=0; r<rowList.size(); r++)
        {
            currentRow = rowList.get(r);
            
            // prints out the top line of the square
            for (int c=0; c<currentRow.size(); c++)
            {
                System.out.print("+");
                if (currentRow.get(c).hasTopWall())
                {
                    System.out.print("-----");
                }
                else
                {
                    System.out.print("     ");
                }
            }
            System.out.println("+");
            
            // prints out the second line of the square
            for (int c=0; c<currentRow.size(); c++)
            {
                if (currentRow.get(c).hasLeftWall())
                {
                    System.out.print("|     ");  
                }
                else
                {
                    System.out.print("      ");
                }  
            }
            System.out.println("|");
            
            // prints out the third line, adds 'S' or 'F' if necessary
            for (int c=0;c<currentRow.size(); c++)
            {
                if(currentRow.get(c).hasLeftWall())
                {
                    System.out.print("|");
                }
                else
                {
                    System.out.print(" ");
                }
                if(currentRow.get(c).start() && currentRow.get(c).finish())
                {
                    System.out.print(" SF  ");
                }
                else if (currentRow.get(c).start() && !currentRow.get(c).finish())
                {
                    System.out.print("  S  ");
                }
                else if (!currentRow.get(c).start() && currentRow.get(c).finish())
                {
                    System.out.print("  F  ");
                }
                else if (solution.contains(currentRow.get(c))) {
                    System.out.print("  *  ");
                }
                else
                {
                    System.out.print("     ");
                }
            }
            System.out.println("|");
            
            // prints out the fourth line of the square
            for (int c=0; c<currentRow.size(); c++)
            {
                if (currentRow.get(c).hasLeftWall())
                {
                    System.out.print("|     "); 
                }
                else
                {
                    System.out.print("      ");
                }
            }
            System.out.println("|");
        
        // prints out bottom wall of the maze
        if (r == rowList.size()-1)
        {        
            for (int c=0; c<currentRow.size(); c++)
            {
                System.out.print("+");
                if (currentRow.get(c).hasBottomWall())
                {
                    System.out.print("-----");
                }
            }
            System.out.println("+");
        }
        }
    }


    // This main program acts as a simple unit test for the
    // load-from-file and print-to-System.out Maze capabilities.
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.err.println("Usage: java Maze mazeFile");
            System.exit(1);
        }

        Maze maze = new Maze();
        maze.load(args[0]);
        maze.print();
    }
}