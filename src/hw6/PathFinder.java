//package hw6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class PathFinder {
	
	int nodeCount;
	int edgeCount;
	
	HashMap<Integer,Node> nodeList = new HashMap<>();
	
	public PathFinder() {}
	
	public void readInput(String filename) throws FileNotFoundException
	{
		File file = new File(filename);
		Scanner in = new Scanner(file);
		
		boolean readEdges = false;
		
		int lineCount = 1;
		while(in.hasNextLine())
		{
			String line = in.nextLine();
			//System.out.println(lineCount + ": " + line);
			
			if(line.isEmpty())
			{
				readEdges = true;
			}
			else if (!readEdges)
			{
				if(lineCount == 1)
				{
					Scanner in2 = new Scanner(line);
					while(in2.hasNext())
					{
						nodeCount = in2.nextInt();
						edgeCount = in2.nextInt();
					}
					in2.close();
					++lineCount;
					continue;
				}
				
				// get the coords
				Scanner in2 = new Scanner(line);
				while(in2.hasNext())
				{
					int id = in2.nextInt();
					int x = in2.nextInt();
					int y = in2.nextInt();
					
					Node n = new Node(id,x,y);
					
					nodeList.put(id,n);
				}
				in2.close();
				
			}
			else // read the edges
			{
				int count = 0;
				Scanner in2 = new Scanner(line);
				while(in2.hasNext() && count < edgeCount)
				{
					int n = in2.nextInt();
					int m = in2.nextInt();
					
					nodeList.get(n).neighbors.add(nodeList.get(m));
					nodeList.get(m).neighbors.add(nodeList.get(n));
					++count;
				}
				in2.close();
			}
			++lineCount;
		}
		in.close();
	}
	
	public double[] shortestPathDistances(int id)
	{
		int k = 0;
		double[] distances = new double[nodeList.size()];
		
		for(int i = 0; i < distances.length; ++i)
		{
			distances[i] = -1;
		}
		
		HashMap<Integer,Double> hm = new HashMap<>();
		PriorityQueue<Node> pq = new PriorityQueue<>();
		
		HashMap<Integer,Node> nodeList = copyHM(this.nodeList);
		Node src = nodeList.get(id);
		src.distance = 0;
		src.visited = false;

		pq.add(src);
		
		while(!pq.isEmpty() && pq.peek() != null)
		{
			Node cur = pq.peek();
			pq.poll();
			for(int i = 0; i < cur.neighbors.size(); ++i)
			{
				Node neighbor = cur.neighbors.get(i);
				double distance = distance(cur.x,cur.y,neighbor.x,neighbor.y);
				
				if(neighbor.visited == false && neighbor != src)
				{
					neighbor.visited = true;
					neighbor.distance = cur.distance + distance;
					pq.add(neighbor);
					//distances[k] = neighbor.distance;
					//k++;
					hm.put(neighbor.id,neighbor.distance);
				}
				else if (neighbor == src)
				{
					neighbor.visited = true;
					hm.put(neighbor.id,neighbor.distance);
				}
				else
				{
					if(cur.distance + distance < neighbor.distance)
					{
						neighbor.distance =  cur.distance + distance;
						//distances[k] = neighbor.distance;
						//k++;
						hm.put(neighbor.id,neighbor.distance);
					}
				}
			}
		}
		
		// Sort the hashmap
		Map<Integer,Double> map = new TreeMap<>(hm);
		Set set = map.entrySet();
		Iterator iter = set.iterator();
		while(iter.hasNext())
		{
			Map.Entry me = (Map.Entry)iter.next();
			distances[k] = (double) me.getValue();
			++k;
		}
		
		return distances;
	}
	
	public int noOfShortestPaths(int src, int dest)
	{
		PriorityQueue<Node> pq = new PriorityQueue<>();
		HashMap<Integer,Node> nodeList = copyHM(this.nodeList);
		Node start = nodeList.get(src);
		start.distance = 0;
		start.visited = false;
		start.paths = 1;
		pq.add(start);
		
		while(!pq.isEmpty() && pq.peek() != null)
		{
			Node cur = pq.peek();
			pq.poll();
			for(int i = 0; i < cur.neighbors.size(); ++i)
			{
				Node neighbor = cur.neighbors.get(i);
				double distance = distance(cur.x,cur.y,neighbor.x,neighbor.y);
				
				if(neighbor.visited == false && neighbor != start)
				{
					neighbor.visited = true;
					neighbor.distance = cur.distance + distance;
					neighbor.paths = cur.paths;
					pq.add(neighbor);
				}
				else if (neighbor == start)
				{
					neighbor.visited = true;
				}
				else
				{
					if(cur.distance + distance < neighbor.distance)
					{
						neighbor.distance =  cur.distance + distance;
					}
					else if(cur.distance + distance == neighbor.distance)
					{
						neighbor.paths = neighbor.paths + cur.paths;
					}
				}
			}
		}
		return nodeList.get(dest).paths;
	}
	
	public ArrayList<Integer> fromSrcToDest(int start, int end, int A, int B)
	{
		ArrayList<Integer> ids = new ArrayList<>();
		PriorityQueue<Node> pq = new PriorityQueue<>();
		HashMap<Integer,Node> nodeList = copyHM(this.nodeList);
		
		Node src = nodeList.get(start);
		src.distance = 0;
		src.visited = true;
		
		pq.add(src);

		while(!pq.isEmpty() && pq.peek() != null && pq.peek() != nodeList.get(end))
		{
			Node cur = pq.peek();
			pq.poll();
			for(int i = 0; i < cur.neighbors.size(); ++i)
			{
				Node neighbor = cur.neighbors.get(i);
				double distance = distance(cur.x,cur.y,neighbor.x,neighbor.y);
				
				if(neighbor.visited == false)
				{
					neighbor.prev = cur;
					neighbor.visited = true;
					
					Node target = nodeList.get(end);
					double d1 = distance(neighbor.x,neighbor.y,target.x,target.y);
					double d2 = distance(cur.x,cur.y,target.x,target.y);
					neighbor.distance = A*(cur.distance + distance) + B*(d1-d2);
					pq.add(neighbor);
				}
				else
				{
					if(cur.distance + distance < neighbor.distance)
					{
						neighbor.prev = cur;
						Node target = nodeList.get(end);
						double d1 = distance(neighbor.x,neighbor.y,target.x,target.y);
						double d2 = distance(cur.x,cur.y,target.x,target.y);
						neighbor.distance = A*(cur.distance + distance) + B*(d1-d2);
					}
				}
			}
		}
		
		Node n = nodeList.get(end);
		
		if(n.prev == null) return null;
		
		while(n != null)
		{
			if(n.id != src.id)
			{
				ids.add(n.id);
				n = n.prev;
			}
			else
			{
				ids.add(n.id);
				break;
			}
		}
		ArrayList<Integer> path = new ArrayList<>();
		for(int i = ids.size()-1; i >= 0; --i)
		{
			path.add(ids.get(i));
		}
		
		return path;
	}
	
	public ArrayList<Integer> fromSrcToDestVia(int start, int end, ArrayList<Integer> idList, int A, int B)
	{
		// maybe improve this algorithm to be quicker
		
		ArrayList<Integer> path = new ArrayList<>();
		idList.add(end);
		path.add(start);
		int src = start;
		for(int i = 0; i < idList.size(); ++i)
		{
			int target = idList.get(i);
			ArrayList<Integer> arr = fromSrcToDest(src,target,A,B);
			if(arr == null) return null;
			
			for(int j = 1; j < arr.size(); ++j)
			{
				path.add(arr.get(j));
			}
			src = idList.get(i);
		}
		return path;
	}
	
	public int[] minCostReachabilityFromSrc(int id)
	{
		int[] tree = new int[nodeList.size()];
		
		for(int i = 0; i < tree.length; ++i)
		{
			tree[i] = -1;
		}
		
		PriorityQueue<Node> pq = new PriorityQueue<>();
		Node src = nodeList.get(id);
		src.distance = 0;

		pq.add(src);
		
		while(!pq.isEmpty())
		{
			Node cur = pq.peek();
			cur.visited = true;
			pq.poll();
			
			for(int i = 0; i < cur.neighbors.size(); ++i)
			{
				Node neighbor = cur.neighbors.get(i);
				double distance = distance(cur.x,cur.y,neighbor.x,neighbor.y);
				
				if(neighbor.visited == false)
				{
					if(distance < neighbor.distance)
					{
						pq.remove(neighbor);
						neighbor.distance = distance;
						pq.add(neighbor);
						neighbor.prev = cur;
					}
				}
			}
		}
		
		for(int i = 0; i < nodeList.size(); ++i)
		{
			Node n = nodeList.get(i);
			if(n.prev != null)
			{
				tree[i] = n.prev.id;
			}
		}
		
		return tree;
	}
	
	public double minCostOfReachabilityFromSrc(int id)
	{
		double totalWeight = 0;
		
		int[] tree = minCostReachabilityFromSrc(id);
		
		for(int i = 0; i < nodeList.size(); ++i)
		{
			Node child = nodeList.get(i);
			Node parent = nodeList.get(tree[i]);
			if(parent != null)
			{
				double distance = distance(child.x,child.y,parent.x,parent.y);
				totalWeight += distance;
			}
		}
		
		return totalWeight;
	}
	
	public boolean isFullReachableFromSrc(int id)
	{
		double[] results = shortestPathDistances(id);
		for(int i = 0; i < results.length; ++i)
		{
			if(results[i] < 0)
			{
				return false;
			}
		}
		return true;
	}
	
	public double distance(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt((x1 - x2)* (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	private HashMap<Integer,Node> copyHM(HashMap<Integer,Node> hm)
	{
		HashMap<Integer,Node> result = new HashMap<>();
		for(Map.Entry<Integer,Node> set : hm.entrySet())
		{
			int key = set.getKey();
			Node n = new Node();
			n.id = key;
			n.x = set.getValue().x;
			n.y = set.getValue().y;
			result.put(key,n);
		}
		
		for(Map.Entry<Integer,Node> set : hm.entrySet())
		{
			Node n = result.get(set.getKey());
			copyNeighbors(set.getValue().neighbors,result, n);
		}
		
		return result;
	}
	private void copyNeighbors(ArrayList<Node> list,HashMap<Integer,Node> hm, Node n)
	{
		for(int i =0; i < list.size(); ++i)
		{
			int id = list.get(i).id;
			if(hm.containsKey(id) && id == hm.get(id).id)
			{
				n.neighbors.add(hm.get(id));
			}
		}
	}

}

class MinHeap
{
	ArrayList<Vertice> heap;
	
	public MinHeap()
	{
		this.heap = new ArrayList<>();
	}
	
	public void add(int key, double val)
	{
		Vertice v = new Vertice(key,val);
		heap.add(v);
		
		int i = heap.size()-1;
		int parent = parent(i);
		
		while(heap.get(parent) != heap.get(i) && heap.get(i).val < heap.get(parent).val)
		{
			Vertice temp = new Vertice();
			temp = heap.get(parent);
			heap.set(parent,heap.get(i));
			heap.set(i,temp);
			i = parent;
			parent = parent(i);
		}
		
		
		while(heap.get(parent) != heap.get(i) && heap.get(i).val == heap.get(parent).val && heap.get(i).key < heap.get(parent).key)
		{
			Vertice temp = new Vertice();
			temp = heap.get(parent);
			heap.set(parent,heap.get(i));
			heap.set(i,temp);
			i = parent;
			parent = parent(i);
		}
	}
	
	public ArrayList<Integer> getHeap()
	{
		ArrayList<Integer> list = new ArrayList<>();
		for(int i = 0; i < heap.size(); ++i)
		{
			list.add(heap.get(i).key);
		}
		return list;
	}
	
	private int parent(int i )
	{
		if(i%2==1) return (i/2);
		else return (i-1)/2;
	}
}

class Vertice
{
	int key;
	double val;
	public Vertice(int key, double val){
		this.key=key;
		this.val=val;
	}
	public Vertice() {}
	
}

class Node implements Comparable<Node>
{
	int id;
	int x;
	int y;
	boolean visited = false;
	ArrayList<Node> neighbors = new ArrayList<>();
	double distance = Double.MAX_VALUE;
	int paths = 0;
	Node prev;
	
	public Node(int id, int x, int y)
	{
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public Node() {}
	
	@Override
	public int compareTo(Node o) 
	{
		if(this.distance < o.distance) return -1;
		else return 1;
	}
}