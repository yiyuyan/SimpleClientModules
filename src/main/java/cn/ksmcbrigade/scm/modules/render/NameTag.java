package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;

public class NameTag extends Module {

    public NameTag() {
        super(NameTag.class.getSimpleName(),ModuleType.RENDER);
    }
}
