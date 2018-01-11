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
    public void initSendable(SendableBuilder builder)
    {
        super.initSendable(builder);

        SendableBuilderImpl castBuilder = (SendableBuilderImpl) builder;
        mTable = castBuilder.getTable();
    }

    public void addSelectionChangedListener(TableEntryListener listener)
    {
        mTable.addEntryListener("selected", listener, 0xFF);
    }
}
