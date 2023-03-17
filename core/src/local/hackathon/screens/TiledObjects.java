package local.hackathon.screens;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static local.hackathon.util.Settings.PPM;

public class TiledObjects {

    public static void parse(World world, MapObjects objects){
        for(MapObject object : objects){
            Shape shape;
            if(object instanceof PolygonMapObject){
                shape = createPolygon((PolygonMapObject) object);
            } else if(object instanceof PolylineMapObject){
                shape = createPolyline((PolylineMapObject) object);
            } else {
                System.out.println("Unsupported map object. (For now! 👀)");
                return;
            }

            // Collidable body
            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(def);
            body.createFixture(shape, 1);

            // Cleanup
            shape.dispose();

        }
    }

    private static ChainShape createPolygon(PolygonMapObject polygon){
        float[] vecs = polygon.getPolygon().getTransformedVertices();
        Vector2[] worldVecs = new Vector2[vecs.length/2+1];
        for (int i = 0; i < worldVecs.length - 1; i++) {
            worldVecs[i] = new Vector2(vecs[i*2]/PPM, vecs[i*2+1]/PPM);
            if(i== worldVecs.length-2){
                worldVecs[i+1] = new Vector2(vecs[0]/PPM, vecs[1]/PPM);
            }
        }

        return createChainShape(worldVecs);
    }

    private static ChainShape createPolyline(PolylineMapObject polyline){
        float[] vecs = polyline.getPolyline().getTransformedVertices();
        Vector2[] worldVecs = new Vector2[vecs.length/2];
        for (int i = 0; i < worldVecs.length; i++) {
            worldVecs[i] = new Vector2(vecs[i*2]/PPM, vecs[i*2+1]/PPM);
        }

        return createChainShape(worldVecs);
    }

    private static ChainShape createChainShape(Vector2[] vecs){
        ChainShape cs = new ChainShape();
        cs.createChain(vecs);
        return cs;
    }
}
