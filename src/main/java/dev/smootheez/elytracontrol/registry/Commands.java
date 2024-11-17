package dev.smootheez.elytracontrol.registry;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

public class Commands implements ClientCommandRegistrationCallback {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("setdestination")
                .then(ClientCommandManager.argument("Name", StringArgumentType.string()))
                .executes(this::executeCommand));
    }

    private int executeCommand(CommandContext<FabricClientCommandSource> context) {
        String destinationName = StringArgumentType.getString(context, "Name");
        context.getSource().sendFeedback(Text.literal("Destination set to: " + destinationName));
        return Command.SINGLE_SUCCESS;
    }}
