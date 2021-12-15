package sample;

public class PlayerShots extends Boat{
    boolean shot ;
    int x, y;
    Boat b_player, b_enemy;
    public static int player_points = 0;

    PlayerShots(Boat b_player, Boat b_enemy, int x, int y){
        this.shot = b_player.shot;
        this.x = x;
        this.y = y;
        this.b_enemy = b_enemy;
        this.b_player = b_player;
    }

    public String Shoot() {
        String hit_cell = b_enemy.changeLayout(x, y).getKey();
        player_points = b_player.Count(hit_cell, b_enemy.num_of_remaining_cells);
        b_player.shot = true; // The player's shot for this round is completed
        b_player.number_of_shots++;

        // The player managed to hit the enemy
        if(hit_cell != "-"){
            b_player.number_of_successful_shots++;
        }

        return hit_cell;
    }
}
