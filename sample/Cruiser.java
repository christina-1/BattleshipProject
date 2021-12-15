package sample;

import exceptions.AdjacentTilesException;
import exceptions.OrientationException;
import exceptions.OverlapTilesException;
import exceptions.OversizeException;

public class Cruiser extends Boat{
    final int number_of_cells = 3;
    int starting_row_point, starting_column_point, orientation, number_of_remaining_cells = number_of_cells;
    private boolean untouched = true, hit = false, sunk = false;

    public Cruiser (int[] a){
        this.starting_row_point = a[1];
        this.starting_column_point = a[2];
        this.orientation = a[3];
    }

    // Checking if the given layout is valid
    // If it is valid save the value "cruiser" in the cells where the cruiser is put
    public void Cruiser_layout(Cruiser new_cruiser, String[][] layout) throws OversizeException, OverlapTilesException, AdjacentTilesException, OrientationException {
        if (new_cruiser.orientation == 1) {
            for (int i = new_cruiser.starting_column_point;
                 i < new_cruiser.starting_column_point + new_cruiser.number_of_cells; i++) {
                if(i < 0 || i > 9){
                    Main.exception_caught = true;
                    throw new OversizeException("Cruiser is out of array bounds! Please give correct input file");
                }
                else if(ship_array.contains(layout[new_cruiser.starting_row_point][i])){
                    Main.exception_caught = true;
                    throw new OverlapTilesException("The ships cannot overlap each other!");
                }
                else{
                    boolean cruiser_bounds_x1 =  CheckingBounds(new_cruiser.starting_row_point - 1,i,layout,"cruiser");
                    boolean cruiser_bounds_x2 =  CheckingBounds(new_cruiser.starting_row_point + 1,i,layout,"cruiser");
                    boolean cruiser_bounds_y1 =  CheckingBounds(new_cruiser.starting_row_point,i - 1,layout,"cruiser");
                    boolean cruiser_bounds_y2 =  CheckingBounds(new_cruiser.starting_row_point,i + 1,layout,"cruiser");
                    // Checking "diagonal" neighbors
                    boolean cruiser_bounds_xy1 =  CheckingBounds(new_cruiser.starting_row_point - 1,i - 1,layout,"cruiser");
                    boolean cruiser_bounds_xy2 =  CheckingBounds(new_cruiser.starting_row_point + 1,i - 1,layout,"cruiser");
                    boolean cruiser_bounds_xy3 =  CheckingBounds(new_cruiser.starting_row_point - 1,i + 1,layout,"cruiser");
                    boolean cruiser_bounds_xy4 =  CheckingBounds(new_cruiser.starting_row_point + 1,i + 1,layout,"cruiser");

                    if(cruiser_bounds_x1 && cruiser_bounds_x2 && cruiser_bounds_y1 && cruiser_bounds_y2 &&
                            cruiser_bounds_xy1 && cruiser_bounds_xy2 && cruiser_bounds_xy3 && cruiser_bounds_xy4){
                        layout[new_cruiser.starting_row_point][i] = "cruiser"; // 1 means there is a boat in that cell
                    }
                    else{
                        Main.exception_caught = true;
                        throw new AdjacentTilesException("The ships must not be adjacent to each other");
                    }
                }
            }
        } else if (new_cruiser.orientation == 2){
            for (int i = new_cruiser.starting_row_point;
                 i < new_cruiser.starting_row_point + new_cruiser.number_of_cells; i++) {
                if(i < 0 || i >9){
                    Main.exception_caught = true;
                    throw new OversizeException("Cruiser is out of array bounds! Please give correct input file");
                }
                else if(layout[i][new_cruiser.starting_column_point] != "-"){
                    Main.exception_caught = true;
                    throw new OverlapTilesException("The ships cannot overlap each other!");
                }
                else{
                    boolean cruiser_bounds_x1 = CheckingBounds(i - 1,new_cruiser.starting_column_point,layout,"cruiser");
                    boolean cruiser_bounds_x2 = CheckingBounds(i + 1,new_cruiser.starting_column_point,layout,"cruiser");
                    boolean cruiser_bounds_y1 = CheckingBounds(i,new_cruiser.starting_column_point - 1,layout,"cruiser");
                    boolean cruiser_bounds_y2 = CheckingBounds(i,new_cruiser.starting_column_point + 1,layout,"cruiser");

                    // Checking "diagonal" neighbors
                    boolean cruiser_bounds_xy1 =  CheckingBounds(i - 1,new_cruiser.starting_column_point - 1,layout,"cruiser");
                    boolean cruiser_bounds_xy2 =  CheckingBounds(i - 1,new_cruiser.starting_column_point + 1,layout,"cruiser");
                    boolean cruiser_bounds_xy3 =  CheckingBounds(i + 1,new_cruiser.starting_column_point - 1,layout,"cruiser");
                    boolean cruiser_bounds_xy4 =  CheckingBounds(i + 1,new_cruiser.starting_column_point + 1,layout,"cruiser");
                    if(cruiser_bounds_x1 && cruiser_bounds_x2 && cruiser_bounds_y1 && cruiser_bounds_y2 &&
                            cruiser_bounds_xy1 && cruiser_bounds_xy2 && cruiser_bounds_xy3 && cruiser_bounds_xy4){
                        layout[i][new_cruiser.starting_column_point] = "cruiser"; // 1 means there is a boat in that cell
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
            throw new OrientationException("Please give valid orientation for cruiser!");
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
