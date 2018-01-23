package org.snobot.lib.modules;

public interface IUpdateableModule
{

    /**
     * Gathering and storing current sensor information. Ex. Motor Speed.
     */
    void update();
}
