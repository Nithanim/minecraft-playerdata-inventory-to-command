import com.flowpowered.nbt.ByteArrayTag;
import com.flowpowered.nbt.ByteTag;
import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.DoubleTag;
import com.flowpowered.nbt.FloatTag;
import com.flowpowered.nbt.IntArrayTag;
import com.flowpowered.nbt.IntTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.LongTag;
import com.flowpowered.nbt.ShortArrayTag;
import com.flowpowered.nbt.ShortTag;
import com.flowpowered.nbt.StringTag;
import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.stream.NBTInputStream;
import org.apache.commons.collections4.ListUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("I need the playerdata/<uuid>.dat file!");
            System.exit(1);
            return;
        }

        try (InputStream in = Files.newInputStream(Paths.get(args[0]))) {

            NBTInputStream nbtin = new NBTInputStream(in);
            CompoundTag root = (CompoundTag) nbtin.readTag();
            ListTag inv = (ListTag) root.getValue().get("Inventory");

            List<List<CompoundTag>> rawItemsPartitioned = ListUtils.partition((List<CompoundTag>) inv.getValue(), 27);
            List<String> commands = rawItemsPartitioned.stream().map(Main::itemsToChestCommand).collect(Collectors.toList());

            System.out.println(String.join("\n\n", commands));
        }
    }

    private static void fixSlot(CompoundMap item, int i) {
        item.put(new ByteTag("Slot", (byte) i));
    }

    private static String treeToJson(Tag tag) {
        Class<? extends Tag> c = tag.getClass();
        if (c == StringTag.class) {
            return treeToJson((StringTag) tag);
        } else if (c == ListTag.class) {
            return treeToJson((ListTag) tag);
        } else if (c == IntArrayTag.class) {
            return treeToJson((IntArrayTag) tag);
        } else if (c == ShortArrayTag.class) {
            return treeToJson((ShortArrayTag) tag);
        } else if (c == ByteArrayTag.class) {
            return treeToJson((ByteArrayTag) tag);
        } else if (c == FloatTag.class) {
            return treeToJson((FloatTag) tag);
        } else if (c == DoubleTag.class) {
            return treeToJson((DoubleTag) tag);
        } else if (c == LongTag.class) {
            return treeToJson((LongTag) tag);
        } else if (c == IntTag.class) {
            return treeToJson((IntTag) tag);
        } else if (c == ShortTag.class) {
            return treeToJson((ShortTag) tag);
        } else if (c == ByteTag.class) {
            return treeToJson((ByteTag) tag);
        } else if (c == CompoundTag.class) {
            return treeToJson((CompoundTag) tag);
        } else {
            throw new IllegalArgumentException("Unknown tag " + c);
        }
    }

    private static String treeToJson(CompoundTag tag) {
        CompoundMap map = tag.getValue();
        String between = map.entrySet()
                .stream()
                .map(e -> "\"" + e.getKey() + "\":" + treeToJson(e.getValue()))
                .collect(Collectors.joining(","));
        return "{" + between + "}";
    }

    private static String treeToJson(IntArrayTag tag) {
        int[] vs = tag.getValue();
        return "[" + Arrays.stream(vs).mapToObj(String::valueOf).collect(Collectors.joining(",")) + "]";
    }

    private static String treeToJson(ShortArrayTag tag) {
        short[] vs = tag.getValue();
        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i < vs.length; i++) {
            a.add(String.valueOf(vs[i]));
        }
        return "[" + String.join(",", a) + "]";
    }

    private static String treeToJson(ByteArrayTag tag) {
        byte[] vs = tag.getValue();
        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i < vs.length; i++) {
            a.add(String.valueOf(vs[i]));
        }
        return "[" + String.join(",", a) + "]";
    }

    private static String treeToJson(ListTag tag) {
        List<Tag> vs = tag.getValue();
        return "[" + vs.stream().map(Main::treeToJson).collect(Collectors.joining(",")) + "]";
    }

    private static String treeToJson(StringTag tag) {
        return "\"" + tag.getValue() + "\"";
    }

    private static String treeToJson(FloatTag tag) {
        return String.valueOf(tag.getValue());
    }

    private static String treeToJson(DoubleTag tag) {
        return String.valueOf(tag.getValue());
    }

    private static String treeToJson(LongTag tag) {
        return String.valueOf(tag.getValue());
    }

    private static String treeToJson(IntTag tag) {
        return String.valueOf(tag.getValue());
    }

    private static String treeToJson(ShortTag tag) {
        return String.valueOf(tag.getValue());
    }

    private static String treeToJson(ByteTag tag) {
        return String.valueOf(tag.getValue());
    }

    private static String itemsToChestCommand(List<CompoundTag> rawItems) {
        int slot = 0;
        List<String> items = new ArrayList<>();
        for (CompoundTag item : rawItems) {
            CompoundMap map = item.getValue();
            fixSlot(map, slot++);
            items.add(treeToJson(item));
        }

        String cmd = "/give @p minecraft:chest 1 0 {display: {Name:\"Magic Surprise Box\"}, BlockEntityTag:{Items:[${}] }}";
        return cmd.replace("${}", String.join(",", items));
    }
}
