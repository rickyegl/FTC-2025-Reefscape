package org.firstinspires.ftc.teamcode.common.commandbase.command.safe;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

@Config
public class WaitUntilExtend extends CommandBase {
    private Extension extension;
    private final double position;
    private static int offsetE = 15;

    public WaitUntilExtend(Extension e, double position){
        this.extension = e;
        this.position = position;
    }

    @Override
    public void initialize(){
        while (extension.getPositionCM() >= position - offsetE || extension.getPositionCM() <= position + offsetE);
    }
}
