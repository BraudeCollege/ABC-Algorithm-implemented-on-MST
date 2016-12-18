package com.graph.MST;

import java.util.ArrayList;

public class PrimsEager {

    private static final double FLOATING_POINT_EPSILON = 1E-12;
    boolean marked[];
    ArrayList<ArrayList<Edge>> graph;
    ArrayList<Edge> mst;

    double distTo[];
    int edgeTo[];

    int E, V;
    ArrayList<Double> sumofvertices = new ArrayList<>();
    
    private Edge[] edgesTo;
    private double[] distsTo;
    private boolean[] mark;
    private IndexFibonacciMinPQ<Double> fib;

    public PrimsEager()
    {
        
    }
    
    @SuppressWarnings("UseOfObsoleteCollectionType")
    PrimsEager(int v) {
        V = v;
        graph = new ArrayList<>();
        for (int i = 0; i < v; ++i) {
            graph.add(new ArrayList<>());
        }
        distTo = new double[V];
        edgeTo = new int[V];
        mst = new ArrayList<>();
        marked = new boolean[V];
        
    }
    
    public PrimsEager(EdgeWeightedGraph G)
    {
        edgesTo = new Edge[G.V()];
        distsTo = new double[G.V()];
        mark = new boolean[G.V()];
        fib = new IndexFibonacciMinPQ<>(G.V());
        for(int v=0;v<G.V();v++)
        {
            distsTo[v] = Double.POSITIVE_INFINITY;
        }
        
        for(int v = 0;v<G.V();v++)
        {
            if(!mark[v])
            {
                prims(G,v);
            }
        }
        assert check(G);
    }
    
    private void prims(EdgeWeightedGraph G, int s)
    {
        distsTo[s] = 0.0;
        fib.insert(s, distsTo[s]);
        while(!fib.isEmpty())
        {
            int v = fib.delMin();
            scan(G,v);
        }
    }
    
    private void scan(EdgeWeightedGraph G, int v)
    {
        mark[v] = true;
        for(Edge e : G.adj(v))
        {
            int w = e.other(v);
            if(mark[w]) continue;
            if(e.weight() < distsTo[w])
            {
                distsTo[w] = e.weight();
                edgesTo[w] = e;
                if(fib.contains(w))
                {
                    fib.decreaseKey(w, distsTo[w]);
                }
                else
                {
                    fib.insert(w, distsTo[w]);
                }
            }
        }
    }
    
    /**
     * Returns the edges in a minimum spanning tree (or forest).
     * @return the edges in a minimum spanning tree (or forest) as
     *    an iterable of edges
     */
    public Iterable<Edge> edges() 
    {
        @SuppressWarnings("LocalVariableHidesMemberVariable")
        Queue<Edge> mst = new Queue<>();
        for (Edge e : edgesTo) {
            if (e != null) {
                mst.enqueue(e);
            }
        }
        return mst;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight() 
    {
        double weight = 0.0;
        for (Edge e : edges())
            weight += e.weight();
        return weight;
    }


    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph G) 
    {

        // check weight
        double totalWeight = 0.0;
        for (Edge e : edges()) {
            totalWeight += e.weight();
        }
        if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
            return false;
        }

        // check that it is acyclic
        UF uf = new UF(G.V());
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.connected(v, w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Edge e : G.edges()) {
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : edges()) {

            // all edges in MST except e
            uf = new UF(G.V());
            for (Edge f : edges()) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }

            // check that e is min weight edge in crossing cut
            for (Edge f : G.edges()) {
                int x = f.either(), y = f.other(x);
                if (!uf.connected(x, y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }
        return true;
    }

    public void compute(String path) 
    {
        In in = new In(path);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        PrimsEager prims = new PrimsEager(G);
        
        for(Edge e : prims.edges())
        {
            StdOut.println(e);
        }
        StdOut.println(prims.weight());
    }
    
}
