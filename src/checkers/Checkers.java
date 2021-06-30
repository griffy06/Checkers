package checkers;

import java.awt.*;
import java.awt.event.*;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;

class State{
    static int[][] board;
    public int turn;
    public int height;
    State(int[][] board, int turn, int height)
    {
        this.board=board;
        this.turn=turn;
        this.height=height;
    }
}

class solve{

    public int cutoff1=4;
    public int cutoff2=2;
    
    public static int[][] getArray(int[][] arr)
    {
        int[][] b=new int[8][8];
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
                b[i][j]=arr[i][j];
        }
        return b;
    }

    public int findHeuristic(State s)
    {
        int pawns1=0;
        int kings1=0;
        int pawns2=0;
        int kings2=0;
        int pos1=0;
        int pos2=0;
        int center1=0;
        int center2=0;
        int capture1=0;
        int capture2=0;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(s.board[i][j]==1)
                {
                    pawns1++;
                    pos1+=i;
                    if(i-1>=0 && j-1>=0 && i+1<8 && j+1<8 && s.board[i+1][j+1]==0 && (s.board[i-1][j-1]==-1 || s.board[i-1][j-1]==-2))
                        capture1++;
                    else if(i-1>=0 && j+1<8 && i+1<8 && j-1>=0 && s.board[i+1][j-1]==0 && (s.board[i-1][j+1]==-1 || s.board[i-1][j+1]==-2))
                        capture1++;
                    else if(i+1<8 && j-1>=0 && i-1>=0 && j+1<8 && s.board[i-1][j+1]==0 && s.board[i+1][j-1]==-2)
                        capture1++;
                    else if(i+1<8 && j+1<8 && i-1>=0 && j-1>=0 && s.board[i-1][j-1]==0 && s.board[i+1][j+1]==-2)
                        capture1++;
                    if((i==3 || i==4) && j>1 && j<6)
                        center1++;
                        
                }
                else if(s.board[i][j]==2)
                {
                    kings1++;
                    pos1+=i;
                    if(i-1>=0 && j-1>=0 && i+1<8 && j+1<8 && s.board[i+1][j+1]==0 && (s.board[i-1][j-1]==-1 || s.board[i-1][j-1]==-2))
                        capture1+=1;
                    else if(i-1>=0 && j+1<8 && i+1<8 && j-1>=0 && s.board[i+1][j-1]==0 && (s.board[i-1][j+1]==-1 || s.board[i-1][j+1]==-2))
                        capture1+=1;
                    else if(i+1<8 && j-1>=0 && i-1>=0 && j+1<8 && s.board[i-1][j+1]==0 && s.board[i+1][j-1]==-2)
                        capture1+=1;
                    else if(i+1<8 && j+1<8 && i-1>=0 && j-1>=0 && s.board[i-1][j-1]==0 && s.board[i+1][j+1]==-2)
                        capture1+=1;
                    if((i==3 || i==4) && j>1 && j<6)
                        center1++;
                }
                else if(s.board[i][j]==-1)
                {
                    pawns2++;
                    pos2+=7-i;
                    if(i+1<8 && j+1<8 && i-1>=0 && j-1>=0 && s.board[i-1][j-1]==0 && (s.board[i+1][j+1]==1 || s.board[i+1][j+1]==2))
                        capture2++;
                    else if(i+1<8 && j-1>=0 && i-1>=0 && j+1<8 && s.board[i-1][j+1]==0 && (s.board[i+1][j-1]==1 || s.board[i+1][j-1]==2))
                        capture2++;
                    else if(i-1>=0 && j+1<8 && i+1<8 && j-1>=0 && s.board[i+1][j-1]==0 && s.board[i-1][j+1]==2)
                        capture2++;
                    else if(i-1>=0 && j-1>=0 && i+1<8 && j+1<8 && s.board[i+1][j+1]==0 && s.board[i-1][j-1]==2)
                        capture2++;
                    if((i==3 || i==4) && j>1 && j<6)
                        center2++;
                }
                else if(s.board[i][j]==-2)
                {
                    kings2++;
                    pos2+=7-i;
                    if(i+1<8 && j+1<8 && i-1>=0 && j-1>=0 && s.board[i-1][j-1]==0 && (s.board[i+1][j+1]==1 || s.board[i+1][j+1]==2))
                        capture2+=1;
                    else if(i+1<8 && j-1>=0 && i-1>=0 && j+1<8 && s.board[i-1][j+1]==0 && (s.board[i+1][j-1]==1 || s.board[i+1][j-1]==2))
                        capture2+=1;
                    else if(i-1>=0 && j+1<8 && i+1<8 && j-1>=0 && s.board[i+1][j-1]==0 && s.board[i-1][j+1]==2)
                        capture2+=1;
                    else if(i-1>=0 && j-1>=0 && i+1<8 && j+1<8 && s.board[i+1][j+1]==0 && s.board[i-1][j-1]==2)
                        capture2+=1;
                    if((i==3 || i==4) && j>1 && j<6)
                        center2++;
                }
            }
        }
        if(Checkers.x==1){
            int h=5*pawns1+8*kings1+4*pos1+2*center1-15*capture1-5*pawns2-8*kings2-4*pos2-2*center2+15*capture2;
            return h;
        }
        else
        {
            int h=5*pawns1+8*kings1+4*pos1+2*center1-5*capture1-5*pawns2-8*kings2-4*pos2-2*center2+5*capture2;
            return h;
        }
    }

    int findMaxValue(State s, boolean apply)
    {
        if(s.height>cutoff1 && apply==false)
        {
            return findHeuristic(s);
        }
        int maxcost=Integer.MIN_VALUE;
        int[][] maxboard=getArray(s.board);
        int[][] board=getArray(s.board);
        int h=s.height+1;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(board[i][j]==1)
                {
                    int[][] boardNew = getArray(board);
                    if((i-2>=0 && j-2>=0 && board[i-2][j-2]==0 && (board[i-1][j-1]==-2 || board[i-1][j-1]==-1)) || (i-2>=0 && j+2<8 && board[i-2][j+2]==0 && (board[i-1][j+1]==-2 || board[i-1][j+1]==-1)))
                    {
                        while((i-2>=0 && j-2>=0 && board[i-2][j-2]==0 && (board[i-1][j-1]==-2 || board[i-1][j-1]==-1)) || (i-2>=0 && j+2<8 && board[i-2][j+2]==0 && (board[i-1][j+1]==-2 || board[i-1][j+1]==-1)))
                        {
                            if(i-2>=0 && j-2>=0 && board[i-2][j-2]==0 && (board[i-1][j-1]==-2 || board[i-1][j-1]==-1))
                            {
                                boardNew[i][j]=0;
                                boardNew[i-1][j-1]=0;
                                if(i-2>0)
                                boardNew[i-2][j-2]=1;
                                else
                                boardNew[i-2][j-2]=2;
                                i=i-2;
                                j=j-2;
                            }
                            else if(i-2>=0 && j+2<8 && board[i-2][j+2]==0 && (board[i-1][j+1]==-2 || board[i-1][j+1]==-1))
                            {
                                boardNew[i][j]=0;
                                boardNew[i-1][j+1]=0;
                                if(i-2>0)
                                boardNew[i-2][j+2]=1;
                                else
                                boardNew[i-2][j+2]=2;
                                i=i-2;
                                j=j+2;
                            }
                        }
                        if(apply==true)
                        {
                            s.board=getArray(boardNew);
                            return -1;
                        }
                        State sNew=new State(boardNew,2,h);
                        int cost=findMinValue(sNew,false);
                        return cost;
                    }
                    if(i-1>=0 && j-1>=0 && board[i-1][j-1]==0)
                    {
                        boardNew[i][j]=0;
                        if(i-1>0)
                        boardNew[i-1][j-1]=1;
                        else
                        boardNew[i-1][j-1]=2;
                        State sNew=new State(boardNew,2,h);
                        int cost=findMinValue(sNew,false);
                        if(cost>maxcost){
                        maxcost=cost;
                        maxboard=getArray(boardNew);
                        }
                        
                    }
                    boardNew=getArray(board);
                    if(i-1>=0 && j+1<8 && board[i-1][j+1]==0)
                    {
                        boardNew[i][j]=0;
                        if(i-1>0)
                        boardNew[i-1][j+1]=1;
                        else
                        boardNew[i-1][j+1]=2;
                        State sNew=new State(boardNew,2,h);
                        int cost=findMinValue(sNew,false);
                        if(cost>maxcost){
                        maxcost=cost;
                        maxboard=getArray(boardNew);
                        }
                    }
                }
                else if(board[i][j]==2)
                {
                    int[][] boardNew = getArray(board);
                    if((i-2>=0 && j-2>=0 && board[i-2][j-2]==0 && (board[i-1][j-1]==-2 || board[i-1][j-1]==-1)) || (i-2>=0 && j+2<8 && board[i-2][j+2]==0 && (board[i-1][j+1]==-2 || board[i-1][j+1]==-1)) || (i+2<8 && j-2>=0 && board[i+2][j-2]==0 && (board[i+1][j-1]==-2 || board[i+1][j-1]==-1)) || (i+2<8 && j+2<8 && board[i+2][j+2]==0 && (board[i+1][j+1]==-2 || board[i+1][j+1]==-1)))
                    {
                        while((i-2>=0 && j-2>=0 && board[i-2][j-2]==0 && (board[i-1][j-1]==-2 || board[i-1][j-1]==-1)) || (i-2>=0 && j+2<8 && board[i-2][j+2]==0 && (board[i-1][j+1]==-2 || board[i-1][j+1]==-1)) || (i+2<8 && j-2>=0 && board[i+2][j-2]==0 && (board[i+1][j-1]==-2 || board[i+1][j-1]==-1)) || (i+2<8 && j+2<8 && board[i+2][j+2]==0 && (board[i+1][j+1]==-2 || board[i+1][j+1]==-1)))
                        {
                            if(i-2>=0 && j-2>=0 && board[i-2][j-2]==0 && (board[i-1][j-1]==-2 || board[i-1][j-1]==-1))
                            {
                                boardNew[i][j]=0;
                                boardNew[i-1][j-1]=0;
                                boardNew[i-2][j-2]=2;
                                i=i-2;
                                j=j-2;
                            }
                            else if(i-2>=0 && j+2<8 && board[i-2][j+2]==0 && (board[i-1][j+1]==-2 || board[i-1][j+1]==-1))
                            {
                                boardNew[i][j]=0;
                                boardNew[i-1][j+1]=0;
                                boardNew[i-2][j+2]=2;
                                i=i-2;
                                j=j+2;
                            }
                            else if(i+2<8 && j+2<8 && board[i+2][j+2]==0 && (board[i+1][j+1]==-2 || board[i+1][j+1]==-1))
                            {
                                boardNew[i][j]=0;
                                boardNew[i+1][j+1]=0;
                                boardNew[i+2][j+2]=2;
                                i=i+2;
                                j=j+2;
                            }
                            else if(i+2<8 && j-2>=0 && board[i+2][j-2]==0 && (board[i+1][j-1]==-2 || board[i+1][j-1]==-1))
                            {
                                boardNew[i][j]=0;
                                boardNew[i+1][j-1]=0;
                                boardNew[i+2][j-2]=2;
                                i=i+2;
                                j=j-2;
                            }
                        }
                        State sNew=new State(boardNew,2,h);
                        if(apply==true)
                        {
                            s.board=getArray(boardNew);
                            return -1;
                        }
                        int cost=findMinValue(sNew,false);
                        return cost;
                    }
                    if(i-1>=0 && j-1>=0 && board[i-1][j-1]==0)
                    {
                        boardNew[i][j]=0;
                        boardNew[i-1][j-1]=2;
                        State sNew=new State(boardNew,2,h);
                        int cost=findMinValue(sNew,false);
                        if(cost>maxcost){
                        maxcost=cost;
                        maxboard=getArray(boardNew);
                        }
                    }
                    boardNew=getArray(board);
                    if(i-1>=0 && j+1<8 && board[i-1][j+1]==0)
                    {
                        boardNew[i][j]=0;
                        boardNew[i-1][j+1]=2;
                        State sNew=new State(boardNew,2,h);
                        int cost=findMinValue(sNew,false);
                        if(cost>maxcost){
                        maxcost=cost;
                        maxboard=getArray(boardNew);
                        }
                    }
                    boardNew=getArray(board);
                    if(i+1<8 && j-1>=0 && board[i+1][j-1]==0)
                    {
                        boardNew[i][j]=0;
                        boardNew[i+1][j-1]=2;
                        State sNew=new State(boardNew,2,h);
                        int cost=findMinValue(sNew,false);
                        if(cost>maxcost){
                        maxcost=cost;
                        maxboard=getArray(boardNew);
                        }
                    }
                    boardNew=getArray(board);
                    if(i+1<8 && j+1<8 && board[i+1][j+1]==0)
                    {
                        boardNew[i][j]=0;
                        boardNew[i+1][j+1]=2;
                        State sNew=new State(boardNew,2,h);
                        int cost=findMinValue(sNew,false);
                        if(cost>maxcost){
                        maxcost=cost;
                        maxboard=getArray(boardNew);
                        }
                    }
                }

            }
        }
        if(apply==true)
        {
            s.board=getArray(maxboard);
            return -1;
        }
        return maxcost;
    }

    int findMinValue(State s, boolean apply)
    {
        if(s.height>cutoff2 && apply==false)
        {
            return findHeuristic(s);
        }
        int mincost=Integer.MAX_VALUE;
        int[][] minboard=getArray(s.board);
        int[][] board=getArray(s.board);
        int h=s.height+1;
        for(int i=7;i>=0;i--)
        {
            for(int j=7;j>=0;j--)
            {
                if(board[i][j]==-1)
                {
                    int[][] boardNew = getArray(board);
                    if((i+2<8 && j+2<8 && board[i+2][j+2]==0 && (board[i+1][j+1]==2 || board[i+1][j+1]==1)) || (i+2<8 && j-2>=0 && board[i+2][j-2]==0 && (board[i+1][j-1]==2 || board[i+1][j-1]==1)))
                    {
                        while((i+2<8 && j+2<8 && board[i+2][j+2]==0 && (board[i+1][j+1]==2 || board[i+1][j+1]==1)) || (i+2<8 && j-2>=0 && board[i+2][j-2]==0 && (board[i+1][j-1]==2 || board[i+1][j-1]==1)))
                        {
                            if(i+2<8 && j+2<8 && board[i+2][j+2]==0 && (board[i+1][j+1]==2 || board[i+1][j+1]==1))
                            {
                                boardNew[i][j]=0;
                                boardNew[i+1][j+1]=0;
                                if(i+2<7)
                                boardNew[i+2][j+2]=-1;
                                else
                                boardNew[i+2][j+2]=-2;
                                i=i+2;
                                j=j+2;
                            }
                            else if(i+2<8 && j-2>=0 && board[i+2][j-2]==0 && (board[i+1][j-1]==2 || board[i+1][j-1]==1))
                            {
                                boardNew[i][j]=0;
                                boardNew[i+1][j-1]=0;
                                if(i+2<7)
                                boardNew[i+2][j-2]=-1;
                                else
                                boardNew[i+2][j-2]=-2;
                                i=i+2;
                                j=j-2;
                            }
                        }
                        if(apply==true)
                        {
                            s.board=getArray(boardNew);
                            return -1;
                        }
                        State sNew=new State(boardNew,1,h);
                        int cost=findMaxValue(sNew,false);
                        return cost;
                    }
                    if(i+1<8 && j+1<8 && board[i+1][j+1]==0)
                    {
                        boardNew[i][j]=0;
                        if(i+1<7)
                        boardNew[i+1][j+1]=-1;
                        else
                        boardNew[i+1][j+1]=-2;
                        State sNew=new State(boardNew,1,h);
                        int cost=findMaxValue(sNew,false);
                        if(cost<mincost){
                        mincost=cost;
                        minboard=getArray(boardNew);
                        }
                    }
                    boardNew=getArray(board);
                    if(i+1<8 && j-1>=0 && board[i+1][j-1]==0)
                    {
                        boardNew[i][j]=0;
                        if(i+1<7)
                        boardNew[i+1][j-1]=-1;
                        else
                        boardNew[i+1][j-1]=-2;
                        State sNew=new State(boardNew,1,h);
                        int cost=findMaxValue(sNew,false);
                        if(cost<mincost){
                        mincost=cost;
                        minboard=getArray(boardNew);
                        }
                    }
                }
                else if(board[i][j]==-2)
                {
                    int[][] boardNew = getArray(board);
                    if((i+2<8 && j+2<8 && board[i+2][j+2]==0 && (board[i+1][j+1]==2 || board[i+1][j+1]==1)) || (i+2<8 && j-2>=0 && board[i+2][j-2]==0 && (board[i+1][j-1]==2 || board[i+1][j-1]==1)) || (i-2>=0 && j+2<8 && board[i-2][j+2]==0 && (board[i-1][j+1]==2 || board[i-1][j+1]==1) || (i-2>=0 && j-2>=0 && board[i-2][j-2]==0 && (board[i-1][j-1]==2 || board[i-1][j-1]==1))))
                    {
                        while((i+2<8 && j+2<8 && board[i+2][j+2]==0 && (board[i+1][j+1]==2 || board[i+1][j+1]==1)) || (i+2<8 && j-2>=0 && board[i+2][j-2]==0 && (board[i+1][j-1]==2 || board[i+1][j-1]==1)) || (i-2>=0 && j+2<8 && board[i-2][j+2]==0 && (board[i-1][j+1]==2 || board[i-1][j+1]==1) || (i-2>=0 && j-2>=0 && board[i-2][j-2]==0 && (board[i-1][j-1]==2 || board[i-1][j-1]==1))))
                        {
                            if(i+2<8 && j+2<8 && board[i+2][j+2]==0 && (board[i+1][j+1]==2 || board[i+1][j+1]==1))
                            {
                                boardNew[i][j]=0;
                                boardNew[i+1][j+1]=0;
                                boardNew[i+2][j+2]=-2;
                                i=i+2;
                                j=j+2;
                            }
                            else if(i+2<8 && j-2>=0 && board[i+2][j-2]==0 && (board[i+1][j-1]==2 || board[i+1][j-1]==1))
                            {
                                boardNew[i][j]=0;
                                boardNew[i+1][j-1]=0;
                                boardNew[i+2][j-2]=-2;
                                i=i+2;
                                j=j-2;
                            }
                            else if(i-2>=0 && j-2>=0 && board[i-2][j-2]==0 && (board[i-1][j-1]==2 || board[i-1][j-1]==1))
                            {
                                boardNew[i][j]=0;
                                boardNew[i-1][j-1]=0;
                                boardNew[i-2][j-2]=-2;
                                i=i-2;
                                j=j-2;
                            }
                            else if(i-2>=0 && j+2<8 && board[i-2][j+2]==0 && (board[i-1][j+1]==2 || board[i-1][j+1]==1))
                            {
                                boardNew[i][j]=0;
                                boardNew[i-1][j+1]=0;
                                boardNew[i-2][j+2]=-2;
                                i=i-2;
                                j=j+2;
                            }
                        }
                        if(apply==true)
                        {
                            s.board=getArray(boardNew);
                            return -1;
                        }
                        State sNew=new State(boardNew,1,h);
                        int cost=findMaxValue(sNew,false);
                        return cost;
                    }
                    if(i+1<8 && j+1<8 && board[i+1][j+1]==0)
                    {
                        boardNew[i][j]=0;
                        boardNew[i+1][j+1]=-2;
                        State sNew=new State(boardNew,1,h);
                        int cost=findMaxValue(sNew,false);
                        if(cost<mincost){
                        mincost=cost;
                        minboard=getArray(boardNew);
                        }
                    }
                    boardNew=getArray(board);
                    if(i+1<8 && j-1>=0 && board[i+1][j-1]==0)
                    {
                        boardNew[i][j]=0;
                        boardNew[i+1][j-1]=-2;
                        State sNew=new State(boardNew,1,h);
                        int cost=findMaxValue(sNew,false);
                        if(cost<mincost){
                        mincost=cost;
                        minboard=getArray(boardNew);
                        }
                    }
                    boardNew=getArray(board);
                    if(i-1>=0 && j+1<8 && board[i-1][j+1]==0)
                    {
                        boardNew[i][j]=0;
                        boardNew[i-1][j+1]=-2;
                        State sNew=new State(boardNew,1,h);
                        int cost=findMaxValue(sNew,false);
                        if(cost<mincost){
                        mincost=cost;
                        minboard=getArray(boardNew);
                        }
                    }
                    boardNew=getArray(board);
                    if(i-1>=0 && j-1>=0 && board[i-1][j-1]==0)
                    {
                        boardNew[i][j]=0;
                        boardNew[i-1][j-1]=-2;
                        State sNew=new State(boardNew,1,h);
                        int cost=findMaxValue(sNew,false);
                        if(cost<mincost){
                        mincost=cost;
                        minboard=getArray(boardNew);
                        }
                    }
                }

            }
        }
        if(apply==true)
        {
            s.board=getArray(minboard);
            return -1;
        }
        return mincost;
    }

    void findBestMove(State s)
    {
        if(s.turn==1)
        {
            findMaxValue(s, true);
        }
        else
        {
            findMinValue(s, true);
        }
        if(s.turn==1)
        s.turn=2;
        else
        s.turn=1;
        s.height+=1;
    }
}

