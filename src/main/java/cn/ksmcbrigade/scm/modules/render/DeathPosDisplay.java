package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;

import java.io.IOException;

public class DeathPosDisplay extends Module {

    public DeathPosDisplay() throws IOException {
        super(DeathPosDisplay.class.getSimpleName(),ModuleType.RENDER);
    }
}
