package com.graph.MST;

import java.util.Stack;

public class EdgeWeightedGraph 
{
    private static final String NEWLINE = System.getProperty("line.separtor");
    private int V;
    private int E;
    private Bag<Edge>[] adj;
    
    public EdgeWeightedGraph(int V)
    {
        if(V<0) throw new IllegalArgumentException("Number of Vertices must be negative");
        this.V = V;
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        
        for(int i=0;i<V;i++)
        {
            adj[i] = new Bag<>();
        }
    }
    
    public EdgeWeightedGraph(int V,int E)
    {
        this(V);
        if(V < 0)throw new IllegalArgumentException();
        
        for(int i=0;i<E;i++)
        {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            double weight = Math.round(100 * StdRandom.uniform()) / 100.0;
            Edge e = new Edge(v,w,weight);
            addEdge(e);
        }
    }
    
    public EdgeWeightedGraph(In in)
    {
        this(in.readInt());
        @SuppressWarnings("LocalVariableHidesMemberVariable")
        int E = in.readInt();
        if(E < 0) throw new IllegalArgumentException("Number of edge must not be negatives ");
        for(int i=0;i<E;i++)
        {
            int v = in.readInt();
            int w = in.readInt();
            double weight = in.readDouble();
            Edge e = new Edge(v, w, weight);
            addEdge(e);
        }
    }
    
    public EdgeWeightedGraph(EdgeWeightedGraph G)
    {
        this(G.V);
        this.E = G.E();
        for(int v=0;v< G.V();v++)
        {
            Stack<Edge> reverse = new Stack<>();
            for(Edge e : G.adj[v])
            {
                reverse.push(e);
            }
            
            for(Edge e : reverse)
            {
                adj[v].add(e);
            }
        }
        
    }
    
    public int V()
    {
        return V;
    }
    
    public int E()
    {
        return E;
        
    }
    
    private void validateVertex(int v)
    {
        if(v < 0 || v >= V)
            throw new IndexOutOfBoundsException("Vertex " + v + " is not between 0 and " + (V-1));
    }
    
    public void addEdge(Edge e)
    {
        int v = e.either();
        int w = e.other(v);
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        adj[v].add(e);
        E++;
    }
    
    public Iterable<Edge> adj(int v)
    {
        validateVertex(v);
        return adj[v];
        
    }
    
    public int degree(int v)
    {
        validateVertex(v);
        return adj[v].size();
    }
    
    public Iterable<Edge> edges()
    {
        Bag<Edge> list = new Bag<>();
        for(int i=0;i<V;i++)
        {
            int selfLoops = 0;
            for(Edge e : adj(i))
            {
                if(e.other(i) > i)
                {
                    list.add(e);
                }
                else if(e.other(i) == i)
                {
                    if(selfLoops % 2 == 0)
                    {
                        list.add(e);
                    }
                }
            }
        }
        return list;
    }
    
    
    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(V).append(" ").append(E).append(NEWLINE);
        for(int v=0;v<V;v++)
        {
            s.append(v).append(": ");
            for(Edge e : adj[v])
            {
                s.append(e).append(" ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}