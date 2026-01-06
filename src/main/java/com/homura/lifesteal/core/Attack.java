package com.homura.lifesteal.core;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
        // 仅在服务端执行，防止客户端和服务端各执行一次导致重复回血
        if (!(event.getEntity().level() instanceof ServerLevel)) return;

        DamageSource source = event.getSource();

        // 仅允许近战和远程攻击触发，环境伤害、反伤等非主手攻击来源
        if (!source.is(DamageTypes.MOB_ATTACK)
                && !source.is(DamageTypes.PLAYER_ATTACK)
                && !source.is(DamageTypes.ARROW)) return;

        // 获取攻击者实体，必须是存活的生物
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;
        if (!attacker.isAlive()) return;

        // 获取吸血附魔等级
        ItemStack weapon = attacker.getMainHandItem();
        int level = weapon.getEnchantmentLevel(Enroll.LIFESTEAL.get());
        if (level <= 0) return;

        //避免没造成伤害也能吸血
        float damage = event.getAmount();
        if (damage <= 0.0F) return;

        attacker.heal(1.0F);
    }

}

