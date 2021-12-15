package sample;

import exceptions.AdjacentTilesException;
import exceptions.OrientationException;
import exceptions.OverlapTilesException;
import exceptions.OversizeException;

public class Battleship extends Boat {
    final int number_of_cells = 4;
    int starting_row_point, starting_column_point, orientation, number_of_remaining_cells = number_of_cells;
    private boolean untouched = true, hit = false, sunk = false;

    public Battleship (int[] a){
        this.starting_row_point = a[1];
        this.starting_column_point = a[2];
        this.orientation = a[3];
    }

    // Checking if the given layout is valid
    // If it is valid save the value "battleship" in the cells where the battleship is put
    public void Battleship_layout(Battleship new_battleship, String[][] layout) throws OversizeException, OverlapTilesException, AdjacentTilesException, OrientationException {
        if (new_battleship.orientation == 1) {
            for (int i = new_battleship.starting_column_point;
                 i < new_battleship.starting_column_point + new_battleship.number_of_cells; i++) {
                if(i < 0 || i > 9){
                    Main.exception_caught = true;
                    throw new OversizeException("Battleship is out of array bounds! Please give correct input file");

                }
                else if(ship_array.contains(layout[new_battleship.starting_row_point][i])){
                    Main.exception_caught = true;
                    throw new OverlapTilesException("The ships cannot overlap each other!");
                }
                else{
                    boolean battleship_bounds_x1 =  CheckingBounds(new_battleship.starting_row_point - 1,i,layout,"battleship");
                    boolean battleship_bounds_x2 =  CheckingBounds(new_battleship.starting_row_point + 1,i,layout,"battleship");
                    boolean battleship_bounds_y1 =  CheckingBounds(new_battleship.starting_row_point,i - 1,layout,"battleship");
                    boolean battleship_bounds_y2 =  CheckingBounds(new_battleship.starting_row_point,i + 1,layout,"battleship");
                    // Checking "diagonal" neighbors
                    boolean battleship_bounds_xy1 =  CheckingBounds(new_battleship.starting_row_point - 1,i - 1,layout,"battleship");
                    boolean battleship_bounds_xy2 =  CheckingBounds(new_battleship.starting_row_point + 1,i - 1,layout,"battleship");
                    boolean battleship_bounds_xy3 =  CheckingBounds(new_battleship.starting_row_point - 1,i + 1,layout,"battleship");
                    boolean battleship_bounds_xy4 =  CheckingBounds(new_battleship.starting_row_point + 1,i + 1,layout,"battleship");

                    if(battleship_bounds_x1 && battleship_bounds_x2 && battleship_bounds_y1 && battleship_bounds_y2 &&
                            battleship_bounds_xy1 && battleship_bounds_xy2 && battleship_bounds_xy3 && battleship_bounds_xy4){
                        layout[new_battleship.starting_row_point][i] = "battleship";
                    }
                    else{
                        Main.exception_caught = true;
                        throw new AdjacentTilesException("The ships must not be adjacent to each other");
                    }
                }
            }
        } else if(new_battleship.orientation == 2){
            for (int i = new_battleship.starting_row_point;
                 i < new_battleship.starting_row_point + new_battleship.number_of_cells; i++) {
                if(i < 0 || i >9){
                    Main.exception_caught = true;
                    throw new OversizeException("Battleship is out of array bounds! Please give correct input file");
                }
                else if(layout[i][new_battleship.starting_column_point] != "-"){
                    Main.exception_caught = true;
                    throw new OverlapTilesException("The ships cannot overlap each other!");
                }
                else{
                    // Check the 8 neighbors of each cell (the ones that are inside the board's limits)
                    boolean battleship_bounds_x1 =  CheckingBounds(i - 1,new_battleship.starting_column_point,layout,"battleship");
                    boolean battleship_bounds_x2 =  CheckingBounds(i + 1,new_battleship.starting_column_point,layout,"battleship");
                    boolean battleship_bounds_y1 =  CheckingBounds(i,new_battleship.starting_column_point - 1,layout,"battleship");
                    boolean battleship_bounds_y2 =  CheckingBounds(i,new_battleship.starting_column_point + 1,layout,"battleship");
                    // Checking "diagonal" neighbors
                    boolean battleship_bounds_xy1 =  CheckingBounds(i - 1,new_battleship.starting_column_point-1,layout,"battleship");
                    boolean battleship_bounds_xy2 =  CheckingBounds(i - 1,new_battleship.starting_column_point+1,layout,"battleship");
                    boolean battleship_bounds_xy3 =  CheckingBounds(i+1,new_battleship.starting_column_point - 1,layout,"battleship");
                    boolean battleship_bounds_xy4 =  CheckingBounds(i+1,new_battleship.starting_column_point + 1,layout,"battleship");

                    if(battleship_bounds_x1 && battleship_bounds_x2 && battleship_bounds_y1 && battleship_bounds_y2 &&
                            battleship_bounds_xy1 && battleship_bounds_xy2 && battleship_bounds_xy3 && battleship_bounds_xy4){
                        layout[i][new_battleship.starting_column_point] = "battleship"; // 1 means there is a boat in that cell
                    }
                    else{
                        Main.exception_caught = true;
                        throw new AdjacentTilesException("The ships must not be adjacent to each other");
                    }
                }
            }
        }
        else{
            Main.exception_caught = true;
            throw new OrientationException("Please give valid orientation for battleship!");
        }
    }

    //Getters
    public boolean getUntouchedState(){
        return this.untouched;
    }
    public boolean getHitState(){
        return this.hit;
    }
    public boolean getSunkState(){
        return this.sunk;
    }

    //Setters
    public void setUntouchedState(boolean untouched){
        this.untouched = untouched;
    }
    public void setHitState(boolean hit){
        this.hit = hit;
    }
    public void setSunkState(boolean sunk){
        this.sunk = sunk;
    }
}
