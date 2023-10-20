package com.bevan.mongofit.Tabs.StepCounter;

// Will listen to step alerts
public interface StepListener {
    //gets the steps and numbers of steps taken and can be changed in the step counter
    public void stepCounter(long timeNs);

}
