package com.snobot.lib.autonomous;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class ObservableSendableChooser<V> extends SendableChooser<V>
{
    private NetworkTable mTable;

    @Override
    public void initSendable(SendableBuilder aBuilder)
    {
        super.initSendable(aBuilder);

        SendableBuilderImpl castBuilder = (SendableBuilderImpl) aBuilder;
        mTable = castBuilder.getTable();
    }

    public void addSelectionChangedListener(TableEntryListener aListener)
    {
        mTable.addEntryListener("selected", aListener, 0xFF);
    }
}