class Panel extends JPanel
{
        public void paintComponent(Graphics g)
        {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.BLACK);
            int k=70;
            for(int i=0;i<8*k;i+=k)
            {
                if(i%(2*k)==0){
                    for(int j=k;j<8*k;j+=2*k)
                    {
                        g.fillRect(7*k-j,7*k-i,k,k);
                    }
                    for(int j=0;j<8*k;j+=2*k)
                    {
                        g.drawRect(7*k-j, 7*k-i, k, k);
                    }
                }
                else
                {
                    for(int j=0;j<8*k;j+=2*k)
                    {
                        g.fillRect(7*k-j,7*k-i,k,k);
                    }
                    for(int j=k;j<8*k;j+=2*k)
                    {
                        g.drawRect(7*k-j, 7*k-i, k, k);
                    }
                }
            }
            if(Checkers.x==1){
                g.setColor(Color.GRAY);
                if(Checkers.turn==1){
                g.drawString("Computer's", 2, 30);
                g.drawString("turn!", 15, 47);
                }
                else{
                g.drawString("Your", 508, 520);
                g.drawString("turn!", 509, 535);}
            }
            else if(Checkers.x==2){
                g.setColor(Color.GRAY);
                if(Checkers.turn==1){
                g.drawString("Player1's", 2, 30);
                g.drawString("turn!", 15, 47);
                }
                else{
                g.drawString("Player2's", 508, 520);
                g.drawString("turn!", 509, 535);}
            }
            boolean player1=false;
            boolean player2=false;
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    if(Checkers.checkersBoard[i][j]==1)
                    {
                        player1=true;
                        g.setColor(Color.RED);
                        g.fillOval(7+((7-j)*560)/8, 4+((7-i)*570)/8, 55, 55);
                    }
                    else if(Checkers.checkersBoard[i][j]==2)
                    {
                        player1=true;
                        g.setColor(Color.PINK);
                        g.fillOval(7+((7-j)*560)/8, ((7-i)*570)/8, 55, 55);
                    }
                    else if(Checkers.checkersBoard[i][j]==-1)
                    {
                        player2=true;
                        g.setColor(Color.BLUE);
                        g.fillOval(7+((7-j)*560)/8, 4+((7-i)*570)/8, 55, 55);
                    }
                    else if(Checkers.checkersBoard[i][j]==-2)
                    {
                        player2=true;
                        g.setColor(Color.GREEN);
                        g.fillOval(7+((7-j)*560)/8, 4+((7-i)*570)/8, 55, 55);
                    }
                }
            }
            if(!player1){
                if(Checkers.x==1){
                showMessageDialog(null, "You won!");
                }
                else
                    showMessageDialog(null, "Player2 won!");
                System.exit(0);
            }
            if(!player2){
                if(Checkers.x==1){
                showMessageDialog(null, "Computer won!");
                }
                else
                {
                    showMessageDialog(null, "Player2 won!");
                }
                System.exit(0);
            }
        }
}

