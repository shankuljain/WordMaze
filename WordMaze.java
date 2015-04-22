
/*
 * File : WordMaze.java
 * Created by Shankul Jain on April 17, 2015
 */


import java.util.Random;
import java.util.*;

class WordMaze implements Direction{
	
    /* private final variables */
    private static final int start_index = 2;
	private static final int size = 12;
	private static final int delimiter = -1;
	
	/* Program starts from here */
	public static void main(String [] args){
		
		Scanner s = new Scanner(System.in);
        System.out.println("Enter no of words");
        int n =  s.nextInt();
		
        /* combining the all words into a single string */
		StringBuilder sb = new StringBuilder();
		
        System.out.println("Enter words");
        for(int i=0;i<n;i++){
            sb.append(s.next());
        }
        
		String str = sb.toString();
		
		/* filling Integer array with delimiter that is -1 */
		arr = new int[size*size];
		Arrays.fill(arr, delimiter);
		

		/* checking whether maze can be generated or not
		 * if possible than calling the generate puzzle function that generates the maze.
		 */
        if(str.length() > size*size){
            System.out.println("impossible");
        }else{
            generatePuzzle(str,start_index);
            showPuzzle(str);	//function to print the puzzle on the console
        }
				
	}
    
	/* Random characters at empty places are not filled 
	 * for easily watching the input words and its flow.
	 * Below Method can be modified to fill the empty positions
	 * with random characters.
	 */
	
    private static void showPuzzle(String str){
        for(int i=0;i<size;i++){
            for(int j =0; j<size;j++){
                int pos = arr[size*i+j];
                if(pos == -1){
                    System.out.print('0'+"\t");         // '0' means random character
                }else{
                    System.out.print(str.charAt(pos) + "\t");
                }
                
            }
            System.out.println();
        }

    }
	
    
    /* Method : generatePuzzle 
     * This method is responsible for generating the puzzle.
     * It is a recursive backtracking method that goes in forward direction
     * until program finishes aur some conflicts occur to the constraints.
     * If a conflict occur it backtrack to a upper lever and continues till program finishes.
     */
	private static void generatePuzzle(String s, int currentIndex){
		
		arr[currentIndex] = elemcounter++;
		
		/* Internal if block makes sure that last character is at the border */
		if(s.length() == elemcounter){
			if(!checkBorder(currentIndex)){         //Comment this if block if position of last character
				arr[currentIndex] = delimiter;      // doesn't matter (uncommented version requires more time)
				--elemcounter;
			}
			return;
		}
		
		/* list keeping record for each direction it has visited 
		 * and making sure not going to the same direction if program
		 * backtracks to a previous level.
		 */
		ArrayList<Integer> list = new ArrayList<Integer>();
		int nextIndex = getNextIndex(currentIndex,list);
		
		
		/* we go in forward direction until every constraints are satisfied */
		while(nextIndex != -1){
			generatePuzzle(s,nextIndex);
			if(s.length() == elemcounter){
				return;
			}
			nextIndex = getNextIndex(currentIndex,list);
		}
		
		/* this if block is responsible for backtracking */
		if(nextIndex == -1){
			arr[currentIndex] = delimiter;
			--elemcounter;
		}
		
	}
	
	/* checkBorder function that checks that given index is at Border of maze or not
	 * It is only called for checking the last character if required.
	 */
	private static boolean checkBorder(int index){
		if((index<size && index >=0) || (index%size == 0) || ((index+1)%size == 0) || (index<size*size && index >= size*size-size )){
			return true;
		}
		return false;
	}
	
	/* This method generates the index of next Cell in which character is to 
	 * be filled. It also checks whether it is possible or not to go in a 
	 * particular direction and remembers all the choices it has made.
	 */
	private static int getNextIndex(int index,ArrayList<Integer> list){
		int random;
		
		while(true){
			random = rgen.nextInt(count); // returns a random no between 0 and count excluding count.
			if(list.size() == count){
				break;
			}else if(list.contains(random)){
				//do nothing
			}else{
				list.add(random);
				switch(random){
				
				case west :
					if(index%size !=0){
						if(arr[index-1] == delimiter){
							return index-1;  //moving left
						}	
					}
					break;
					
				case east :
					if((index+1)%size!=0){
						if(arr[index+1] == delimiter){
							return index+1; // moving right;
						}
					}
					break;
				
				case south :	
					if(index < size*size - size){
						if(arr[index+size] == delimiter){
							return index+size; // moving down
						}
					}
					break;
					
				case north :
					if(index >= size){
						if(arr[index-size] == delimiter){
							return index-size;
						}
					}
					break;
					
				case south_west :
					if((index%size !=0) && (index < size*size - size)){
						if(arr[index+size-1] == delimiter){
							if(isConflict(south_west,index)){
								break;
							}
							return index + size -1; // left and down
						}
					}
					break;
					
				case south_east : 
					if(((index+1)%size!=0) && (index < size*size - size)){
						if(arr[index+size+1] == delimiter){
							if(isConflict(south_east,index)){
								break;
							}
							return index+size+1;		//right and down
						}
					}
					break;
					
				case north_east :
					if(((index+1)%size!=0) && (index >= size)){
						if(arr[index-size+1] == delimiter){
							if(isConflict(north_east,index)){
								break;
							}
							return index-size+1;		//right and up
						}
					}
					break;
					
				case north_west :
					if((index%size !=0) && (index >= size)){
						if(arr[index-size-1] == delimiter){
							if(isConflict(north_west,index)){
								break;
							}
							return index-size-1;		//left and up
						}
					}
					break;
					
				default :
					System.out.println("something wrong");
				}
			}
			
		}
		
		return -1;
	}
	
	/* this method checks for the diagonal conflict */
	private static boolean isConflict(int direction, int currentIndex){
        int val1 = 0;
        int val2 = 0;
		
		switch(direction){
		case south_west :
			val1 = arr[currentIndex+size]; 	//south
			val2 = arr[currentIndex-1];		//west
            break;
			
		case south_east :
			val1 = arr[currentIndex+size];
			val2 = arr[currentIndex+1];
            break;
                
		case north_west : 
			val1 = arr[currentIndex-size];
			val2 = arr[currentIndex-1];
            break;
			
		case north_east :
			val1 = arr[currentIndex-size];
			val2 = arr[currentIndex+1];
            break;
		}
        
        if(val1 == val2+1 || val1 == val2-1) return true;
        return false;
		
	}
	
	private static int elemcounter = 0;
	private static Random rgen = new Random();
	private static int arr[];
}