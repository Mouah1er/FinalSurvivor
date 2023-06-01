package fr.twah2em.survivor.commands.internal;

import java.util.Arrays;
import java.util.List;

public class SurvivorCommandMetadata {
    private final String name;
    private final String description;
    private final String usage;
    private final List<String> aliases;
    private final List<SurvivorSubCommand> subCommands;
    private final String permission;

    private SurvivorCommandMetadata(String name, String description, String usage, List<String> aliases, List<SurvivorSubCommand> subCommands, String permission) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.subCommands = subCommands;

        final boolean hasNoPermission = permission == null || permission.equals("") || permission.isBlank() || permission.isEmpty();
        this.permission = hasNoPermission ? null : permission;
    }

    public String name() {
        return this.name;
    }

    public String description() {
        return this.description;
    }

    public String usage() {
        return this.usage;
    }

    public List<String> aliases() {
        return this.aliases;
    }

    public List<SurvivorSubCommand> subCommands() {
        return this.subCommands;
    }

    public String permission() {
        return this.permission;
    }

    public static class SurvivorCommandMetadataBuilder {
        private final String name;
        private String description;
        private String usage;
        private List<String> aliases;
        private List<SurvivorSubCommand> subCommands;
        private String permission;

        public SurvivorCommandMetadataBuilder(String name) {
            this.name = name;
        }

        public SurvivorCommandMetadataBuilder withDescription(String description) {
            this.description = description;

            return this;
        }

        public SurvivorCommandMetadataBuilder withUsage(String usage) {
            this.usage = usage;

            return this;
        }

        public SurvivorCommandMetadataBuilder withAliases(String... aliases) {
            return withAliases(Arrays.asList(aliases));
        }

        public SurvivorCommandMetadataBuilder withAliases(List<String> aliases) {
            this.aliases = aliases;

            return this;
        }

        public SurvivorCommandMetadataBuilder withSubCommands(SurvivorSubCommand... subCommands) {
            return withSubCommands(Arrays.asList(subCommands));
        }

        public SurvivorCommandMetadataBuilder withSubCommands(List<SurvivorSubCommand> subCommands) {
            this.subCommands = subCommands;

            return this;
        }

        public SurvivorCommandMetadataBuilder withPermission(String permission) {
            this.permission = permission;

            return this;
        }

        public SurvivorCommandMetadata build() {
            return new SurvivorCommandMetadata(name, description, usage, aliases, subCommands, permission);
        }
    }
}
