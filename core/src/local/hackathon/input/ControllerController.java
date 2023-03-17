package local.hackathon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;

import java.util.ArrayList;

public class ControllerController {

    class Change implements ControllerListener {
        ControllerController controllerController;


        Change(ControllerController controllerController){
            this.controllerController = controllerController;
        }

        @Override
        public void connected(Controller controller) {}

        @Override
        public void disconnected(Controller controller) {
            controllerController.removeController(controller);
        }

        @Override
        public boolean buttonDown(Controller controller, int buttonCode) {
            return false;
        }

        @Override
        public boolean buttonUp(Controller controller, int buttonCode) {
            return false;
        }

        @Override
        public boolean axisMoved(Controller controller, int axisCode, float value) {
            return false;
        }
    }

    private ArrayList<Controller> controllers = new ArrayList<>();

    public ControllerController(){
        for (Controller controller : Controllers.getControllers()) {
            Gdx.app.log("Controller name: ", controller.getName());
            controllers.add(controller);
            controller.addListener(new Change(this));
        }
        Controllers.addListener(new ControllerAdapter(){
            @Override
            public void connected(Controller controller) {
                addController(controller);
            }
        });
    }

    public void addController(Controller controller){
        controllers.add(controller);
    }

    public void removeController(Controller controller){
        controllers.remove(controller);
    }

    public ArrayList<Controller> getControllers() {
        return controllers;
    }

    public int controllerCount(){
        return controllers.size();
    }
}
