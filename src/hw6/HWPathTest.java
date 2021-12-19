package hw6;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class HWPathTest 
{

	public static void main(String[] args) throws FileNotFoundException 
	{
				
		MinHeap pQ = new MinHeap();
		pQ.add(1, 5);
		pQ.add(11, 6);
		pQ.add(2, 6);
		pQ.add(3, 4);
		pQ.add(10, 3);
		pQ.add(9, 6);
		ArrayList<Integer> elements = pQ.getHeap();
		System.out.println("Binary MinHeap");
		for (int i=0; i<elements.size(); i++)
			System.out.printf("%5s", elements.get(i));
		System.out.println();

		PathFinder pF = new PathFinder();
		
		pF.readInput("sample1.txt");
		int node = 3;
		System.out.print(node + " neighbors are: ");
		for(int i = 0; i < pF.nodeList.get(node).neighbors.size(); ++i)
		{
			System.out.print(pF.nodeList.get(node).neighbors.get(i).id + " ");
		}
		System.out.println();
		System.out.println();
		
		System.out.println("Shortest path distances to all vertices\n");
		double[] sDistances = pF.shortestPathDistances(3);
		//double[] sDistances1 = pF.dist2All(3,2);
		for (int i = 0; i < sDistances.length; i++)
			System.out.println("Distance from 3 to " + i + ": " + sDistances[i]);
		System.out.println();
		
		System.out.println();
		int x = pF.noOfShortestPaths(3, 4);
		System.out.println("Number of Shortest Paths from 3 to 6: " + x + "\n");
		
		System.out.println();
		System.out.print("Is Everything reachable from 3: ");
		System.out.println(pF.isFullReachableFromSrc(3));
		System.out.println();
		
		System.out.println("Path from source to destination using some different valuations of relaxation parameters");
		ArrayList<Integer> p1 = pF.fromSrcToDest(1, 8, 1, 0);
		System.out.println("Params (A, B): " + 1 + ", " + 0);
		if (p1 == null) System.out.println("No path exists");
		else 
			for (int i=0; i < p1.size(); i++)
				System.out.print(p1.get(i) + " ");
		System.out.println();
		
		p1 = pF.fromSrcToDest(1, 8, 0, 1);
		System.out.println("Params (A, B): " + 0 + ", " + 1);
		if (p1 == null) System.out.println("No path exists");
		else 
			for (int i=0; i < p1.size(); i++)
				System.out.print(p1.get(i) + " ");
			System.out.println();
		
		p1 = pF.fromSrcToDest(1, 8, 1, 1);
		System.out.println("Params (A, B): " + 1 + ", " + 1);
		if (p1 == null) System.out.println("No path exists");
		else 
			for (int i=0; i < p1.size(); i++)
				System.out.print(p1.get(i) + " ");
		System.out.println();
		
		System.out.println();
	}

}
