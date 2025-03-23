package cz.sio2.ontology.version.commands;

import cz.sio2.ontology.version.model.HeaderExtractor;
import cz.sio2.ontology.version.model.Utils;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.net.URL;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "extract",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Extracts the latest versions of ontologies."
)
@Slf4j
class Extract implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "The configuration file URL.")
    private URL configurationUrl;

    @CommandLine.Option(names = {"-o"}, description = "Output file.")
    private String outputFile;

    @Override
    public Integer call() {
        try {
            new HeaderExtractor().extract(Utils.loadConfiguration(configurationUrl), outputFile);
        } catch (Exception e) {
            log.error("Error during extraction: ", e);
            return -1;
        }
        return 0;
    }
}