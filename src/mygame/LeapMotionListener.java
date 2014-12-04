/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;


import java.io.IOException;
import java.lang.Math;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

public class LeapMotionListener extends Listener {
    
    private static double rollAngle;
   
    public LeapMotionListener() {

    }
    
    @Override
    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    @Override
    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    @Override
    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }
    
    @Override
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Hand rightHand = controller.frame().hands().rightmost();
        Vector normal = rightHand.palmNormal();
        Vector direction = rightHand.direction();

        rollAngle = Math.toDegrees(normal.roll());

    }
    
    public static double getRoll() {
        return rollAngle;
    }
}