class Click extends Frame implements MouseListener{
    
    
    public void mousePressed(MouseEvent e) 
    { 
        double k1=(e.getX()*8)/572;
        if(Checkers.turn==2){
        human.y1=7-(int)Math.round(k1);
        }
        else{
        human2.y1=7-(int)Math.round(k1);
        }
        double k2=(e.getY()*8)/572;
        if(Checkers.turn==2){
        human.x1=7-(int)Math.round(k2);
        }
        else
        {
            human2.x1=7-(int)Math.round(k2);
        }
        System.out.println("prssed at "+human.x1+" "+human.y1);
    }

    public void mouseReleased(MouseEvent e) 
    { 
        double k1=(e.getX()*8)/572;
        if(Checkers.turn==2){
        human.y2=7-(int)Math.round(k1);
        }
        else
        {
            human2.y2=7-(int)Math.round(k1);
        }
        double k2=((e.getY())*8)/572;
        if(Checkers.turn==2){
        human.x2=7-(int)Math.round(k2);
        }
        else
        {
            human2.x2=7-(int)Math.round(k2);
        }
        System.out.println("released at "+human.x2+" "+human.y2);
        Checkers.pressed=true;
        System.out.println(Checkers.pressed);
    } 

    public void mouseExited(MouseEvent e){}
    
