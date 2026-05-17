package org.sjk.pocketwords;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;
import org.sjk.pocketwords.core.Core;
import org.sjk.pocketwords.core.properties.impl.PropertyReaderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public final class PocketWords {

    private static final Logger LOGGER = LoggerFactory.getLogger(PocketWords.class);

    static void main(final String[] args) {
        LOGGER.info("Starting Pocket Words");

        try {
            final Properties properties = new PropertyReaderImpl().readProperties();

            final Options options = new Options();
            options.addOption("h", "help", false, "Print this message");
            options.addOption("i", "input-file", true, "Path to the eBook file");
            options.addOption("o", "output-file", true, "Path to the output file");
            options.addOption("v", "version", false, "Print version");

            options.getOption("i").setRequired(true);
            final CommandLineParser parser = new DefaultParser();
            final CommandLine line = parser.parse(options, args);
            if (line.hasOption("h")) {
                final HelpFormatter formatter = HelpFormatter.builder().get();
                final String cmd = "java %s.jar".formatted(properties.getProperty("build.jar.name"));
                final String name = properties.getProperty("name");
                formatter.printHelp(cmd, name, options, "", true);
                System.exit(0);
            }
            if (line.hasOption("v")) {
                final String version = properties.getProperty("version");
                final String buildTimestamp = properties.getProperty("build.timestamp");
                System.out.printf("%s %s%n", version, buildTimestamp);
                System.exit(0);
            }
            PocketWords.passArgumentsToCore(line, options);
        } catch (final IOException | ParseException e) {
            LOGGER.error("Unexpected error", e);
            System.exit(-1);
        }
        Core.getInstance().run();
    }

    private static void passArgumentsToCore(final CommandLine line, final Options options) throws ParseException {
        final Path inputFilePath = Path.of(line.getOptionValue(options.getOption("i")));
        Core.getInstance().setInputFilePath(inputFilePath);
        final Path outputFilePath = line.getParsedOptionValue(options.getOption("o"));
        Core.getInstance().setOutputFilePath(outputFilePath);
    }
}
