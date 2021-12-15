package sample;

public class CountPoints {
    int points = 0;

    /**
     * Refreshes the points of a player. The points change depending on the
     * type of ship that is hit. If a ship is sunk the corresponding bonus points, if
     * there are bonus points for the specific ship, are added to the points.
     * @param type_of_cell the type of ship contained in the hit cell
     * @param num_of_remaining_cells the number of the ship's cells that have not yet been hit by the opponent
     * @return the current points of the player/enemy
     */
    public int Count(String type_of_cell, int num_of_remaining_cells){
        if(type_of_cell == "carrier"){
            points = points + 350;
            if(num_of_remaining_cells == 0){
                points = points + 1000;
            }
        }
        else if(type_of_cell == "battleship"){
            points = points + 250;
            if(num_of_remaining_cells == 0){
                points = points + 500;
            }
        }
        else if(type_of_cell == "cruiser"){
            points = points + 100;
            if(num_of_remaining_cells == 0){
                points = points + 250;
            }
        }
        else if(type_of_cell == "submarine"){
            points = points + 100;
        }
        else if(type_of_cell == "destroyer"){
            points = points + 50;
        }
        return points;
    }
}
