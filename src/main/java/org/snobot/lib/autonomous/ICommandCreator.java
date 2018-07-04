
package org.snobot.lib.autonomous;

import java.util.List;

import org.snobot.lib.ASnobot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * CommandCreator Interface Reference All autonomous commands have to have a
 * create method with this format.
 */
public interface ICommandCreator<RobotType extends ASnobot>
{
    Command createCommand(List<String> aArgs, RobotType aSnobot);
}
