package Main;
public class CollisionChecker {

    public boolean checkCollision(Entity e){

        if(e.x < 0) return true;
        if(e.y < 0) return true;

        if(e.x > 2900) return true;
        if(e.y > 1900) return true;

        return false;

    }

}
