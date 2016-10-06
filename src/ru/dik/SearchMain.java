package ru.dik;

import java.util.Collections;
import java.util.List;

import ru.dik.FlatGraph.Point;

public class SearchMain {

	public static void main(String[] args) {
		
		FlatGraph graph = new FlatGraph();
		System.out.println("Generated graph");
		System.out.println(graph.toString());
		
		graph.sythesizeWaveOnGraph();
		System.out.println("Graph after waving algorithm");
		System.out.println(graph.toString());
		
		List<Point> route = graph.getShortestTrackReverse();
		Collections.reverse(route);
		System.out.println("Resulting route");
		System.out.println(route);
		
		System.out.println("Resulting graph");
		System.out.println(graph.combineGraphAndRoute(route));
		
	}
	
}
