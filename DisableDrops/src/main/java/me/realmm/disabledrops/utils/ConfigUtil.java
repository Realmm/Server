package me.realmm.disabledrops.utils;

import me.realmm.disabledrops.DisableDrops;
import org.bukkit.Material;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class ConfigUtil {

    private static final Set<Material> disabled = new HashSet<>();

    private ConfigUtil(){}

    public static void populateDisabledMaterials() {
        DisableDrops.get().getConfig().getStringList("disabled-drops").stream().map(Material::matchMaterial).forEach(disabled::add);
    }

    public static Collection<Material> getDisabledMaterials() {
        return Collections.unmodifiableCollection(disabled);
    }

}
