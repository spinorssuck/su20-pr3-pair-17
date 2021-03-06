/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * Rithvik Reddy Golamari
 * rrg2477
 * <Student1 5-digit Unique No.>
 * Mina Abbassian
 * mea2947
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:https://github.com/spinorssuck/su20-pr3-pair-17
 * Summer 2020
 */


package assignment3;
import java.util.*;
import java.io.*;

class Node{
	public String word;
	//Reference to previous node
	public Node parent;
	
	public Node(String word,Node parent){
			this.word = word;
			this.parent = parent;
	}		
	
	public Node(String word){
		this.parent  = null;
		this.word = word;	
	}

	
}	

public class Main {
	
	public static Set<String> dictionary;
	public static HashSet<String> visited = new HashSet<String>();
	public static String startcopy;
	public static String endcopy;
	public static boolean setflag;
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();
		
		// TODO methods to read in words, output ladder
		ArrayList<String> words = parse(kb);
		if(words!=null){
		String start = words.get(0);
		String end = words.get(1);
		
		//Print ladder
		printLadder(getWordLadderBFS(start,end));
		}
	}
	
	public static void initialize() {
		setflag=false;
		startcopy = "";
		endcopy = "";
		visited.clear();
		dictionary = makeDictionary();
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		// TO DO
		
		//input from the keyboard is two lowercase words separated by a space
		//in addition, the command "/quit" must result in program terminating with no further input
		
		String firstWord = keyboard.next();
		if(firstWord.equals("/quit")){
			keyboard.close();
			return null;
		}	
		String secondWord = keyboard.next();
		ArrayList<String> output = new ArrayList<String>();
		
		//checking if the first or second inputted word is /quit
		if((firstWord.equals("/quit")) || (secondWord.equals("/quit"))){
			return null;
		}
		
		//otherwise, return the lists all in upper case
		output.add(firstWord.toUpperCase());
		output.add(secondWord.toUpperCase());
		
		return (output);
	}

	/**
	 * Finds a word ladder using Breadth First Search(BFS)
	 * between two words
	 * 
	 * @param start The starting word of the ladder
	 * @param end The end word of the ladder
	 * @return word ladder between start and end including both.
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// If ladder is empty, return list with just start and end.
		// TODO some code
		start= start.toUpperCase();
		end= end.toUpperCase();
		if(setflag==false){
			startcopy = start;
			endcopy = end;
			setflag=true;
		}	
		visited.add(start);
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(start);
		if(start.equals(end))
			return list;
		Queue<String> neighbors = getNeighbors(start, end);
		if(neighbors.isEmpty())
			return null;
		while(!neighbors.isEmpty()){
			
			ArrayList<String> temp = getWordLadderDFS(neighbors.remove(),end);
			
			if(temp!=null){
				ArrayList<String> temp1 = list;
				temp1.addAll(temp);
				return temp1;
			}	
		}
		return null;
		
	}
	
	/**
	 * Finds a word ladder using Breadth First Search(BFS)
	 * between two words
	 * 
	 * @param start The starting word of the ladder
	 * @param end The end word of the ladder
	 * @return word ladder between start and end including both.
	 */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		Queue<Node> queue = new LinkedList<Node>();
		start = start.toUpperCase();end = end.toUpperCase();
		startcopy = start;
		endcopy = end;
		
		//Add start to visited set
		visited.add(start);
		
		Node first = new Node(start);
		queue.add(first);
		while(!(queue.isEmpty())){
			Node node = queue.remove();
			String word = node.word;
			
			//Add node to visited set
			visited.add(word);
			
			Queue<String> neighbors = getNeighbors(word, end);
			//Adding neighbors to the queue
			while(!neighbors.isEmpty()){
				Node neighbor = new Node(neighbors.remove(),node);
				//Creating arraylist if word is equal to end word
				if(neighbor.word.equals(end)){

					ArrayList<String> WordLadder = new ArrayList<String>();
					Node check = neighbor;
					while(check!=null){
						WordLadder.add(check.word);
						check = check.parent;
					}	
					visited.clear();
					Collections.reverse(WordLadder);
					return WordLadder;
				}	
				queue.add(neighbor);
			}
		}
		visited.clear();
		return null;
	}
    
	/**
	 * Prints the word ladder(ArrayList) in the specified format
	 * 
	 * @param WordLadder
	 */
	public static void printLadder(ArrayList<String> ladder) {
		if(ladder==null)
			System.out.println("no word ladder can be found between "+startcopy.toLowerCase()+" and "+endcopy.toLowerCase()+".");
		else{
			System.out.println("a "+(ladder.size()-2)+"-rung word ladder exists between "+startcopy.toLowerCase()+" and "+endcopy.toLowerCase()+".");
			for(int i=0;i<ladder.size();i++){
				System.out.println(ladder.get(i));
			}	
		}	
	}

	/**
	 * Returns the word neighbors of start.
	 * Ensures that the neighbor has not been visited.
	 * Implements a way to print shorter DFS ladders.
	 *
	 * @param start The starting word of the ladder
	 * @param end The end word of the ladder
	 * @return Queue of neighbors
	 */
	private static Queue<String> getNeighbors(String word, String end) {
		//queue to be returned with all of the neighbors
		Queue<String> neighborsQueue = new LinkedList<String>();
		
		//the priority changes 
		//check the first 5 words
		//direct changes from the start word letters to the end word letters 
		
		for(int i = 0; i < 5; i++) {
			String checkWord = word;
			String endWord = end;
			if(i == 0) {
				checkWord = endWord.charAt(0) + checkWord.substring(1, 5);
			}
			
			else if(i == 4) {
				checkWord = checkWord.substring(0, 4) + endWord.charAt(4);
			}
			
			else {
				checkWord = checkWord.substring(0, i) + endWord.charAt(i) + checkWord.substring(i+1, 5);
			}
			
			if(dictionary.contains(checkWord)) {
				if(!visited.contains(checkWord)) {
					neighborsQueue.add(checkWord);
				}
			}		
		}
		
		for(int i = 0; i < 5; i++) {
			for(char c = 'A'; c <= 'Z'; c++) {
				String neighbor;
				if(i == 0) {
					neighbor = c + word.substring(1, 5);
				}
				
				else if(i == 4) {
					neighbor = word.substring(0, 4) + c;
				}
				
				else {
					neighbor = word.substring(0, i) + c + word.substring(i+1, 5);
				}
				//Checks if contained in dictionary
				if(dictionary.contains(neighbor)) {
					//Check if node is not already visited
					if(!visited.contains(neighbor)){
						neighborsQueue.add(neighbor);
					}	
				}		
			}
		}
		
		return neighborsQueue;
	}
		

	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
