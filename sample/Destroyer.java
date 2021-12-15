package sample;


import exceptions.AdjacentTilesException;
import exceptions.OrientationException;
import exceptions.OverlapTilesException;
import exceptions.OversizeException;

public class Destroyer extends Boat{
    final int number_of_cells = 2;
    int starting_row_point, starting_column_point, orientation, number_of_remaining_cells = number_of_cells;
    private boolean untouched = true, hit = false, sunk = false;

    public Destroyer (int[] a){
        this.starting_row_point = a[1];
        this.starting_column_point = a[2];
        this.orientation = a[3];
    }

    // Checking if the given layout is valid
    // If it is valid save the value "destroyer" in the cells where the destroyer is put
    public void Destroyer_layout(Destroyer new_destroyer, String[][] layout) throws OversizeException, OverlapTilesException, AdjacentTilesException, OrientationException {
        if (new_destroyer.orientation == 1) {
            for (int i = new_destroyer.starting_column_point;
                 i < new_destroyer.starting_column_point + new_destroyer.number_of_cells; i++) {
                if(i < 0 || i > 9){
                    Main.exception_caught = true;
                    throw new OversizeException("Destroyer is out of array bounds! Please give correct input file");
                }
                else if(ship_array.contains(layout[new_destroyer.starting_row_point][i])){
                    System.out.println(ship_array.contains(layout[new_destroyer.starting_row_point][i]));
                    Main.exception_caught = true;
                    System.out.println("SAY STH");
                    throw new OverlapTilesException("The ships cannot overlap each other!");
                }
                else{
                    boolean destroyer_bounds_x1 =  CheckingBounds(new_destroyer.starting_row_point - 1,i,layout,"destroyer");
                    boolean destroyer_bounds_x2 =  CheckingBounds(new_destroyer.starting_row_point + 1,i,layout,"destroyer");
                    boolean destroyer_bounds_y1 =  CheckingBounds(new_destroyer.starting_row_point,i - 1,layout,"destroyer");
                    boolean destroyer_bounds_y2 =  CheckingBounds(new_destroyer.starting_row_point,i + 1,layout,"destroyer");
                    // Checking "diagonal" neighbors
                    boolean destroyer_bounds_xy1 =  CheckingBounds(new_destroyer.starting_row_point - 1,i - 1,layout,"destroyer");
                    boolean destroyer_bounds_xy2 =  CheckingBounds(new_destroyer.starting_row_point + 1,i - 1,layout,"destroyer");
                    boolean destroyer_bounds_xy3 =  CheckingBounds(new_destroyer.starting_row_point - 1,i + 1,layout,"destroyer");
                    boolean destroyer_bounds_xy4 =  CheckingBounds(new_destroyer.starting_row_point + 1,i + 1,layout,"destroyer");

                    if(destroyer_bounds_x1 && destroyer_bounds_x2 && destroyer_bounds_y1 && destroyer_bounds_y2 &&
                            destroyer_bounds_xy1 && destroyer_bounds_xy2 && destroyer_bounds_xy3 && destroyer_bounds_xy4){
                        layout[new_destroyer.starting_row_point][i] = "destroyer"; // 1 means there is a boat in that cell
                    }
                    else{
                        Main.exception_caught = true;
                        throw new AdjacentTilesException("The ships must not be adjacent to each other");
                    }
                }
            }
        } else if (new_destroyer.orientation == 2){
            for (int i = new_destroyer.starting_row_point;
                 i < new_destroyer.starting_row_point + new_destroyer.number_of_cells; i++) {
                if(i < 0 || i > 9){
                    Main.exception_caught = true;
                    throw new OversizeException("Destroyer is out of array bounds! Please give correct input file");
                }
                else if(layout[i][new_destroyer.starting_column_point] != "-"){
                    Main.exception_caught = true;
                    throw new OverlapTilesException("The ships cannot overlap each other!");
                }
                else{
                    boolean destroyer_bounds_x1 = CheckingBounds(i - 1,new_destroyer.starting_column_point,layout,"destroyer");
                    boolean destroyer_bounds_x2 = CheckingBounds(i + 1,new_destroyer.starting_column_point,layout,"destroyer");
                    boolean destroyer_bounds_y1 = CheckingBounds(i,new_destroyer.starting_column_point - 1,layout,"destroyer");
                    boolean destroyer_bounds_y2 = CheckingBounds(i,new_destroyer.starting_column_point + 1,layout,"destroyer");
                    // Checking "diagonal" neighbors
                    boolean destroyer_bounds_xy1 =  CheckingBounds(i - 1,new_destroyer.starting_column_point - 1,layout,"destroyer");
                    boolean destroyer_bounds_xy2 =  CheckingBounds(i - 1,new_destroyer.starting_column_point + 1,layout,"destroyer");
                    boolean destroyer_bounds_xy3 =  CheckingBounds(i + 1,new_destroyer.starting_column_point - 1,layout,"destroyer");
                    boolean destroyer_bounds_xy4 =  CheckingBounds(i + 1,new_destroyer.starting_column_point + 1,layout,"destroyer");

                    if(destroyer_bounds_x1 && destroyer_bounds_x2 && destroyer_bounds_y1 && destroyer_bounds_y2 &&
                            destroyer_bounds_xy1 && destroyer_bounds_xy2 && destroyer_bounds_xy3 && destroyer_bounds_xy4){
                        layout[i][new_destroyer.starting_column_point] = "destroyer"; // 1 means there is a boat in that cell
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
            throw new OrientationException("Please give valid orientation for destroyer!");
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