    public void mouseEntered(MouseEvent e){}
    
    public void mouseClicked(MouseEvent e){}
}

class human{
    static int x1=-1;
    static int y1=-1;
    static int x2=-1;
    static int y2=-1;
    static int move(State s)
    {
        boolean g;
        PrintStream original = System.out;
        PrintStream created = new PrintStream(new OutputStream(){
        public void write(int x) {
           }
        });
        while(!Checkers.pressed){
            System.setOut(created);
            System.out.println(Checkers.pressed);
        }
        if(x1==-1 || y1==-1 || x2==-1 || y2==-1)
        {
            Checkers.pressed=false;
            return move(s);
        }
        int k=s.board[x1][y1];
        if(k!=-1 && k!=-2)
        {
            showMessageDialog(null, "Either the move is not allowed or the mouse could not detect your move properly. Try again.");
            Checkers.pressed=false;
            return move(s);
        }
        boolean flag=false;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(s.board[i][j]==-1 && i+2<8 && ((j+2<8 && i+2<8 && s.board[i+2][j+2]==0 && (s.board[i+1][j+1]==1 || s.board[i+1][j+1]==2)) || (j-2>=0 && i+2<8 && s.board[i+2][j-2]==0 && (s.board[i+1][j-1]==1 || s.board[i+1][j-1]==2)))){
                    flag=true;
                    break;
                }
                if(s.board[i][j]==-2 && (i+2<8 && ((j+2<8 && i+2<8 && s.board[i+2][j+2]==0 && (s.board[i+1][j+1]==1 || s.board[i+1][j+1]==2)) || (j-2>=0 && i+2<8 && s.board[i+2][j-2]==0 && (s.board[i+1][j-1]==1 || s.board[i+1][j-1]==2))) || (i-2>=0 && (j+2<8 && i-2>=0 && s.board[i-2][j+2]==0 && (s.board[i-1][j+1]==1 || s.board[i-1][j+1]==2)) || (j+2<8 && i-2>=0 && s.board[i-2][j+2]==0 && (s.board[i-1][j+1]==1 || s.board[i-1][j+1]==2))))){
                    flag=true;
                    break;
                }
            }
        }
        if(k==-1)
        {
            
            System.setOut(original);
            if(flag==true)
            {
                if(!(x2==x1+2 && Math.abs(y2-y1)==2 && s.board[x2][y2]==0 && (s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==1 || s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==2)))
                {
                    showMessageDialog(null, "Capture the opponent's piece!");
                    Checkers.pressed=false;
                    return move(s);
                }
            }
            if(!((s.board[x2][y2]==0 && x2==x1+1 && Math.abs(y2-y1)==1) || (x2==x1+2 && Math.abs(y2-y1)==2 && s.board[x2][y2]==0 && (s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==1 || s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==2)) ))
            {
                showMessageDialog(null, "Either the move is not allowed or the mouse could not detect your move properly. Try again.");
                Checkers.pressed=false;
                move(s);
            }
        }
        else if(k==-2)
        {
            if(flag==true)
            {
                if(!(Math.abs(x2-x1)==2 && Math.abs(y2-y1)==2 && s.board[x2][y2]==0 && (s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==1 || s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==2)))
                {
                    showMessageDialog(null, "Capture the opponent's piece!");
                    Checkers.pressed=false;
                    return move(s);
                }
            }
            if(!((s.board[x2][y2]==0 && Math.abs(x1-x2)==1 && Math.abs(y2-y1)==1) || (Math.abs(x1-x2)==2 && Math.abs(y2-y1)==2 && s.board[x2][y2]==0 && (s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==1 || s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==2)) ))
            {
                showMessageDialog(null, "Either the move is not allowed or the mouse could not detect your move properly. Try again.");
                Checkers.pressed=false;
                return move(s);
            }
        }
        s.board[x1][y1]=0;
        boolean again=false;
        if(Math.abs(x1-x2)==2)
        {
            if(x1<x2)
            {
                if(y1<y2)
                    s.board[Math.abs(x1+1)][Math.abs(y1+1)]=0;
                else
                    s.board[Math.abs(x1+1)][Math.abs(y2+1)]=0;
            }
            else
            {
                if(y1<y2)
                    s.board[Math.abs(x2+1)][Math.abs(y1+1)]=0;
                else
                    s.board[Math.abs(x2+1)][Math.abs(y2+1)]=0;
            }
            again=true;
        }
        if(k==-1){
        if(x2<7)
        s.board[x2][y2]=-1;
        else
            s.board[x2][y2]=-2;
        }   
        else if(k==-2)
        {
            s.board[x2][y2]=-2;
        }
        s.height+=1;
        
        if(again)
        {
            boolean flag2=false;
            if(s.board[x2][y2]==-1)
            {
                if(x2+2<8 && y2+2<8 && s.board[x2+2][y2+2]==0 && (s.board[x2+1][y2+1]==1 || s.board[x2+1][y2+1]==2))
                    flag2=true;
                if(x2+2<8 && y2-2>=0 && s.board[x2+2][y2-2]==0 && (s.board[x2+1][y2-1]==1 || s.board[x2+1][y2-1]==2))
                    flag2=true;
            }
            
            else if(s.board[x2][y2]==-2)
            {
                if(x2+2<8 && y2+2<8 && s.board[x2+2][y2+2]==0 && (s.board[x2+1][y2+1]==1 || s.board[x2+1][y2+1]==2))
                    flag2=true;
                if(x2+2<8 && y2-2>=0 && s.board[x2+2][y2-2]==0 && (s.board[x2+1][y2-1]==1 || s.board[x2+1][y2-1]==2))
                    flag2=true;
                if(x2-2>=0 && y2+2<8 && s.board[x2-2][y2+2]==0 && (s.board[x2-1][y2+1]==1 || s.board[x2-1][y2+1]==2))
                    flag2=true;
                if(x2-2>=0 && y2-2>=0 && s.board[x2-2][y2-2]==0 && (s.board[x2-1][y2-1]==1 || s.board[x2-1][y2-1]==2))
                    flag2=true;
            }
            if(flag2)
            {
                Checkers.checkersBoard=solve.getArray(s.board);
                Checkers.jframe.repaint();
                Checkers.pressed=false;
                if(Checkers.x==1)
                showMessageDialog(null, "You get an extra jump!");
                else
                    showMessageDialog(null, "Player2 gets an extra jump!");
                return move(s);
            }
        }
        s.turn=1;
        Checkers.pressed=false;
        for(int i=7;i>=0;i--)
            {
                for(int j=7;j>=0;j--)
                {
                    System.out.print(s.board[i][j]+" ");
                }
                System.out.println();
            }
        return 0;
    }
    
}


