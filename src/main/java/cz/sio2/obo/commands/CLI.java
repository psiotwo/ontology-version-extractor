package cz.sio2.obo.commands;

import picocli.CommandLine;

@CommandLine.Command(subcommands = {
        Extract.class,
        Transform.class
})
public class CLI {
    public static void main(final String... args) {
        final int exitCode = new CommandLine(new CLI()).execute(args);
        System.exit(exitCode);
    }
}