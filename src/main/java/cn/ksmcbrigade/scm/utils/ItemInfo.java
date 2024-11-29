package cn.ksmcbrigade.scm.utils;

import net.minecraft.world.entity.item.ItemEntity;

public class ItemInfo {

    public final ItemEntity entity;
    public int count;

    public ItemInfo(ItemEntity item,int count){
        this.entity = item;
        this.count = count;
    }

    public ItemInfo copy(){
        return new ItemInfo(this.entity,this.count);
    }
}
