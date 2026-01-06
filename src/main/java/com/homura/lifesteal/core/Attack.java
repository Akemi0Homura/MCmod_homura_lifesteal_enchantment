package com.homura.lifesteal.core;


import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 逻辑模块<br>
 * 附魔后，攻击敌人会回血<br>
 * @author Akemi0Homura
 */
@Mod.EventBusSubscriber(modid = "homura_lifesteal_enchantment")
public class Attack {
    @SubscribeEvent
    public static void onAttack(LivingHurtEvent event) {

        // 只在服务端执行，防止客户端重复回血
        if (event.getEntity().level().isClientSide) return;

        DamageSource source = event.getSource();

        // 获取伤害来源实体（攻击者）
        Entity src = source.getEntity();
        if (!(src instanceof LivingEntity attacker)) return;

        // 获取攻击者主手武器
        ItemStack weapon = attacker.getMainHandItem();

        // 获取自定义吸血附魔等级
        int level = weapon.getEnchantmentLevel(Enroll.LIFESTEAL.get());
        if (level <= 0) return;

        // 给攻击者回血（不会超过最大生命值）
        attacker.heal(1.0F);
    }

}

