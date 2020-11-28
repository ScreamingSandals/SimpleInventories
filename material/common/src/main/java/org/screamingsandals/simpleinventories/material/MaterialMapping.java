package org.screamingsandals.simpleinventories.material;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MaterialMapping {

    @Getter
    protected Platform platform;
    protected final Map<String, MaterialHolder> materialMapping = new HashMap<>();

    private static MaterialMapping mapping = null;
    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(((?:(?<namespace>[A-Za-z][A-Za-z0-9_.\\-]*):)?(?<material>[A-Za-z][A-Za-z0-9_.\\-/ ]*)(?::)?(?<durability>\\d+)?)|((?<id>\\d+)(?::)?(?<data>\\d+)?))$");

    public static Optional<MaterialHolder> resolve(String material) {
        if (mapping == null) {
            throw new UnsupportedOperationException("Material mapping is not initialized yet.");
        }
        material = material.trim();

        Matcher matcher = RESOLUTION_PATTERN.matcher(material);

        if (matcher.group("material") != null) {

            /*String namespace = matcher.group("namespace");*/ // namespace is currently useless on vanilla

            String name = matcher.group("material").toUpperCase();
            String durability = matcher.group("durability");

            if (durability != null && !durability.isEmpty()) {
                if (mapping.materialMapping.containsKey(name.toUpperCase() + ":" + durability)) {
                    MaterialHolder holder = mapping.materialMapping.get(name.toUpperCase() + ":" + durability);
                    return Optional.of(holder.newDurability(Short.parseShort(durability)));
                } else if (mapping.materialMapping.containsKey(name.toUpperCase())) {
                    MaterialHolder holder = mapping.materialMapping.get(name.toUpperCase());
                    return Optional.of(holder.newDurability(Short.parseShort(durability)));
                }
            } else if (mapping.materialMapping.containsKey(name)) {
                return Optional.of(mapping.materialMapping.get(name));
            }
        } else if (matcher.group("id") != null) {
            String id = matcher.group("id").toUpperCase();
            String data = matcher.group("data");

            if (data != null && !data.isEmpty()) {
                if (mapping.materialMapping.containsKey(id + ":" + data)) {
                    return Optional.of(mapping.materialMapping.get(id + ":" + data));
                } else if (mapping.materialMapping.containsKey(id)) {
                    MaterialHolder holder = mapping.materialMapping.get(id);
                    return Optional.of(holder.newDurability(Short.parseShort(data)));
                }
            } else if (mapping.materialMapping.containsKey(id)) {
                return Optional.of(mapping.materialMapping.get(id));
            }
        }
        return Optional.empty();
    }

    @SneakyThrows
    public static void init(Class<? extends MaterialMapping> materialMapping) {
        if (mapping != null) {
            throw new UnsupportedOperationException("Material mapping is already initialized.");
        }

        MaterialMapping mapping = materialMapping.getConstructor().newInstance();
        mapping.applyBaseMapping();
    }

    private void applyBaseMapping() {
        // Legacy remapping
        // TODO

        // Flattening remapping
        f2f("ZOMBIFIED_PIGLIN_SPAWN_EGG", "ZOMBIE_PIGMAN_SPAWN_EGG");
        f2f("GREEN_DYE", "CACTUS_GREEN");
        f2f("YELLOW_DYE", "DANDELION_YELLOW");
        f2f("RED_DYE", "ROSE_RED");
        f2f("OAK_SIGN", "SIGN");
        f2f("BIRCH_SIGN", "SIGN");
        f2f("DARK_OAK_SIGN", "SIGN");
        f2f("JUNGLE_SIGN", "SIGN");
        f2f("SPRUCE_SIGN", "SIGN");
        f2f("ACACIA_SIGN", "SIGN");
        f2f("OAK_WALL_SIGN", "SIGN");
        f2f("BIRCH_WALL_SIGN", "SIGN");
        f2f("BIRCH_WALL_SIGN", "SIGN");
        f2f("DARK_OAK_WALL_SIGN", "SIGN");
        f2f("JUNGLE_WALL_SIGN", "SIGN");
        f2f("SPRUCE_WALL_SIGN", "SIGN");
        f2f("ACACIA_WALL_SIGN", "SIGN");
    }

    private void f2f(String material, String material2) {
        if (materialMapping.containsKey(material.toUpperCase()) && !materialMapping.containsKey(material2.toUpperCase())) {
            materialMapping.put(material2.toUpperCase(), materialMapping.get(material.toUpperCase()));
        } else if (materialMapping.containsKey(material2.toUpperCase()) && !materialMapping.containsKey(material.toUpperCase())) {
            materialMapping.put(material.toUpperCase(), materialMapping.get(material2.toUpperCase()));
        }
    }

    /* For Materials where the name is same */
    private void f2l(String material, int legacyId) {
        f2l(material, material, legacyId);
    }

    /* For Materials where the name is changed */
    private void f2l(String flatteningMaterial, String legacyMaterial, int legacyId) {
        f2l(flatteningMaterial, legacyMaterial, legacyId, (byte) 0);
    }
    /* For Materials where the name is changed and data is not zero*/
    private void f2l(String flatteningMaterial, String legacyMaterial, int legacyId, byte data) {
        flatteningMaterial = flatteningMaterial.toUpperCase();
        legacyMaterial = legacyMaterial.toUpperCase();
        if (getPlatform().isUsingLegacyNames()) {
            if (materialMapping.containsKey(legacyMaterial)) {
                MaterialHolder holder = materialMapping.get(legacyMaterial).newDurability(data);
                if (flatteningMaterial.equals(legacyMaterial)) {
                    // if data is not 0, than idk what to do
                } else if (!materialMapping.containsKey(flatteningMaterial)) {
                    materialMapping.put(flatteningMaterial, holder);
                }

                if (!materialMapping.containsKey(String.valueOf(legacyId)) && data == 0) {
                    materialMapping.put(String.valueOf(legacyId), holder);
                }
                if (!materialMapping.containsKey(legacyId + ":" + data)) {
                    materialMapping.put(legacyId + ":" + data, holder);
                }
            }
        } else {
            if (materialMapping.containsKey(flatteningMaterial)) {
                MaterialHolder holder = materialMapping.get(flatteningMaterial);
                if (flatteningMaterial.equals(legacyMaterial)) {
                    // if data is not 0, than idk what to do
                } else if (!materialMapping.containsKey(legacyMaterial)) {
                    materialMapping.put(legacyMaterial, holder);
                }

                if (!materialMapping.containsKey(String.valueOf(legacyId)) && data == 0) {
                    materialMapping.put(String.valueOf(legacyId), holder);
                }
                if (!materialMapping.containsKey(legacyId + ":" + data)) {
                    materialMapping.put(legacyId + ":" + data, holder);
                }
            }
        }
    }
}
