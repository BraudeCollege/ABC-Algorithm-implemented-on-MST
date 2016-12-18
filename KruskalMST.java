
package com.graph.MST;

/**
 *
 * @author Rishabh
 */
public class KruskalMST 
{
    private static final double FLOATING_POINT_EPSILON = 1E-12;
    private double weight;
    private final Queue<Edge> mst = new Queue<>();
    
    public KruskalMST()
    {
        
    }
    
    public KruskalMST(EdgeWeightedGraph G)
    {
        MinPQ<Edge> pq = new MinPQ<>();
        for(Edge e : G.edges())
        {
            pq.insert(e);
        }
        
        //run greedy Algorithm
        UF uf = new UF(G.V());
        
        while(!pq.isEmpty() && mst.size() < G.V() - 1)
        {
            Edge e = pq.delMin();
            int v = e.either();
            int w = e.other(v);
            if(!uf.connected(v, w))
            {
                uf.union(v, w);
                mst.enqueue(e);
                weight += e.weight();
            }
            
        }
        assert  check(G);
    }
    
    public Iterable<Edge> edges()
    {
        return mst;
    }
    
    public double weight()
    {
        return weight;
    }
    
    private boolean check(EdgeWeightedGraph G)
    {
        //check total weight
        double total = 0.0;
        for(Edge e : edges())
        {
            total += e.weight();
        }
        if(Math.abs(total - weight()) > FLOATING_POINT_EPSILON)
        {
            System.err.printf("Weight of edges dows not equal weight() : %f vs. %f\n",total,weight());
            return false;
        }
        
        //check that it is acyclic
        UF uf = new UF(G.V());
        for(Edge e : edges())
        {
            int v = e.either();
            int w = e.other(v);
            
            if(uf.connected(v, w))
            {
                System.err.println("Not a forest");
                return true;
            }
            uf.union(v, w);
        }
        
        //check that it is a spanning forest
        for(Edge e : G.edges())
        {
            int v = e.either() , w = e.other(v);
            if(!uf.connected(v, w))
            {
                System.err.println("Not a spanning forest");
                return false;
            }
        }
        
        //check that it is minimal spanning forest
        for(Edge e : edges())
        {
            uf = new UF(G.V());
            for(Edge f : mst )
            {
                int x = f.either();
                int y = f.other(x);
                if(f != e)
                {
                    uf.union(x, y);
                }
            }
            
            //check that e is min weight crossing cut
            for(Edge f : G.edges())
            {
                int x = f.either();
                int y = f.other(x);
                
                if(!uf.connected(x, y))
                {
                    if(f.weight() < e.weight())
                    {
                        System.err.println("Edge " + f + " violates cut optimally conditions");
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
        @SuppressWarnings("LocalVariableHidesMemberVariable")
        KruskalMST mst = new KruskalMST(G);
        for(Edge e : mst.edges())
        {
            StdOut.println(e);
        }
        //Total mean weight
        //StdOut.printf("%.5f\n", mst.weight());
        //mst.getTotalWeight(mst.weight());
    }
    
    
    
}