class human2{
    static int x1=-1;
    static int y1=-1;
    static int x2=-1;
    static int y2=-1;
    static int move(State s) throws Exception
    {
        boolean g;
        PrintStream original = System.out;
        PrintStream created = new PrintStream(new OutputStream(){
        public void write(int x) {
           }
        });
        while(!Checkers.pressed){
            System.setOut(created);
            System.out.println("human "+Checkers.pressed);
        }
        if(x1==-1 || y1==-1 || x2==-1 || y2==-1)
        {
            Checkers.pressed=false;
            return move(s);
        }
        int k=s.board[x1][y1];
        if(k!=1 && k!=2)
        {
            showMessageDialog(null, "Either the move is not allowed or the mouse could not detect your move properly. Try again.");
            Checkers.pressed=false;
            return move(s);
        }
        boolean flag=false;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(s.board[i][j]==1 && i-2>=0 && ((j-2>=0 && i-2>=0 && s.board[i-2][j-2]==0 && (s.board[i-1][j-1]==-1 || s.board[i-1][j-1]==-2)) || (j+2<8 && i-2>=0 && s.board[i-2][j+2]==0 && (s.board[i-1][j+1]==-1 || s.board[i-1][j+1]==-2)))){
                    flag=true;
                    break;
                }
                if(s.board[i][j]==2 && (i-2>=0 && ((j-2>=0 && i-2>=0 && s.board[i-2][j-2]==0 && (s.board[i-1][j-1]==-1 || s.board[i-1][j-1]==-2)) || (j+2<8 && i-2>=0 && s.board[i-2][j+2]==0 && (s.board[i-1][j+1]==-1 || s.board[i-1][j+1]==-2))) || (i-2>=0 && (j+2<8 && i-2>=0 && s.board[i-2][j+2]==0 && (s.board[i-1][j+1]==1 || s.board[i-1][j+1]==2)) || (j+2<8 && i-2>=0 && s.board[i-2][j+2]==0 && (s.board[i-1][j+1]==1 || s.board[i-1][j+1]==2))))){
                    flag=true;
                    break;
                }
            }
        }
        if(k==1)
        {
            
            System.setOut(original);
            if(flag==true)
            {
                if(!(x2==x1-2 && Math.abs(y2-y1)==2 && s.board[x2][y2]==0 && (s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==-1 || s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==-2)))
                {
                    showMessageDialog(null, "Capture the opponent's piece!");
                    Checkers.pressed=false;
                    return move(s);
                }
            }
            if(!((s.board[x2][y2]==0 && x2==x1-1 && Math.abs(y2-y1)==1) || (x2==x1-2 && Math.abs(y2-y1)==2 && s.board[x2][y2]==0 && (s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==-1 || s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==-2)) ))
            {
                showMessageDialog(null, "Either the move is not allowed or the mouse could not detect your move properly. Try again.");
                Checkers.pressed=false;
                move(s);
            }
        }
        else if(k==2)
        {
            if(flag==true)
            {
                if(!(Math.abs(x2-x1)==2 && Math.abs(y2-y1)==2 && s.board[x2][y2]==0 && (s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==-1 || s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==-2)))
                {
                    showMessageDialog(null, "Capture the opponent's piece!");
                    Checkers.pressed=false;
                    return move(s);
                }
            }
            if(!((s.board[x2][y2]==0 && Math.abs(x1-x2)==1 && Math.abs(y2-y1)==1) || (Math.abs(x1-x2)==2 && Math.abs(y2-y1)==2 && s.board[x2][y2]==0 && (s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==-1 || s.board[(int)((x1+x2)/2)][(int)((y1+y2)/2)]==-2)) ))
            {
                showMessageDialog(null, "Either the move is not allowed or the mouse could not detect your move properly. Try again.");
                Checkers.pressed=false;
                return move(s);
            }
        }
        s.board[x1][y1]=0;
        boolean again=false;
        if(Math.abs(x1-x2)==2)
        {
            if(x1<x2)
            {
                if(y1<y2)
                    s.board[Math.abs(x1+1)][Math.abs(y1+1)]=0;
                else
                    s.board[Math.abs(x1+1)][Math.abs(y2+1)]=0;
            }
            else
            {
                if(y1<y2)
                    s.board[Math.abs(x2+1)][Math.abs(y1+1)]=0;
                else
                    s.board[Math.abs(x2+1)][Math.abs(y2+1)]=0;
            }
            again=true;
        }
        if(k==1){
        if(x2>0)
        s.board[x2][y2]=1;
        else
            s.board[x2][y2]=2;
        }   
        else if(k==2)
        {
            s.board[x2][y2]=2;
        }
        s.height+=1;
        
        if(again)
        {
            boolean flag2=false;
            if(s.board[x2][y2]==1)
            {
                if(x2-2>=0 && y2-2>=0 && s.board[x2-2][y2-2]==0 && (s.board[x2-1][y2-1]==-1 || s.board[x2-1][y2-1]==-2))
                    flag2=true;
                if(x2-2>=0 && y2+2<8 && s.board[x2-2][y2+2]==0 && (s.board[x2-1][y2+1]==-1 || s.board[x2-1][y2+1]==-2))
                    flag2=true;
            }
            
            else if(s.board[x2][y2]==2)
            {
                if(x2-2>=0 && y2-2>=0 && s.board[x2-2][y2-2]==0 && (s.board[x2-1][y2-1]==-1 || s.board[x2-1][y2-1]==-2))
                    flag2=true;
                if(x2-2>=0 && y2+2<8 && s.board[x2-2][y2+2]==0 && (s.board[x2-1][y2+1]==-1 || s.board[x2-1][y2+1]==-2))
                    flag2=true;
                if(x2+2<8 && y2-2>=0 && s.board[x2+2][y2-2]==0 && (s.board[x2+1][y2-1]==-1 || s.board[x2+1][y2-1]==-2))
                    flag2=true;
                if(x2+2<8 && y2+2<8 && s.board[x2+2][y2+2]==0 && (s.board[x2+1][y2+1]==-1 || s.board[x2+1][y2+1]==-2))
                    flag2=true;
            }
            if(flag2)
            {
                Checkers.checkersBoard=solve.getArray(s.board);
                Checkers.jframe.repaint();
                Checkers.pressed=false;
                showMessageDialog(null, "Player1 gets an extra jump!");
                return move(s);
            }
        }
        s.turn=2;
        Checkers.pressed=false;
        for(int i=7;i>=0;i--)
            {
                for(int j=7;j>=0;j--)
                {
                    System.out.print(s.board[i][j]+" ");
                }
                System.out.println();
            }
        return 0;
    }
    
}



