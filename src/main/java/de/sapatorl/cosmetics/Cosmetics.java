package de.sapatorl.cosmetics;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Liste aller Cosmetics + aktuell gewähltes Cosmetic (wird gespeichert). */
public final class Cosmetics {
    public record Entry(String id, String name, int color) {}

    public static final List<Entry> ALL = List.of(
            new Entry("krone", "Krone", 0xFFC542),
            new Entry("zylinder", "Zylinder", 0xAAAAAA),
            new Entry("heiligenschein", "Heiligenschein", 0xFFF3A0),
            new Entry("cap", "Cap", 0x55CCFF),
            new Entry("teufelshoerner", "Teufelshörner", 0xFF5555),
            new Entry("katzenohren", "Katzenohren", 0xF29BB8),
            new Entry("sonnenbrille", "Sonnenbrille", 0x55AAAA),
            new Entry("partyhut", "Partyhut", 0xFF66CC),
            new Entry("kopfhoerer", "Gaming-Headset", 0xB57BFF)
    );

    public static final Logger LOG = LoggerFactory.getLogger("sapatorl_cosmetics");
    private static boolean loggedApply = false;
    private static String lastSkip = "";

    private static String selected = "";
    private static ItemStack cachedStack = ItemStack.EMPTY;

    private Cosmetics() {}

    public static String selected() {
        return selected;
    }

    public static void select(String id) {
        selected = id == null ? "" : id;
        cachedStack = ItemStack.EMPTY;
        loggedApply = false;
        lastSkip = "";
        LOG.info("[sapatorl_cosmetics] Ausgewaehlt: '{}'", selected);
        save();
    }

    /** Item, das als Kopf-Cosmetic gerendert wird (leer = nichts). */
    public static ItemStack stack() {
        if (selected.isEmpty()) return ItemStack.EMPTY;
        if (cachedStack.isEmpty()) {
            ItemStack s = new ItemStack(Items.PAPER);
            s.set(DataComponents.ITEM_MODEL, Identifier.fromNamespaceAndPath("cosmetics", selected));
            cachedStack = s;
        }
        return cachedStack;
    }

    /** Setzt das Cosmetic als Kopf-Item in den Render-State (nur eigener Spieler). */
    public static void applyTo(LivingEntity entity, LivingEntityRenderState state) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || entity != mc.player) return;
        ItemStack cosmetic = stack();
        if (cosmetic.isEmpty()) return;
        if (!entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            skip("Helm/Kopf-Item ausgeruestet");
            return;
        }
        state.wornHeadType = null;
        state.wornHeadProfile = null;
        mc.getItemModelResolver().updateForLiving(state.headItem, cosmetic, ItemDisplayContext.HEAD, entity);
        if (!loggedApply) {
            loggedApply = true;
            LOG.info("[sapatorl_cosmetics] Cosmetic '{}' angewendet, headItem leer = {}", selected, state.headItem.isEmpty());
        }
    }

    private static void skip(String why) {
        if (!why.equals(lastSkip)) {
            lastSkip = why;
            LOG.info("[sapatorl_cosmetics] Cosmetic nicht angezeigt: {}", why);
        }
    }

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("sapatorl_cosmetics.txt");
    }

    public static void load() {
        try {
            Path f = file();
            if (Files.exists(f)) {
                String id = Files.readString(f, StandardCharsets.UTF_8).trim();
                if (ALL.stream().anyMatch(e -> e.id().equals(id))) selected = id;
            }
        } catch (Exception ignored) {
        }
    }

    private static void save() {
        try {
            Files.writeString(file(), selected, StandardCharsets.UTF_8);
        } catch (Exception ignored) {
        }
    }
}