public class Checkers{
    
    static JFrame jframe;
    static Panel panel;
    static int[][] checkersBoard;
    static boolean pressed=false;
    static int turn=1;
    static int x;
    
    public static boolean compare(int[][] board1, int[][] board2)
    {
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(board1[i][j]!=board2[i][j])
                    return false;
            }
        }
        return true;
    }
    
    public static boolean findMarker(int[][] board, int player)
    {
        if(player==1)
        {
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    if(board[i][j]==1 || board[i][j]==2)
                        return true;
                }
            }
            return false;
        }
        else
        {
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    if(board[i][j]==-1 || board[i][j]==-2)
                        return true;
                }
            }
            return false;
        }
    }
    public static void main(String[] args) throws Exception
    {
        int[][] board=new int[8][8];
        String[] options = {"Computer vs Computer", "Human vs Computer", "Human vs Human"};
        x = JOptionPane.showOptionDialog(null, "Use drag and drop to play. Choose the players","Click a button",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if(x==0)
            System.out.println("You chose Computer vs Computer!");
        else if(x==1)
            System.out.println("You chose Human vs Computer!");
        else
            System.out.println("You chose Human vs Human!");
        for(int i=0;i<8;i++)
        {
            if(i%2==0)
            {
                for(int j=1;j<8;j+=2)
                {
                    if(i<3)
                    {
                        board[i][j]=-1;
                    }
                    else if(i>4)
                    {
                        board[i][j]=1;
                    }
                    else
                    {
                        board[i][j]=0;
                    }
                }
            }
            else{
                for(int j=0;j<8;j+=2)
                {
                    if(i<3)
                    {
                        board[i][j]=-1;
                    }
                    else if(i>4)
                    {
                        board[i][j]=1;
                    }
                    else
                    {
                        board[i][j]=0;
                    }
                }
            }
        }
        solve solver=new solve();
        int[][] prevBoard=solve.getArray(board);
        for(int i=7;i>=0;i--)
        {
            for(int j=0;j<8;j++)
            {
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
        State s=new State(board,1,0);
        checkersBoard=solve.getArray(s.board);
        jframe = new JFrame("Checkers");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Click m=new Click();
        panel = new Panel();
        jframe.addMouseListener(m);  
        jframe.getContentPane().add(BorderLayout.CENTER, panel);
        jframe.setSize(572, 598);
        jframe.setLocationRelativeTo(null);
        jframe.setResizable(true);
        jframe.setVisible(true);
        while(true)
        {
            if(x==2){
                if(s.turn==1)
                human2.move(s);
                else{
                    human.move(s);
                }
            }
            else if(x==1){
                if(s.turn==1)
                solver.findBestMove(s);
                else{
                    human.move(s);
                }
            }
            else if(x==0)
            {
                solver.findBestMove(s);
            }
            if(compare(prevBoard, s.board))
            {
                if(s.turn==1)
                showMessageDialog(null, "No more moves possible for player 2."+ "The winner is player "+s.turn);
                else
                  showMessageDialog(null, "No more moves possible for player 1."+ "The winner is player "+s.turn);  
                System.out.println("The winner is player "+s.turn);
                break;
            }
            if(x==0)
            Thread.sleep(1200);
            else if(x==1)
            Thread.sleep(800);
            checkersBoard=solve.getArray(s.board);
            turn=s.turn;
            Checkers.jframe.repaint();
            prevBoard=solve.getArray(checkersBoard);
        }
    }
}